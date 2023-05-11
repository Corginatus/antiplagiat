package com.antiplagiat.calculation;

public class SaatyMethod {

    public static double startSaaty(double f1, double cosineSimilarity) {
        double a1 = 0.45;
        double a2 = 0.55;
        if (f1 > 0.999 && cosineSimilarity > 0.999){
            return 1;
        } else return a1 * (1-f1) + a2 * (1-cosineSimilarity);

    }
}
