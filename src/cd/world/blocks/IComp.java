package cd.world.blocks;

import arc.func.*;
import arc.struct.*;
import arc.util.*;
import cd.world.component.*;

/**
 * Defines a interface that need class to defines component. Defines some operates for components.
 * The method named {@code components} must be implemented
 */
@SuppressWarnings("unchecked")
public interface IComp{
    ObjectMap<Class<? extends BaseComponent>, BaseComponent> components();

    /** Return the component related to the type param */
    @Nullable
    default <C extends BaseComponent> C getComp(Class<C> type){
        return (C)components().getNull(type);
    }

    /** Add components */
    default void addComp(BaseComponent... c){
        for(var sc : c){
            var type = sc.getClass();
            if(type.isAnonymousClass()){
                type = (Class<? extends BaseComponent>)type.getSuperclass();
            }
            components().put(type, sc);
        }
    }

    /** Return a iterator for components to apply or calculate */
    default Iterable<BaseComponent> listComps(){
        return components().values();
    }

    /** Apply a consumer-lambda to all components */
    default void executeAllComps(Cons<? super BaseComponent> operator){
        for(var i : listComps()){
            operator.get(i);
        }
    }
}
