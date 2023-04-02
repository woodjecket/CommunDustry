package cd.entities.building;

import arc.util.io.*;

public interface ComponentData{
    void read(Reads read, byte revision);

    void write(Writes write);
}
