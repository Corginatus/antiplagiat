package com.antiplagiat.calculation;

public class SaatyMethod {

    public static double startSaaty(double f1, double cosineSimilarity) {
        System.out.println("f1=" + f1 + "; cosineSimilarity=" + cosineSimilarity);
        double a1 = 0.5;
        double a2 = 0.5;
        if (f1 > 0.999 && cosineSimilarity > 0.999){
            return 0;
        } else return a1 * (1-f1) + a2 * (1-cosineSimilarity);

    }

}

