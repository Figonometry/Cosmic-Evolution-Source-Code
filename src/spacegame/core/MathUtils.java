package spacegame.core;

public abstract class MathUtils {

    public static double distance3D(double a1, double b1, double c1, double a2, double b2, double c2){
        double a = a1 - a2;
        double b = b1 - b2;
        double c = c1 - c2;
        return Math.sqrt(a * a + b * b + c * c);
    }

    public static double distance2D(double a1, double b1, double a2, double b2) {
        double a = a1 - a2;
        double b = b1 - b2;
        return Math.sqrt(a * a + b * b);
    }

    public static int floorDouble(double a){
        return (int)Math.floor(a);
    }

}
