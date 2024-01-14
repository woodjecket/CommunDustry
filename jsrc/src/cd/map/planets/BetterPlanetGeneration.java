package cd.map.planets;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import cd.map.planets.GenericPlanetGenerator.*;
import mindustry.maps.generators.*;

@Deprecated
public class BetterPlanetGeneration extends PlanetGenerator{

    @Override
    public float getHeight(Vec3 position){
        //TODO complete
        return 0;
    }

    @Override
    public Color getColor(Vec3 position){
        //TODO complete
        return null;
    }

    public void genBasedOnRooms(){
        float constraint = 1.3f;
        float radius = width / 2f / Mathf.sqrt3;
        int rooms = rand.random(2, 5);
        Seq<Room> roomseq = new Seq<>();

        for(int i = 0; i < rooms; i++){
            //Choose a point
            Tmp.v1.trns(rand.random(360f), rand.random(radius / constraint));

            float rx = (width / 2f + Tmp.v1.x);
            float ry = (height / 2f + Tmp.v1.y);
            float maxrad = radius - Tmp.v1.len();
            float rrad = Math.min(rand.random(9f, maxrad / 2f), 30f);
            //roomseq.add(new Room((int)rx, (int)ry, (int)rrad));
        }
    }

    @Override
    public void generate(){
        cells(6);
        distort(10f, 12f);
        genBasedOnRooms();
    }
}
