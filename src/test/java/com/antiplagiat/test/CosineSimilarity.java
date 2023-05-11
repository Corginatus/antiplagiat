package com.antiplagiat.test;

import java.util.*;
import java.util.stream.Collectors;

public class CosineSimilarity {
    public static void main(String[] args) {
        // Define documents
        Map<String, String> docs = new HashMap<>();
        docs.put("doc1", "jupiter is the largest planet");
        docs.put("doc2", "mars is the fourth planet from the sun");

        // Generate TF map for each document
        List<Map<String, Double>> tfDocs = docs.values().stream().map(CosineSimilarity::generateTfMap).collect(Collectors.toList());

        // Generate IDF map for all documents
        Map<String, Double> idf = generateIdfMap(tfDocs);

        // Generate TF-IDF map for each document
        List<Map<String, Double>> tfIdfDocs = tfDocs.stream().map(tfDoc -> generateTfIdfMap(tfDoc, idf)).collect(Collectors.toList());

        // Calculate cosine similarity
        Double cosineSimilarity = calculateCosineSimilarity(tfIdfDocs.get(0), tfIdfDocs.get(1));
        System.out.printf("Cosine similarity: %.4f", cosineSimilarity);
    }

    private static Map<String, Double> generateTfMap(String doc) {
        List<String> words = Arrays.asList(doc.split(" "));
        Map<String, Double> tfMap = new HashMap<>();
        for (String word : words) {
            tfMap.put(word, tfMap.getOrDefault(word, 0.0) + 1.0 / words.size());
        }
        return tfMap;
    }

    private static Map<String, Double> generateIdfMap(List<Map<String, Double>> tfDocs) {
        Map<String, Double> idfMap = new HashMap<>();
        for (Map<String, Double> tfDoc : tfDocs) {
            for (String word : tfDoc.keySet()) {
                idfMap.put(word, idfMap.getOrDefault(word, 0.0) + 1.0);
            }
        }
        for (String word : idfMap.keySet()) {
            idfMap.put(word, Math.log(tfDocs.size() / idfMap.get(word)));
        }
        return idfMap;
    }

    private static Map<String, Double> generateTfIdfMap(Map<String, Double> tfMap, Map<String, Double> idfMap) {
        Map<String, Double> tfIdfMap = new HashMap<>();
        for (String word : tfMap.keySet()) {
            tfIdfMap.put(word, tfMap.get(word) * idfMap.get(word));
        }
        return tfIdfMap;
    }

    private static Double calculateCosineSimilarity(Map<String, Double> tfIdf1, Map<String, Double> tfIdf2) {
        Double dotProduct = 0.0;
        Double magnitude1 = 0.0;
        Double magnitude2 = 0.0;
        for (String word : tfIdf1.keySet()) {
            dotProduct += tfIdf1.get(word) * tfIdf2.getOrDefault(word, 0.0);
            magnitude1 += Math.pow(tfIdf1.get(word), 2);
        }
        for (String word : tfIdf2.keySet()) {
            magnitude2 += Math.pow(tfIdf2.get(word), 2);
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }
}

