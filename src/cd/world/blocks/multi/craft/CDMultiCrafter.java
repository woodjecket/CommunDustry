package cd.world.blocks.multi.craft;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

/**
 * MultiCrafter.
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

    @Override
    public void setBars(){
        super.setBars();
        //no need for dynamic liquid bar
        removeBar("liquid");
        //set up liquid bars for liquid
        Seq<Liquid> seq = new Seq<>();
        recipes.each(pair -> {
            pair.in.liquids.each(s -> seq.add(s.liquid));
            pair.out.liquids.each(s -> seq.add(s.liquid));
        });
        //then display output buffer
        for(var liquid : seq){
            addLiquidBar(liquid);
        }

    }

    @Override
    public boolean rotatedOutput(int x, int y){
        return false;
    }

    @Override
    public void load(){
        super.load();

        drawer.load(this);
    }

    @Override
    public void setStats(){
        super.setStats();
        recipes.each(r -> r.buildStats(this));
    }

    public OrderedMap<String, Func<Building, Bar>> getBarMap(){
        return barMap;
    }

    public class CDMultiCrafterBuild extends Building implements HeatBlock, HeatConsumer{
        public float[] sideHeat = new float[4];
        public float heat;
        public float warmup;
        public RecipeView recipeView = new RecipeView();


        @Override
        public void placed(){
            //If there is no recipe, then set it
            super.placed();
            if(recipeView.isCraftsEmpty()) setRecipes(defaultSelection);
        }

        public void setRecipes(int[] indexes){
            recipeView.setRecipe(indexes);
        }

        @Override
        public void displayBars(Table table){
            //TODO let displayBars() create and manage Bar instead of init()
            var map = getBarMap();
            Seq<Liquid> seq = new Seq<>();
            recipeView.getPairs().each(p -> {
                p.in.liquids.each(s -> seq.add(s.liquid));
                p.out.liquids.each(s -> seq.add(s.liquid));
            });
            for(Func<Building, Bar> bar : block.listBars()){
                var barName = map.findKey(bar, false);
                if(!barName.contains("liquid-")){
                    var result = bar.get(this);
                    if(result == null) continue;
                    table.add(result).growX();
                    table.row();
                }
                for(var liquid : seq){
                    if(barName.contains(liquid.name)){
                        var result = bar.get(this);
                        if(result == null) continue;
                        table.add(result).growX();
                        table.row();
                        break;
                    }
                }
            }
        }

        public float recipePower(){
            float total = 0f;
            for(var pair : recipeView.getPairs()){
                total += pair.out.power;
                total -= pair.in.power;
            }
            return total;
        }

        @Override
        public void buildConfiguration(Table table){
            //Thanks for Singularity
            table.table(Tex.buttonTrans, prescripts -> {
                prescripts.defaults().grow().marginTop(0).marginBottom(0).marginRight(5).marginRight(5);
                prescripts.add(Core.bundle.get("select-recipe")).padLeft(5).padTop(5).padBottom(5);
                prescripts.row();
                prescripts.pane(buttons -> {
                    for(int i = 0; i < recipes.size; i++){
                        //For lambda
                        int s = i;
                        RecipePair p = recipes.get(i);
                        buttons.left().button(t -> {
                            t.left().defaults().left();
                            buildRecipeSimple(p, t);
                        }, Styles.underlineb, () -> recipeSelect(s))
                        .update(b -> b.setChecked(recipeView.selected(s)))
                        .fillY().growX().left().margin(5).marginTop(8).marginBottom(8).pad(4);
                        buttons.row();
                    }
                }).fill().maxHeight(280);
            });

            table.row();
        }

        @SuppressWarnings("MethodMayBeStatic")
        private void buildRecipeSimple(RecipePair pair, Table table){
            pair.in.buildTable(table);
            table.image(Icon.right).padLeft(8).padRight(8).size(30);
            pair.out.buildTable(table);
        }

        private void recipeSelect(int index){
            if(recipeView.selected(index)){
                recipeView.unselect(index);
            }else{
                recipeView.select(index);
            }
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


            for(var recipeDO : recipeView.getCrafts()){
                var pair = recipeDO.pair;
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

                recipeDO.efficiency = Math.min(e, minEfficiency);
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
                recipeView.getPairs().each(pair -> pair.in.liquids.each(s -> liquids.remove(s.liquid, s.amount)));
                recipeView.getPairs().each(pair -> pair.in.consumers.each(c -> c.update, c -> c.trigger(this)));
            }
        }

        @Override
        public boolean shouldConsume(){
            for(var pair : recipeView.getPairs()){
                if(!pair.out.items.isEmpty()){
                    for(var output : pair.out.items){
                        if(items.get(output.item) + output.amount > itemCapacity){
                            return false;
                        }
                    }
                }
                if(!pair.out.liquids.isEmpty() && !ignoreLiquidFullness){
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
        public float heatRequirement(){
            float total = 0f;
            for(var pair : recipeView.getPairs()){
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
            for(var pair : recipeView.getPairs()){
                total += pair.out.heat;
            }
            return heat / total;
        }


        @Override
        public float[] sideHeat(){
            return sideHeat;
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
        public float warmup(){
            return warmup;
        }

        @Override
        public void updateTile(){
            for(var recipeDO : recipeView.getCrafts()){
                var pair = recipeDO.pair;
                if(efficiency > 0){
                    if(recipeDO.efficiency > 0){
                        recipeDO.progress += getProgressIncrease(pair, pair.craftTime);
                        warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
                        //continuously output based on efficiency
                        if(pair.out.liquids != null){
                            float inc = getProgressIncrease(1f);
                            for(var output : pair.out.liquids){
                                handleLiquid(this, output.liquid, Math.min(output.amount * inc,
                                liquidCapacity - liquids.get(output.liquid)));
                            }
                        }
                        if(wasVisible && Mathf.chanceDelta(pair.updateEffectChance / recipeView.getPairs().size)){
                            pair.updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size << 2));
                        }
                    }
                }else{
                    warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                }

                if(recipeDO.progress >= 1f){
                    craft(recipeDO);
                }

                dumpRecipeOutput(pair);
            }
        }

        public float getProgressIncrease(RecipePair pair, float baseTime){
            //limit progress increase by maximum amount of liquid it can produce
            float scaling = 1f, max = 1f;
            //Log.info(pairs);
            if(pair.out.liquids != null){
                max = 0f;
                for(var s : pair.out.liquids){
                    float value = (liquidCapacity - liquids.get(s.liquid)) / (s.amount * edelta());
                    scaling = Math.min(scaling, value);
                    max = Math.max(max, value);
                }
            }
            //when dumping excess take the maximum value instead of the minimum.
            return getProgressIncrease(baseTime) * (dumpExtraLiquid ? Math.min(max, 1f) : scaling);
        }

        public void craft(RecipeDO crafts){
            var pair = crafts.pair;
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
            crafts.progress %= 1f;
        }

        public void dumpRecipeOutput(RecipePair recipe){
            if(recipe.out.items != null && timer(timerDump, dumpTime / timeScale)){
                for(ItemStack output : recipe.out.items){
                    dump(output.item);
                }
            }

            if(recipe.out.liquids != null){
                for(int i = 0; i < recipe.out.liquids.size; i++){
                    int dir = recipe.liquidOutputDirections.length > i ?
                    recipe.liquidOutputDirections[i] : -1;
                    dumpLiquid(recipe.out.liquids.get(i).liquid, 2f, dir);
                }
            }
        }

        public void consume(RecipePair pair){
            pair.in.items.each(s -> items.remove(s));
            pair.in.consumers.each(c -> !c.update, c -> c.trigger(this));
        }
        //endregion
    }

    public class RecipeView{

        private Seq<RecipeDO> crafts = new Seq<>();
        private Seq<RecipePair> pairs = new Seq<>();
        private IntSeq numbers = new IntSeq();

        public void setRecipe(int[] indexes){
            pairs.clear();
            for(var index : indexes){
                select(index);
            }
        }

        public void select(int index){
            if(selected(index)){
                throw new IllegalArgumentException("WHat happened?");
            }
            RecipePair pair;
            if(index >= recipes.size || index == -1){
                pair = RecipePair.EMPTY_RECIPE_PAIR;
            }else{
                pair = recipes.get(index);
            }
            crafts.add(new RecipeDO(pair, 0f, 0f));
            pairs.add(pair);
            numbers.addUnique(index);
        }

        public boolean selected(int index){
            return numbers.contains(index);
        }

        public Seq<RecipeDO> getCrafts(){
            return crafts;
        }

        public Seq<RecipePair> getPairs(){
            return pairs;
        }

        public boolean isCraftsEmpty(){
            return crafts.isEmpty();
        }

        public void unselect(int index){
            int address = numbers.indexOf(index);
            numbers.removeIndex(address);
            crafts.remove(address);
            pairs.remove(address);
        }
    }
}
