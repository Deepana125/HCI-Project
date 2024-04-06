package Frames;

import java.awt.Point;

public class Furniture2DItem {
    private String type;
    private Point position;

    public Furniture2DItem(String type, Point position) {
        this.type = type;
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public Point getPosition() {
        return position;
    }
}
