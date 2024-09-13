package cd.map.dv.data;

public enum DeepVeinLayer {
    HIGH(-15), MEDIUM(-50), LOW(-200);

    private final int yShift;

    DeepVeinLayer(int yShift) {
        this.yShift = yShift;
    }
}
