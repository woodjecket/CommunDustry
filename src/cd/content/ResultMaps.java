package cd.content;

import cd.type.valence.*;
import mindustry.content.*;

public class ResultMaps{
    public static ResultMap map1;

    public static void load(){
        map1 = new ResultMap(30, true);
        loadResult();
    }

    public static void loadResult(){
        map1.putResult(Items.phaseFabric, new int[]{-8, 3, 1}, new int[]{3, 2, 1});
        map1.putResult(Items.surgeAlloy, new int[]{10, 11, 13});
        map1.putResult(CDItems.platinum, new int[]{24, 25});
    }
}
