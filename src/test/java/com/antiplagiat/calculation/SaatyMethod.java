package com.antiplagiat.calculation;

public class SaatyMethod {

    public static double startSaaty(double f1, double avgSentenceLengthText1, double avgSentenceLengthText2,
                                    double complexSentencesPercentText1, double complexSentencesPercentText2) {
        // Calculate comparison matrix
        double[][] comparisonMatrix = new double[3][3];
        comparisonMatrix[0][0] = 1;
        comparisonMatrix[0][1] = getPairwiseComparisonValue(f1, avgSentenceLengthText1 / avgSentenceLengthText2);
        comparisonMatrix[0][2] = getPairwiseComparisonValue(f1, complexSentencesPercentText1 / complexSentencesPercentText2);
        comparisonMatrix[1][0] = comparisonMatrix[0][1] != 0.0 ? 1 / comparisonMatrix[0][1] : 0.0;
        comparisonMatrix[1][1] = 1;
        comparisonMatrix[1][2] = getPairwiseComparisonValue(avgSentenceLengthText1 / avgSentenceLengthText2,
                complexSentencesPercentText1 / complexSentencesPercentText2);
        comparisonMatrix[2][0] = comparisonMatrix[0][2] != 0.0 ? 1 / comparisonMatrix[0][2] : 0.0;
        comparisonMatrix[2][1] = 1 / comparisonMatrix[1][2];
        comparisonMatrix[2][2] = 1;

        System.out.println("MATRIX");
        for (int i = 0; i < comparisonMatrix.length; i++) {
            for (int j = 0; j < comparisonMatrix[0].length; j++) {
                System.out.printf("%f\t", comparisonMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println("---");

        // Calculate criteria weights
        double[] criteriaWeights = calculateCriteriaWeights(comparisonMatrix);

        // Print criteria weights
        System.out.println("Criteria weights:");
        for (int i = 0; i < criteriaWeights.length; i++) {
            System.out.printf("Criterion %d: %.2f\n", i + 1, criteriaWeights[i]);
        }

        System.out.println("---IntegralCriterion---");

        double integralCriterion = criteriaWeights[0] * f1 +
                criteriaWeights[1] * (avgSentenceLengthText1 / avgSentenceLengthText2) +
                criteriaWeights[2] * (complexSentencesPercentText1 / complexSentencesPercentText2);
        System.out.printf("Integral Criterion: %f\n", integralCriterion);

        return integralCriterion;
    }

    private static double getPairwiseComparisonValue(double a, double b) {
        if (a == 0) {
            return 0.0;
        }

        double value = b / a;

        if (value < 1) {
            value = 1 / value;
        }

        return value;
    }

    public static double[] calculateCriteriaWeights(double[][] comparisonMatrix) {
        int n = comparisonMatrix.length;
        double[] weights = new double[n];

        double sumWeights = 0.0;
        for (int i = 0; i < n; i++) {
            double product = 1.0;
            for (int j = 0; j < n; j++) {
                product *= comparisonMatrix[i][j];
            }

            weights[i] = Math.pow(product, 1.0 / n);
            sumWeights += weights[i];
        }

        // Normalize weights
        for (int i = 0; i < n; i++) {
            weights[i] /= sumWeights;
        }

        // Adjust weight of criterion
        weights[0] *= 4;
        weights[1] *= 2;
        weights[2] *= 1;

        return weights;
    }
}
