package cd.manager;

import cd.struct.gal.*;

public class GalManager{
    public static boolean visible;
    public static boolean autoPlay;
    public static int autoPlayMultiplier = 1;

    public static String currentText;
    public static Avtar speakingAvtar;
    public static Avtar frontAvtar;

    public static Plot currentPlot;
    public static Avtar leftAvtar, rightAvtar;

    public static void ensue(){
        currentPlot.ensue();
    }

    public static void startPlot(Plot plot){
        currentPlot = plot;
        ensue();
    }
}
