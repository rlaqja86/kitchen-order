package cloudkitchen.util;

public class RandomUtils {
    public static int generate(int min, int max) {
        return  (int)(Math.random() * (max - min + 1) + min);
    }
}
