package cd.world.blocks.multi.craft;

import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

/**
 * MultiCrafter. Refers from Li plum's MultiCrafterLib, but fix a bug.
 * Support both single or multiple selective and concurrent mode
 */
public class CDMultiCrafter extends Block{

    public Seq<RecipePair> recipes = new Seq<>();
    public int[] defaultSelection = {0};

    //Vanilla GenericCrafter
    public boolean ignoreLiquidFullness;
    public boolean dumpExtraLiquid;
    public float warmupSpeed = 0.019f;
    public DrawBlock drawer = new DrawDefault();

    public CDMultiCrafter(String name){
        super(name);
        update = true;
        solid = true;
        sync = true;
        flags = EnumSet.of(BlockFlag.factory);
        ambientSound = Sounds.machine;
        configurable = true;
        saveConfig = true;
        ambientSoundVolume = 0.03f;
        config(int[].class, CDMultiCrafterBuild::setRecipes);
        consume(new ConsumePowerDynamic(b -> ((CDMultiCrafterBuild)b).recipePower()));
    }

    @Override
    public void init(){
        super.init();
        for(var recipe:recipes){
            if(!recipe.in.items.isEmpty()){
                hasItems = true;
                recipe.in.items.each(s->itemFilter[s.item.id] = true);
            }
            if(!recipe.out.items.isEmpty()){
                hasItems = true;
            }
            if(!recipe.in.liquids.isEmpty()){
                hasLiquids = true;
                recipe.in.liquids.each(s->liquidFilter[s.liquid.id] = true);
            }
            if(!recipe.out.liquids.isEmpty()){
                hasLiquids = true;
            }
            if(recipe.in.power!=0f||recipe.out.power!=0f) hasPower = true;
        }
    }

    public class CDMultiCrafterBuild extends Building implements HeatBlock, HeatConsumer{
        public float[] sideHeat = new float[4];
        public float heat;
        public float warmup;
        public Seq<RecipePair> pairs = new Seq<>();

        /*Every recipe has its progress. It is between 0 and 1. Each frame, if enabled,
         * it will add some number*/
        public ObjectFloatMap<RecipePair> craftTimes = new ObjectFloatMap<>();

        @Override
        public void placed(){
            super.placed();

            if(pairs.isEmpty()) setRecipes(defaultSelection);
        }

        public void setRecipes(int[] indexes){
            pairs.clear();
            for(var index : indexes){
                RecipePair pair;
                if(index > recipes.size || index == -1){
                    pair = RecipePair.EMPTY_RECIPE_PAIR;
                }else{
                    pair = recipes.get(index);
                }
                pairs.add(pair);
                craftTimes.put(pair, 0f);
            }
        }

        public float recipePower(){
            float total = 0f;
            for(var pair : pairs){
                total += pair.out.power;
                total -= pair.in.power;
            }
            return total;
        }

        @Override
        public void buildConfiguration(Table table){
            configure(new int[]{0});
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            var ignored = read.i();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(1);
        }

        //region heat
        @Override
        public float heat(){
            return heat;
        }

        @Override
        public float heatFrac(){
            float total = 0f;
            for(var pair : pairs){
                total += pair.out.heat;
            }
            return heat / total;
        }

        @Override
        public float[] sideHeat(){
            return sideHeat;
        }

        @Override
        public float heatRequirement(){
            float total = 0f;
            for(var pair : pairs){
                total += pair.in.heat;
            }
            return total;
        }

        //endregion

        //region crafter
        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public boolean shouldAmbientSound(){
            return efficiency > 0;
        }

        @Override
        public int getMaximumAccepted(Item item){
            return itemCapacity;
        }

        @Override
        public boolean shouldConsume(){
            for(var pair : pairs){
                if(pair.out.items != null){
                    for(var output : pair.out.items){
                        if(items.get(output.item) + output.amount > itemCapacity){
                            return false;
                        }
                    }
                }
                if(pair.out.liquids != null && !ignoreLiquidFullness){
                    boolean allFull = true;
                    for(var output : pair.out.liquids){
                        if(liquids.get(output.liquid) >= liquidCapacity - 0.001f){
                            if(!dumpExtraLiquid){
                                return false;
                            }
                        }else{
                            //if there's still space left, it's not full for all liquids
                            allFull = false;
                        }
                    }

                    //if there is no space left for any liquid, it can't reproduce
                    if(allFull){
                        return false;
                    }
                }
            }

            return enabled;
        }

        @Override
        public float warmup(){
            return warmup;
        }

        @Override
        public void updateTile(){
            for(var pair : pairs){
                if(efficiency > 0){

                    craftTimes.increment(pair, 0, getProgressIncrease(pair.craftTime));
                    warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                    //continuously output based on efficiency
                    if(pair.out.liquids != null){
                        float inc = getProgressIncrease(1f);
                        for(var output : pair.out.liquids){
                            handleLiquid(this, output.liquid, Math.min(output.amount * inc,
                            liquidCapacity - liquids.get(output.liquid)));
                        }
                    }

                    if(wasVisible && Mathf.chanceDelta(pair.updateEffectChance / pairs.size)){
                        pair.updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size << 2));
                    }
                }else{
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }


                if(craftTimes.get(pair, 0f) >= 1f){
                    craft(pair);
                }

                dumpRecipeOutput(pair.out);
            }
        }

        @Override
        public float getProgressIncrease(float baseTime){
            //limit progress increase by maximum amount of liquid it can produce
            float scaling = 1f, max = 1f;
            for(var pair : pairs){
                if(pair.out.liquids != null){
                    max = 0f;
                    for(var s : pair.out.liquids){
                        float value = (liquidCapacity - liquids.get(s.liquid)) / (s.amount * edelta());
                        scaling = Math.min(scaling, value);
                        max = Math.max(max, value);
                    }
                }
            }
            //when dumping excess take the maximum value instead of the minimum.
            return super.getProgressIncrease(baseTime) * (dumpExtraLiquid ? Math.min(max, 1f) : scaling);
        }

        public void craft(RecipePair pair){
            consume(pair);

            if(pair.out.items != null){
                for(var output : pair.out.items){
                    for(int i = 0; i < output.amount; i++){
                        offload(output.item);
                    }
                }
            }

            if(wasVisible){
                pair.craftEffect.at(x, y);
            }

            var aim = craftTimes.get(pair, 0f) % 1f;
            craftTimes.increment(pair, 0f, aim - craftTimes.get(pair, 0f));

        }

        public void dumpRecipeOutput(Recipe recipe){
            if(recipe.items != null && timer(timerDump, dumpTime / timeScale)){
                for(ItemStack output : recipe.items){
                    dump(output.item);
                }
            }

            if(recipe.liquids.toArray() != null){
                for(int i = 0; i < recipe.liquids.size; i++){
                    int dir = recipe.parent.liquidOutputDirections.length > i ?
                    recipe.parent.liquidOutputDirections[i] : -1;
                    dumpLiquid(recipe.liquids.toArray()[i].liquid, 2f, dir);
                }
            }
        }

        public void consume(RecipePair pair){
            pair.in.items.each(s -> items.remove(s));
            pair.in.liquids.each(s -> liquids.remove(s.liquid, s.amount));
            pair.in.consumers.each(c -> c.trigger(this));
        }
        //endregion
    }
}
