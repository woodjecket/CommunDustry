package cd.world.blocks.multi.craft;

import arc.struct.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.meta.*;

import static cd.world.blocks.multi.craft.RecipePair.EMPTY_RECIPE_PAIR;

/** MultiCrafter. Refers from Li plum's MultiCrafterLib, but fix a bug.
 * Support both single or multiple selective and concurrent mode */
public class CDMultiCrafter extends Block{

    public Seq<RecipePair> recipes = new Seq<>();
    public int defaultRecipeIndex;

    public boolean ignoreLiquidFullness;
    public boolean dumpExtraLiquid;
    public Effect craftEffect = Fx.none;
    public int[] fluidOutputDirections = {-1};

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
        config(Integer.class, CDMultiCrafterBuild::setRecipe);
    }

    public class CDMultiCrafterBuild extends Building implements HeatBlock, HeatConsumer{
        public float[] sideHeat = new float[4];
        public float heat;
        public float warmup;
        public RecipePair recipe;

        @Override
        public void placed(){
            super.placed();
            setRecipe(defaultRecipeIndex);
        }

        public void setRecipe(int index){
            recipe = recipes.get(index);
            if(index == 0){
                recipe = EMPTY_RECIPE_PAIR;
            }
            if(recipe == null) throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            var version = read.i();
            if(version > 0){
                var index = read.i();
                setRecipe(index);
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(1);
            write.i(recipes.indexOf(recipe));
        }

        @Override
        public float heat(){
            return heat;
        }

        @Override
        public float heatFrac(){
            return heat / recipe.out.heat;
        }

        @Override
        public float[] sideHeat(){
            return sideHeat;
        }

        @Override
        public float heatRequirement(){
            return recipe.in.heat;
        }

        @Override
        public boolean shouldConsume(){
            if(recipe.out.items != null){
                for(var output : recipe.out.items){
                    if(items.get(output.item) + output.amount > itemCapacity){
                        return false;
                    }
                }
            }

            if(recipe.out.liquids != null && !ignoreLiquidFullness){
                boolean allFull = true;
                for(var output : recipe.out.liquids){
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

            return enabled;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return hasItems &&
            recipe.in.items.contains(s -> s.item == item) &&
            items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return hasLiquids &&
            recipe.in.liquids.contains(s -> s.liquid == liquid) &&
            liquids.get(liquid) < liquidCapacity;
        }

        public void craft() {
            consume();
            if (!recipe.out.items.isEmpty()) {
                for (ItemStack output : recipe.out.items) {
                    for (int i = 0; i < output.amount; i++) {
                        offload(output.item);
                    }
                }
            }

            if (wasVisible) {
                craftEffect.at(x, y);
            }
        }

        public void dumpOutputs() {

            if (!recipe.out.items.isEmpty() && timer(timerDump, dumpTime / timeScale)) {
                for (ItemStack output : recipe.out.items) {
                    dump(output.item);
                }
            }

            if (!recipe.out.liquids.isEmpty()) {
                Seq<LiquidStack> fluids = recipe.out.liquids;
                for (int i = 0; i < fluids.size; i++) {
                    int dir = fluidOutputDirections.length > i ? fluidOutputDirections[i] : -1;

                    dumpLiquid(fluids.get(i).liquid, 2f, dir);
                }
            }
        }
    }
}
