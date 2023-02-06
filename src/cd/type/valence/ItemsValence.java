package cd.type.valence;

import arc.func.*;
import arc.struct.*;
import arc.util.*;
import mindustry.type.*;

import java.util.concurrent.atomic.*;

public final class ItemsValence{
    public static ObjectMap<Item, ValenceMap> map = new ObjectMap<>();

    private ItemsValence(){
    }

    public static boolean hasValence(Item item){
        return map.containsKey(item);
    }

    public static void setValence(Item item, ValenceFunc valence){
        if(hasValence(item)) getFunc(item).normalFunc = valence;
        else{
            map.put(item, new ValenceMap(valence, null));
        }
    }

    public static void setValence(Item item, int valence){
        setValence(item, new ValenceFunc((module) -> valence));
    }

    public static void setHandle(Item item, ValenceFunc handle){
        if(hasValence(item)) getFunc(item).handleFunc = handle;
    }

    public static void set(Item item, ValenceFunc valence, ValenceFunc handle){
        map.put(item, new ValenceMap(valence, handle));
    }

    public static Integer getValence(Item item, ValenceModule module){
        ValenceMap itemValence = map.get(item);
        return itemValence != null ? itemValence.normalFunc.getNormal(module) : 0;
    }

    public static Integer getHandleValence(Item item, ValenceModule module){
        return map.get(item).handleFunc.getNormal(module);
    }

    public static ValenceMap getFunc(Item item){
        return map.get(item);
    }

    public static class ValenceMap{
        public ValenceFunc normalFunc;
        public ValenceFunc handleFunc;

        public ValenceMap(ValenceFunc normalFunc, @Nullable ValenceFunc handleFunc){
            this.normalFunc = normalFunc;
            this.handleFunc = handleFunc;
        }

        public Integer getHandleFunc(ValenceModule module){
            return handleFunc != null ? handleFunc.getNormal(module) : null;
        }

        public Integer getNormalFunc(ValenceModule module){
            return normalFunc.getNormal(module);
        }
    }

    public static class ValenceFunc{
        public Func<ValenceModule, Integer> func;

        public ValenceFunc(Func<ValenceModule, Integer> func){
            this.func = func;
        }

        public Integer get(ValenceModule module){
            return func.get(module);
        }

        public Integer getNormal(ValenceModule module){
            return module.formulaBefore != null ? get(module) : null;
        }
    }

    public static class ValenceModule{
        public Formula formulaBefore;
        public AtomicInteger valence;
        public int itemValence;

        public ValenceModule(AtomicInteger valence, @Nullable Formula formulaBefore, int itemValence){
            this.valence = valence;
            this.formulaBefore = formulaBefore;
            this.itemValence = itemValence;
        }
    }
}
