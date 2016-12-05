package cn.rx.pdf;

import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.*;

public class CustomRenderer extends BarRenderer{
    private Paint[] paints;

    public CustomRenderer(String[] colors) {
        paints = new Paint[colors.length];
        for (int i = 0; i < colors.length; i++) {
            paints[i] = Color.decode(colors[i]);
        }
    }

    public CustomRenderer(Color[] colors) {
        paints = new Paint[colors.length];
        for (int i = 0; i < colors.length; i++) {
            paints[i] = colors[i];
        }
    }

    //每根柱子以初始化的颜色不断轮循
    public Paint getItemPaint(int i, int j) {
        return paints[j % paints.length];
    }
}
