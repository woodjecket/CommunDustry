package cd.type.blocks.laser;

import mindustry.gen.*;

public interface LaserInterface{
    int laserRange();

    void addLaserParent(Building b);

    void removeLaserParent(Building b);

    Building getLaserChild();

    void setLaserChild(Building b);

    void changeLaserEnergy(float c);

    boolean isAcceptLaserEnergy();

    boolean isProvideLaserEnergy(int bx, int by);

    int getLastChange();

    void setLastChange(int t);

    float getLaserEnergy();
}
