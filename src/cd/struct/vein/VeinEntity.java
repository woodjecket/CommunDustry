package cd.struct.vein;

public class VeinEntity {
    public VeinType type;
    public int currentAbundance = 100;
    public int centerZ = -30;

    @Override
    public String toString() {
        return type.toString() + currentAbundance + super.toString();
    }
}
