package cd.entities.building;

import arc.struct.*;
import arc.util.io.*;

@SuppressWarnings("unchecked")
public interface ICompEntity{
    ObjectMap<Class<? extends ComponentData>,ComponentData> data();

    default <T extends ComponentData> void addData(T data){
        Class<? extends ComponentData> clazz = data.getClass();
        if(clazz.isAnonymousClass()){
            clazz = (Class<? extends ComponentData>)clazz.getSuperclass();
        }
        data().put(clazz,data);
    }

    default <T extends ComponentData> T getData(Class<T> clazz){
        return (T)data().get(clazz);
    }

    default void eachRead(Reads read, byte revision){
        for(var data: data().values()){
            data.read(read,revision);
        }
    }
    default void eachWrite(Writes write){
        for(var data: data().values()){
            data.write(write);
        }
    }
}