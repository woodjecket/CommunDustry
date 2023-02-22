package cd.entities.building;

import mindustry.gen.*;

/** Laser Building */
public interface ILaserBuilding{
    int getLaserRange();

    Building getLaserChild();

    void setLaserChild(Building b);

    /** Give a change based on difference */
    void changeLaserEnergy(float c);

    boolean isAcceptLaserEnergy();

    /** Return whether this building can provide laser to the position */
    boolean isProvideLaserEnergy(int bx, int by);

    float getLaserEnergy();

    void setLaserEnergy(float energy);
}
