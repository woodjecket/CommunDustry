package cd.struct.vein;

import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.noise.Simplex;
import cd.CDConfig;
import mindustry.type.ItemStack;

import java.util.Arrays;


/** Denotes the flyweight of a kind of vein.*/
public class VeinType {
    public static Seq<VeinType> all = new Seq<>();

    public short id;
    public ItemStack[] items;
    public Color color;
    public VeinEntityGenerator generator = new NoiseEntityGenerator();
    
    public VeinType() {
        id = (short) all.size;
        all.add(this);
    }

    @Override
    public String toString() {
        return "{ VeinType: " + id + ",items: " + Arrays.toString(items) + "}";
    }
    
    public interface VeinEntityGenerator{
        VeinEntity get(int x, int y);
    }
    
    public class NoiseEntityGenerator implements VeinEntityGenerator{
        public float threshold = 0.16f;
        public int baseAmount = 5;
        public int baseDepth = CDConfig.defaultDepth;
        public int baseRange = 20;
        public int amountScale = 3;
        public int depthScale = 15;
        public int rangeScale = 3;
        @Override
        public VeinEntity get(int x, int y) {
            float nx = x / 100f;
            float ny = y / 100f;
            float noise1 = Simplex.noise2d(id + 3900, 3, 2, 1, nx, ny);
            float noise2 = Simplex.noise2d(id + 7800, 3, 2, 1, nx, ny);
            float noise3 = Simplex.noise2d(id + 11700, 3, 2, 1, nx, ny);
            float noise4 = Simplex.noise2d(id + 15600, 3, 2, 1, nx, ny);
            if (noise1 < threshold) {
                var entity = new VeinEntity();
                entity.type = VeinType.this;
                entity.amount = (int) (baseAmount + (noise2 - 0.5) * amountScale);
                entity.depth = (int) (baseDepth + (noise3 - 0.5) * depthScale);
                entity.range = (int) (baseRange + (noise4 - 0.5) * rangeScale);
                return entity;
            }
            return null;
        }
    }
    
}
