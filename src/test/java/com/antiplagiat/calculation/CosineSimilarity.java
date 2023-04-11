package com.antiplagiat.calculation;
import java.util.*;
public class CosineSimilarity {
    public static double cosineSimilarity(String[] x, String[] y, Map<String, Integer> freqX, Map<String, Integer> freqY) {
        // calculate frequency vectors
        for (String word : x) {
            freqX.put(word, freqX.getOrDefault(word, 0) + 1);
        }
        for (String word : y) {
            freqY.put(word, freqY.getOrDefault(word, 0) + 1);
        }

        // calculate dot product and norms
        double dotProduct = 0.0;
        double normX = 0.0;
        double normY = 0.0;
        for (String word : freqX.keySet()) {
            int freq1 = freqX.get(word);
            int freq2 = freqY.getOrDefault(word, 0);
            dotProduct += freq1 * freq2;
            normX += freq1 * freq1;
        }
        for (int freq : freqY.values()) {
            normY += freq * freq;
        }

        // calculate denominator and cosine similarity
        double denominator = Math.sqrt(normX) * Math.sqrt(normY);
//        System.out.println("denominator: " + denominator);
//        System.out.println("dotProduct: " + dotProduct);
        if ((denominator == 0) || (dotProduct == 0)) {
            return 0.0;
        } else {
            return dotProduct / denominator;
        }
    }

}
