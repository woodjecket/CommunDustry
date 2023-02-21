package cd.world.blocks.multi;

import mindustry.world.*;

/*The value class for a block and its base rotation. Based on 0(left) to 3(down) counter-clockwise*/
public class RotatedBlock{
    private Block block;
    private int rotation;

    public RotatedBlock(Block block, int rotation){
        this.block = block;
        this.rotation = rotation;
    }

    public RotatedBlock(Block block){
        this.block = block;
    }


    public Block getBlock(){
        return block;
    }

    public int getRotation(){
        return rotation;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        RotatedBlock o1 = (RotatedBlock)obj;
        return rotation == o1.rotation && block.equals(o1.block);
    }

    @Override
    public int hashCode(){
        return block.hashCode() * 114514 + rotation * 1919810;
    }
}
