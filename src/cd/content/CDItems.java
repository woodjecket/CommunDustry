package cd.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class CDItems {
    public static Item
            //T1
            iron, carbonSteel, quartz,
            //T2
            chromium, alloySteel, vonKit,
            //T3
            carbonFiber, specialSteel,
            //T4
            uranium, planckKit, cnt,
            //T5
            antiCryo, higgsKit;
    public static void load(){
        iron = new Item("iron", Color.gray);
        carbonSteel = new Item("carbon-steel",Color.gray);
        quartz = new Item("quartz",Color.white);
        chromium = new Item("chromium", Color.yellow);
        alloySteel = new Item("alloy-steel", Color.gray);
        vonKit = new Item("von-kit",Color.brick);
        carbonFiber = new Item("carbon-fiber", Color.gray);
        specialSteel = new Item("special-steel", Color.crimson);
        uranium = new Item("uranium", Color.green);
        planckKit = new Item("planck-kit", Color.red){{
            frames = 12;
        }};
        cnt = new Item("cnt", Color.orange);
        antiCryo = new Item("anti-cryo", Color.blue);
        higgsKit = new Item("higgs-kit", Color.violet){{
            frames = 12;
        }};
    }
}
