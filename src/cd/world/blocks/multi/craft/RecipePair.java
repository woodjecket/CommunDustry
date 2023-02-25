package cd.world.blocks.multi.craft;

public final class RecipePair{
    public static final RecipePair EMPTY_RECIPE_PAIR = new RecipePair(Recipe.EMPTY_RECIPE,Recipe.EMPTY_RECIPE);
    public Recipe in, out;

    public RecipePair(Recipe in, Recipe out){
        this.in =in;
        this.out = out;
    }

    public RecipePair(){}

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        RecipePair that = (RecipePair)obj;

        if(!in.equals(that.in)) return false;
        return out.equals(that.out);
    }

    @Override
    public int hashCode(){
        int result = in.hashCode();
        result = 424 * result + out.hashCode();
        return result;
    }

    @Override
    public String toString(){
        return "RecipePair{" +
        "in=" + in +
        ", out=" + out +
        '}';
    }
}
