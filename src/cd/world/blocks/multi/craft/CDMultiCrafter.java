package cd.world.blocks.multi.craft;

import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.meta.*;

/**MultiCrafter. Refers from Li plum's MultiCrafterLib, but fix lots of bugs*/
public class CDMultiCrafter extends Block{

    public Seq<RecipePair> recipes;
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
        config(Integer.class, CDMultiCrafterBuild::setCurRecipeIndexFromRemote);
    }

    public class CDMultiCrafterBuild extends Building implements HeatBlock, HeatConsumer{
        public float[] sideHeat = new float[4];
        public float heat;
        public float warmup;

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

        public static <E extends Building, T> void setCurRecipeIndexFromRemote(E e, T t){
        }

        @Override
        public float heat(){
            return heat;
        }

        @Override
        public float heatFrac(){
            return 0;
        }

        @Override
        public float[] sideHeat(){
            return sideHeat;
        }

        @Override
        public float heatRequirement(){
            return 0;
        }
    }
}
