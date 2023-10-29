package cd.world.blocks;

import arc.util.io.*;

public abstract class ComponentData{
    public abstract void read(Reads read, byte revision);

    public abstract void write(Writes write);
}
