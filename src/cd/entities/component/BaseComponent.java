package cd.entities.component;

import mindustry.gen.Building;
import mindustry.world.Block;

public class BaseComponent {

    public float visualExplodePressure, maxOperatePressure, minOperatePressure, leakPointPressure;

    public BaseComponent() {
    }

    public void onUpdateTile(Building b) {
    }

    public void onDestroyed(Building b) {
    }

    public void onCreateExplosion(Building b) {
    }

    public void onCraft(Building b) {
    }

    public boolean onShouldConsume(Building b) { return true;
    }

    public float onEfficiencyScale(Building b) { return 1f;
    }

    public void onInit(Block b) {
    }

    public void onSetStats(Block b) {
    }
    
    public void onSetBar(Block b) {

    }
}
