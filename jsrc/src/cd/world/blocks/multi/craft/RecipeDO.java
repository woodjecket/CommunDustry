package cd.world.blocks.multi.craft;

/*Represent a data object containing recipe pair, craft time and progress*/
public class RecipeDO{
    public RecipePair pair;
    public float efficiency;
    public float progress;
    public boolean enabled;

    public RecipeDO(RecipePair pair, float efficiency, float progress){
        this.pair = pair;
        this.efficiency = efficiency;
        this.progress = progress;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        RecipeDO recipeDO = (RecipeDO)obj;

        if(Float.compare(recipeDO.efficiency, efficiency) != 0) return false;
        if(Float.compare(recipeDO.progress, progress) != 0) return false;
        return pair.equals(recipeDO.pair);
    }

    @Override
    public int hashCode(){
        int result = pair.hashCode();
        result = 7 * result + (efficiency == 0.0f ? 0 : Float.floatToIntBits(efficiency));
        result = 10 * result + (progress == 0.0f ? 0 : Float.floatToIntBits(progress));
        return result;
    }

    @Override
    public String toString(){
        return "RecipeDO{" +
        "pair=" + pair +
        ", craftTime=" + efficiency +
        ", progress=" + progress +
        ", enabled=" + enabled +
        '}';
    }

}
