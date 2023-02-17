package cd.type.formula;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import cd.type.valence.ItemValence;
import mindustry.gen.Building;

public abstract class Step {
    public abstract void consumeBuilding(Building building);

    public abstract Table displayRequirement();

    public abstract boolean canWork(Building building);

    public abstract ItemValence getValenceFunc(Formula.FormulaEnv env);

    public static Step readFromBase64(String base) {
        return null;
    }

    public abstract String writeToBase64();

    protected static String makeList(Object... obj) {
        return new Seq<>(obj).toString(",");
    }

    protected static String[] readList(String str) {
        return str.split(",");
    }
}
