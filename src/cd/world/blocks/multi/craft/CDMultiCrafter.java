package cd.world.blocks.multi.craft;

import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
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
        for(var recipe : recipes){
            if(!recipe.in.items.isEmpty()){
                hasItems = true;
                recipe.in.items.each(s -> itemFilter[s.item.id] = true);
            }
            if(!recipe.out.items.isEmpty()){
                hasItems = true;
            }
            if(!recipe.in.liquids.isEmpty()){
                hasLiquids = true;
                recipe.in.liquids.each(s -> liquidFilter[s.liquid.id] = true);
            }
            if(!recipe.out.liquids.isEmpty()){
                hasLiquids = true;
            }
            if(recipe.in.power != 0f || recipe.out.power != 0f) hasPower = true;
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
        /*Every recipe has its own efficiency, and there is always a building efficiency*/
        public ObjectFloatMap<RecipePair> efficiencies = new ObjectFloatMap<>();

        @Override
        public void placed(){
            super.placed();
            Log.info(pairs);
            if(pairs.isEmpty()) setRecipes(defaultSelection);
        }

        public void setRecipes(int[] indexes){
            pairs.clear();
            for(var index : indexes){
                RecipePair pair;
                if(index >= recipes.size || index == -1){
                    pair = RecipePair.EMPTY_RECIPE_PAIR;
                }else{
                    pair = recipes.get(index);
                }
                pairs.add(pair);
                craftTimes.put(pair, 0f);
                efficiencies.put(pair, 0f);
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
            configure(new int[]{0,1});
        }

        //This is the REAL method to calculate efficiency, controlling the crafter to work
        @Override
        public void updateConsumption(){

            //everything is valid when cheating
            if(cheating()){
                potentialEfficiency = enabled && productionValid() ? 1f : 0f;
                efficiency = optionalEfficiency = shouldConsume() ? potentialEfficiency : 0f;
                updateEfficiencyMultiplier();
                return;
            }

            //disabled -> nothing works
            if(!enabled){
                potentialEfficiency = efficiency = optionalEfficiency = 0f;
                return;
            }

            boolean prevValid = efficiency > 0, updated = shouldConsume() && productionValid();

            float minEfficiency = 1f;

            //first pass: get the minimum efficiency of any consumer
            for(var cons : block.nonOptionalConsumers){
                minEfficiency = Math.min(minEfficiency, cons.efficiency(this));
            }

            //same for optionals
            for(var cons : block.optionalConsumers){
                optionalEfficiency = Math.min(optionalEfficiency, cons.efficiency(this));
            }


            for(var pair : pairs){
                //Commundustry Hack Here
                float e = 1f;
                //Log.info(pair);

                //act as if a normal ConsumeItems
                for(var stack : pair.in.items){
                    e = items.has(stack.item, stack.amount) ? e : 0f;
                }

                //Log.info("After items: " + e);

                if(!pair.in.liquids.isEmpty()){
                    //act as if a normal ConsumeLiquids
                    float min = 1f;
                    float ed = edelta();
                    if(ed <= 0.00000001f || ed > ed){
                        min = 0f;
                    }
                    for(var stack : pair.in.liquids){
                        min = Math.min(liquids.get(stack.liquid) / (stack.amount * ed), min);

                        if(liquids.get(stack.liquid) / (stack.amount * delta()) > 0){
                            min = liquids.get(stack.liquid) / (stack.amount * delta());
                        }

                    }
                    e = Math.min(min, e);
                }

                //Log.info("After liquids: " + e);


                if(heatRequirement() != 0f){
                    //act as if HeatCrafter's
                    float over = Math.max(heat - heatRequirement(), 0f);
                    float heatE = Mathf.clamp(heat / heatRequirement()) + over / heatRequirement();
                    e = Math.min(heatE, e);
                }

                //Log.info("After heat: " + e);

                //load consumer
                for(var cons : pair.in.consumers){
                    e = Math.min(e, cons.efficiency(this));
                }

                //Log.info("After consumers: " + e);
                //Laser and pressure coming soon
                float increment = Math.min(e, minEfficiency) - efficiencies.get(pair, 0f);
                efficiencies.increment(pair, 0f, increment);
                //Hack done
            }


            //efficiency is now this minimum value
            efficiency = minEfficiency;
            optionalEfficiency = Math.min(optionalEfficiency, minEfficiency);

            //assign "potential"
            potentialEfficiency = efficiency;

            //no updating means zero efficiency
            if(!updated){
                efficiency = optionalEfficiency = 0f;
            }

            updateEfficiencyMultiplier();

            //second pass: update every consumer based on efficiency
            if(updated && prevValid && efficiency > 0){
                for(var cons : block.updateConsumers){
                    cons.update(this);
                }
                pairs.each(pair -> pair.in.liquids.each(s -> liquids.remove(s.liquid, s.amount)));
                pairs.each(pair -> pair.in.consumers.each(c -> c.update, c -> c.trigger(this)));
            }
        }

        @Override
        public boolean shouldConsume(){
            for(var pair : pairs){
                if(!pair.out.items.isEmpty()){
                    for(var output : pair.out.items){
                        if(items.get(output.item) + output.amount > itemCapacity){
                            //Log.info("item @ over", output.item);
                            return false;
                        }
                    }
                }
                if(!pair.out.liquids.isEmpty() && !ignoreLiquidFullness){
                    boolean allFull = true;
                    for(var output : pair.out.liquids){
                        if(liquids.get(output.liquid) >= liquidCapacity - 0.001f){
                            if(!dumpExtraLiquid){
                                //Log.info("liquid @ over", output.liquid);
                                return false;
                            }
                        }else{
                            //if there's still space left, it's not full for all liquids
                            allFull = false;
                        }
                    }

                    //if there is no space left for any liquid, it can't reproduce
                    if(allFull){
                        //Log.info("Liquid all full!");
                        return false;
                    }
                }
            }

            return enabled;
        }

        @Override
        public float heatRequirement(){
            float total = 0f;
            for(var pair : pairs){
                total += pair.in.heat;
            }
            return total;
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

        //endregion

        @Override
        public float[] sideHeat(){
            return sideHeat;
        }

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
        public float warmup(){
            return warmup;
        }

        @Override
        public void updateTile(){
            for(var pair : pairs){
                if(efficiency > 0){
                    if(efficiencies.get(pair, 0f) > 0){
                        Log.info(pair);
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
                    }
                }else{
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }


                if(craftTimes.get(pair, 0f) >= 1f){
                    craft(pair);
                }

                dumpRecipeOutput(pair);
            }
        }

        @Override
        public float getProgressIncrease(float baseTime){
            //limit progress increase by maximum amount of liquid it can produce
            float scaling = 1f, max = 1f;
            //Log.info(pairs);
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

        public void dumpRecipeOutput(RecipePair recipe){
            if(recipe.out.items != null && timer(timerDump, dumpTime / timeScale)){
                for(ItemStack output : recipe.out.items){
                    dump(output.item);
                }
            }

            if(recipe.out.liquids.toArray() != null){
                for(int i = 0; i < recipe.out.liquids.size; i++){
                    int dir = recipe.liquidOutputDirections.length > i ?
                    recipe.liquidOutputDirections[i] : -1;
                    dumpLiquid(recipe.out.liquids.toArray()[i].liquid, 2f, dir);
                }
            }
        }

        public void consume(RecipePair pair){
            pair.in.items.each(s -> items.remove(s));
            pair.in.consumers.each(c -> !c.update, c -> c.trigger(this));
        }
        //endregion
    }
}
