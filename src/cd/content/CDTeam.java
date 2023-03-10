package cd.content;

import arc.graphics.*;
import mindustry.game.*;

import java.lang.reflect.*;

public class CDTeam{
    public static Team ancestor;

    public static void load(){
        try{
            var c = Team.class.getDeclaredConstructor(int.class, String.class, Color.class);
            c.setAccessible(true);
            ancestor = c.newInstance(4, "ancestor", Color.valueOf("578BE6"));
        }catch(NoSuchMethodException | IllegalAccessException | InstantiationException |
               InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
}
