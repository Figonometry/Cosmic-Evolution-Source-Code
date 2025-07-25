package spacegame.core;

public abstract class MathUtils {
    private static float[] SIN_TABLE = new float[65536];

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

    //Uses an implementation of fast floor
    public static int floorDouble(double a){
        int a1 = (int)a;
        return a < a1 ? a1 - 1 : a1;
    }

    public static int floorFloat(float a){
        int a1 = (int)a;
        return a < a1 ? a1 - 1 : a1;
    }

    public static int floatToIntRGBA(float comp){
        return (int) (comp * 255);
    }

    public static float intToFloatRGBA(int comp){
        return comp / 255f;
    }

    public static short floatToHalf(float value) {
        int floatBits = Float.floatToIntBits(value);
        int sign = (floatBits >> 16) & 0x8000;
        int exponent = ((floatBits >> 23) & 0xFF) - 112;
        int mantissa = floatBits & 0x7FFFFF;

        if (exponent <= 0) {
            return (short) sign;
        } else if (exponent >= 31) {
            return (short) (sign | 0x7FFF);
        } else {
            return (short) (sign | (exponent << 10) | (mantissa >> 13));
        }
    }

    public static float halfToFloat(short half) {
        int sign = (half >> 15) & 0x1;
        int exponent = (half >> 10) & 0x1F;
        int mantissa = half & 0x3FF;

        if (exponent == 0) {
            return sign == 0? 0 : -0.0f;
        } else if (exponent == 31) {
            return sign == 0? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        } else {
            exponent += 112;
            mantissa <<= 13;
            return Float.intBitsToFloat((sign << 31) | (exponent << 23) | mantissa);
        }
    }

    public static double sin(double a) {
        return SIN_TABLE[(int)(a * 10430.378F) & 65535];
    }

    public static double cos(double a) {
        return SIN_TABLE[(int)(a * 10430.378F + 16384.0F) & 65535];
    }

    static {
        for(int i = 0; i < 65536; i++) {
            SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0D / 65536.0D);
        }
    }


}
