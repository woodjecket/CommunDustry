package cd.manager;

import arc.func.Prov;
import arc.util.pooling.Pool;
import cd.entities.RecipeSlot;

public class PoolManager {
    public static Pool<RecipeSlot> slotPool = get(RecipeSlot::new);

    /** The Pools suck*/
    public static <T> Pool<T> get(Prov<T> supplier, int max) {
        return new Pool<T>(4, max) {
            @Override
            protected T newObject() {
                return supplier.get();
            }
        };
    }

    public static <T> Pool<T> get(Prov<T> supplier){
        return get(supplier, 5000);
    }
}
