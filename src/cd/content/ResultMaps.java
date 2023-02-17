package cd.content;

import cd.type.valence.*;
import mindustry.content.*;

public class ResultMaps{
    public static ResultMap map1;
    public static ResultMap2D map2;

    public static void load(){
        map1 = new ResultMap(30, true);
        map2 = new ResultMap2D(30);
        loadResult();
    }

    public static void loadResult() {
        map1.putResult(Items.phaseFabric, new int[]{-8, 3, 1}, new int[]{3, 2, 1});
        map1.putResult(Items.surgeAlloy, new int[]{10, 11, 13});
        map1.putResult(CDItems.platinum, new int[]{24, 25});
        map2.putResult(new Result(Items.surgeAlloy, 1), 1, 1);
        map2.putResult(new Result(Items.beryllium, 1), 2.1f, 2.1f);
        map2.putResult(new Result(Items.coal, 1), 3.2f, 3.2f);
    }
}
