package mods.redfire.simplemachinery.util;

public class CoordinateCheck {
    public static boolean within(int coordinate, int minimum, int maximum) {
        return minimum < coordinate && coordinate < maximum;
    }

    public static boolean withinRectangle(int x, int y, int minX, int minY, int maxX, int maxY) {
        return within(x, minX, maxX) && within(y, minY, maxY);
    }
}
