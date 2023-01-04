package cd.entities.component;

import mindustry.gen.Building;

public class BaseComponent {

    public float visualExplodePressure, visualMaxOperatePressure, visualMinOperatePressure, leakPointPressure;

    public BaseComponent() {
    }

    public void onUpdateTile(Building b) {
    }

    public void onDestroyed(Building b) {
    }

    public void onCreateExplosion(Building b) {
    }

    public void onShouldConsume(Building b) {
    }

    public void onEfficiencyScale(Building b) {
    }

}
