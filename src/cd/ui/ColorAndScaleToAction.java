package cd.ui;

import arc.graphics.*;
import arc.scene.actions.*;
import arc.util.*;

public class ColorAndScaleToAction extends ScaleToAction{
    public Color startColor, endColor;

    @Override
    protected void begin(){
        super.begin();
        startColor = target.color;
    }

    @Override
    protected void update(float percent){
        super.update(percent);
        target.setColor(Tmp.c1.set(startColor).lerp(endColor,percent));
    }
}
