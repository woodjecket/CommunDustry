package cd.manager;

import arc.util.pooling.Pool;
import arc.util.pooling.Pools;
import cd.entities.RecipeSlot;

public class PoolManager {
    public static Pool<RecipeSlot> slotPool = Pools.get(RecipeSlot.class, () -> {
        return new RecipeSlot();
    });
}
