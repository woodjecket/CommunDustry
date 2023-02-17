package cd.world.blocks.valence;

import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import cd.type.valence.*;
import cd.ui.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import static cd.type.valence.ItemsValence.ValenceModule;

//TODO 完善
public class ValenceCrafter extends GenericCrafter implements ValenceBlock{
    public ResultMap resultMap;

    public ValenceCrafter(String name, ResultMap resultMap){
        super(name);
        this.resultMap = resultMap;
        this.buildType = ValenceCrafterBuild::new;
        configurable = true;
        consume(new ConsumeItemValence());
        config(Integer.class, (build, index) -> {
            ValenceCrafterBuild vBuild = (ValenceCrafterBuild)build;
            switch(index){
                case 0 -> vBuild.formula.subFormulaItem();
                case 1 -> vBuild.updateDialog();
                case 2 -> {
                    vBuild.formula = vBuild.dialog.formula;
                    vBuild.updateDialog();
                }
            }
        });
        config(Item.class, (build, item) -> ((ValenceCrafterBuild)build).formula.putFormulaItem(item));
        config(Float.class, ValenceCrafterBuild::setHeat);
        configClear(build -> ((ValenceCrafterBuild)build).formula.clear());
    }

    @Override
    public ResultMap getMap(){
        return resultMap;
    }

    public static class ConsumeItemValence extends ConsumeItemFilter{
        public ConsumeItemValence(){
            this.filter = ItemsValence::hasValence;
        }

        @Override
        public void trigger(Building build){
            ValenceBuild vBuild = (ValenceBuild)build;
            vBuild.getFormula().toStack().each(i -> build.items.remove(i));
            vBuild.getFormula().clear();
        }
    }

    public class ValenceCrafterBuild extends GenericCrafterBuild implements ValenceBuild{
        public ItemStack output;
        public float heat = 0;
        public Formula formula = new Formula();
        public ValenceMapDialog dialog = new ValenceMapDialog(this);
        public ValenceModule module = new ValenceModule(getFormula().valenceAtomicInteger, null, 0);

        @Override
        public float getProgressIncrease(float baseTime){
            return super.getProgressIncrease(baseTime) * heat;
        }

        @Override
        public void buildConfiguration(Table table){
            table.button(Tex.button, () -> dialog.show());
        }

        @Override
        public float getHeat(){
            return heat;
        }

        @Override
        public void setHeat(float amount){
            heat = amount;
        }

        @Override
        public Formula getFormula(){
            return formula;
        }

        @Override
        public ValenceModule getModule(){
            return module;
        }

        @Override
        public ResultMap getMap(){
            return resultMap;
        }

        @Override
        public int getValence(){
            return getFormula().getValence();
        }

        @Override
        public void updateDialog(){
            if(!dialog.isShown()) return;
            dialog.formula = formula;
            dialog.allValence = formula.getValence();
            dialog.updateFormula();
            dialog.updateItem();
            Log.info(dialog.allValence);
        }

        @Override
        public void updateValence(){
            output = resultMap.getResult(formula.getValence());
            updateDialog();
        }

        @Override
        public void dumpOutputs(){
            if(output != null) dump(output.item);
        }

        @Override
        public void craft(){
            heat = 0;
            updateValence();
            if(output != null){
                for(int i = 0; i < output.amount; i++) offload(output.item);
                if(wasVisible){
                    craftEffect.at(x, y);
                }
            }
            progress %= 1f;
            consume();
            updateDialog();
        }

        @Override
        public Object config(){
            return getFormula();
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.i(formula.items.size);
            write.b(formula.toBytes());
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            int length = read.i();
            formula = Formula.readFromBytes(read.b(length));
        }
    }
}


