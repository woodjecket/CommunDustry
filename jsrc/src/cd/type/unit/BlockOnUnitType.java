package cd.type.unit;

import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.ammo.*;
import mindustry.world.meta.*;

public class BlockOnUnitType extends UnitType{
    public int size = 4;

    public BlockOnUnitType(String name){
        super(name);
        outlineColor = Pal.darkOutline;
        ammoType = new ItemAmmoType(Items.copper);
        researchCostMultiplier = 10f;
        squareShape = true;
        omniMovement = false;
        rotateMoveFirst = true;
        rotateSpeed = 1.3f;
        envDisabled = Env.none;
        speed = 0.8f;
    }

}
