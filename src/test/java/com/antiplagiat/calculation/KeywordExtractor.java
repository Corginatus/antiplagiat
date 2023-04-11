package com.antiplagiat.calculation;


import java.util.*;

import static com.antiplagiat.calculation.RussianWordEndings.removeRussianWordEnding;

public class KeywordExtractor {
    private static final int NUM_KEYWORDS = 7;
    private static final String[] STOP_WORDS = { "с", "на", "в", "как", "и", "это", "или", "при", "не", "ни", "что",
                                                    "мы", "ты", "бы", "я", "а", "ее", "да", "ли",
                                                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public static Set<String> extractKeywords(String text) {
        Map<String, Double> tfIdf = new HashMap<>();
        Set<String> keywords = new HashSet<>();

        List<String> tokens = tokenize(text);
//        System.out.println(tokens);
        for (String token : tokens) {
            if (isStopWord(token)) {
                continue;
            }

            double tf = tf(tokens, token);
            double idf = idf(tokens, token);
            double tfIdfValue = tf * idf;
//            System.out.println(tf);
//            System.out.println(idf);
//            System.out.println(tfIdfValue);
            tfIdf.put(token, tfIdfValue);
        }

        tfIdf.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(NUM_KEYWORDS)
                .forEach(entry -> {
//                    System.out.print(removeRussianWordEnding(entry.getKey()));
//                    System.out.println();
                    keywords.add(removeRussianWordEnding(entry.getKey()));
                });

//        for (String word : keywords){
//
//        }

        return keywords;
    }

    private static List<String> tokenize(String text) {
//        return Arrays.asList(text.toLowerCase().split("\\W+"));
        text = text.toLowerCase();
        text = text.replaceAll("[^а-яА-Я0-9 ]", "");
//        text = text.replaceAll("[аеёиоуыэюя]", "");
        return Arrays.asList(text.split("\\s+"));
    }

    private static boolean isStopWord(String word) {
        for (String stopWord : STOP_WORDS) {
            if (word.equalsIgnoreCase(stopWord)) {
                return true;
            }
        }
        return false;
    }

    // term frequency
    private static double tf(List<String> tokens, String word) {
        double count = 0.0;
        for (String token : tokens) {
            if (token.equalsIgnoreCase(word)) {
                count++;
            }
        }
        return count / tokens.size();
    }

    // inverse document frequency
    private static double idf(List<String> tokens, String word) {
        double count = 0.0;
        for (String token : tokens) {
            if (token.equalsIgnoreCase(word)) {
                count++;
                break;
            }
        }
        return Math.log(tokens.size() / (count + 1));
    }
}

