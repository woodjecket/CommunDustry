package cd.type.formula;

import arc.scene.ui.layout.Table;
import arc.util.serialization.Base64Coder;
import cd.type.valence.ItemValence;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;

public class ItemStep extends Step {
    public Item requirement;
    public float heat = 0.5f;

    public ItemStep(Item item) {
        requirement = item;
    }

    public ItemStep() {
    }

    @Override
    public void consumeBuilding(Building building) {
        building.items.remove(requirement, 1);
    }

    @Override
    public Table displayRequirement() {
        return new Table(t -> t.image(requirement.uiIcon).grow());
    }

    @Override
    public boolean canWork(Building building) {
        return building.items.has(requirement, 1);
    }

    @Override
    public ItemValence getValenceFunc(Formula.FormulaEnv env) {
        return env.all.itemValenceMap.get(requirement);
    }

    public static ItemStep readFromBase64(String base) {
        ItemStep itemStep = new ItemStep();
        String result = Base64Coder.decodeString(base);
        if (result.startsWith("itemStep ")) {
            result = result.replace("itemStep ", "");
            String[] data = readList(result);
            itemStep.requirement = Vars.content.item(data[0]);
            itemStep.heat = Float.parseFloat(data[1]) / 100f;
        }
        return itemStep;
    }

    @Override
    public String writeToBase64() {
        String result = Base64Coder.encodeString("itemStep ");
        result += Base64Coder.encodeString(makeList(requirement.name, heat * 100));
        return result;
    }

    @Override
    public String toString() {
        return "ItemStep{" +
                "requirement=" + requirement +
                ", heat=" + heat +
                '}';
    }
}
