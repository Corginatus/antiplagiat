package com.antiplagiat.calculation;

import java.util.*;
import java.lang.*;


public class TfIdfExample {

    public static void tfIdf(String[] docs) throws Exception {
        // Загрузка документов
//        String[] docs = { "The quick brown fox jumps over the lazy dog",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
//                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua" };



        // Разбиение документов на токены
        List<List<String>> docsTokens = new ArrayList<>();
        for (String doc : docs) {
            List<String> tokens = new ArrayList<>();
            for (String token : doc.toLowerCase().split("\\W+")) {
                if (token.length() > 0) {
                    tokens.add(token);
                }
            }
            docsTokens.add(tokens);
        }

        // Создание словаря слов
        Set<String> vocabulary = new HashSet<>();
        for (List<String> tokens : docsTokens) {
            vocabulary.addAll(tokens);
        }
        List<String> vocabularyList = new ArrayList<>(vocabulary);
        Collections.sort(vocabularyList);

        // Создание матрицы терм-документов
        int[][] termDocMatrix = new int[vocabularyList.size()][docsTokens.size()];
        for (int j = 0; j < docsTokens.size(); j++) {
            List<String> tokens = docsTokens.get(j);
            for (String token : tokens) {
                int i = Collections.binarySearch(vocabularyList, token);
                if (i >= 0) {
                    termDocMatrix[i][j]++;
                }
            }
        }

        // Вычисление TF-IDF
        int numDocs = docsTokens.size();
        double[] idf = new double[vocabularyList.size()];
        for (int i = 0; i < vocabularyList.size(); i++) {
            int df = 0;
            for (int j = 0; j < numDocs; j++) {
                if (termDocMatrix[i][j] > 0) {
                    df++;
                }
            }
            idf[i] = Math.log((double) numDocs / df);
        }
        double[][] tfIdfMatrix = new double[vocabularyList.size()][numDocs];
        for (int j = 0; j < numDocs; j++) {
            double docLength = 0.0;
            for (int i = 0; i < vocabularyList.size(); i++) {
                docLength += termDocMatrix[i][j];
            }
            for (int i = 0; i < vocabularyList.size(); i++) {
                double tf = (double) termDocMatrix[i][j] / docLength;
                tfIdfMatrix[i][j] = tf * idf[i];
            }
        }

        // Вывод результатов
        for (int j = 0; j < numDocs; j++) {
            System.out.println("Document " + (j+1) + ":");
            for (int i = 0; i < vocabularyList.size(); i++) {
                if (termDocMatrix[i][j] > 0) {
                    System.out.printf("  %s: %f\n", vocabularyList.get(i), tfIdfMatrix[i][j]);
                }
            }
        }

    }
}

