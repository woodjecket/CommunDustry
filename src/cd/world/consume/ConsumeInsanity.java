package cd.world.consume;

import cd.manager.*;
import mindustry.gen.*;
import mindustry.world.consumers.*;

public class ConsumeInsanity extends Consume{
    public float amount, min;

    public ConsumeInsanity(float min){
        this.min = min;
    }

    @Override
    public float efficiency(Building build){
        return InsanityManager.insanity >= min ? 1f : 0f;
    }
}
