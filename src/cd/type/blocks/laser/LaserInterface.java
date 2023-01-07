package cd.type.blocks.laser;

import mindustry.gen.*;

public interface LaserInterface{
    int laserRange();

    void addLaserParent(Building b);

    void removeLaserParent(Building b);

    void changeLaserEnergy(float c);

    boolean isAbsorbLaserEnergy(int bx, int by);


    Building getLaserChild();

    void setLaserChild(Building b);

    boolean canProvide(int bx, int by);
}
