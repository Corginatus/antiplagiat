package com.antiplagiat.calculation;
import java.util.*;
public class CosineSimilarity {
    public static double cosineSimilarity(double[] vector1, double[] vector2) {

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        for (int i = 0; i < vector1.length; i++) {
//            while (true) {
//                if (vector1[i] > 1 && vector2[i] > 1) {
//                    if (vector1[i] > 1)
//                        vector1[i] = vector1[i] * 0.1;
//                    if (vector2[i] > 1)
//                        vector2[i] = vector2[i] * 0.1;
//                }
//                else {
//                    break;
//                }
//            }
            if (vector1[i] > vector2[i]) {
                vector2[i] = vector1[i] - vector2[i];
                vector1[i] = 1;
            } else {

                vector1[i] = vector2[i] - vector1[i];
                vector2[i] = 1;
            }
            dotProduct += vector1[i] * vector2[i];
            System.out.println("vector1=" + vector1[0] + "; " + "vector2= " + vector2[0]);
            System.out.println("vector1=" + vector1[1] + "; " + "vector2= " + vector2[1]);
            magnitude1 += Math.pow(vector1[i], 2);
            magnitude2 += Math.pow(vector2[i], 2);
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        System.out.println(magnitude1 + "; " + magnitude2);
        double cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        System.out.println("similarity2=" + dotProduct + " / " + (magnitude1 * magnitude2) + " = " + cosineSimilarity);
        return cosineSimilarity;
    }

    public static double cosineSimilarity(String[] x, String[] y, Map<String, Double> tfIdfMapX, Map<String, Double> tfIdfMapY) {
        // calculate frequency vectors weighted by TF-IDF values
        Map<String, Double> freqX = new HashMap<>();
        Map<String, Double> freqY = new HashMap<>();
        for (String word : x) {
            freqX.put(word, freqX.getOrDefault(word, 0.0) + tfIdfMapX.getOrDefault(word, 0.0));
        }
        for (String word : y) {
            freqY.put(word, freqY.getOrDefault(word, 0.0) + tfIdfMapY.getOrDefault(word, 0.0));
        }

        // calculate dot product and norms
        double dotProduct = 0.0;
        double normX = 0.0;
        double normY = 0.0;
        for (String word : freqX.keySet()) {
            double freq1 = freqX.get(word);
            double freq2 = freqY.getOrDefault(word, 0.0);
            dotProduct += freq1 * freq2;
            normX += freq1 * freq1;
        }
        for (double freq : freqY.values()) {
            normY += freq * freq;
        }

        // calculate denominator and cosine similarity
        double denominator = Math.sqrt(normX) * Math.sqrt(normY);
        if ((denominator == 0) || (dotProduct == 0)) {
            return 0.0;
        } else {
            return dotProduct / denominator;
        }
    }
    public static Map<String, Double> calculateIdf(List<String> docs) {
        Map<String, Double> idfMap = new HashMap<>();
        int docCount = docs.size();

        // Создаем множество уникальных слов
        Set<String> uniqueWords = new HashSet<>();
        for (String doc : docs) {
            List<String> words = tokenize(doc);
            words = removeStopWords(words); // удаление стоп-слов
            uniqueWords.addAll(words);
        }

        // Вычисляем IDF для каждого слова
        for (String word : uniqueWords) {
            int count = 0;
            for (String doc : docs) {
                List<String> words = tokenize(doc);
                words = removeStopWords(words); // удаление стоп-слов
                if (words.contains(word)) {
                    count++;
                }
            }
            double idf = Math.log((double) docCount / count);
            idfMap.put(word, idf+1);
        }

        return idfMap;
    }


    public static Map<String, Double> computeTF(String text) {
        // Разделяем текст на отдельные слова
        String[] words = text.split("\\s+");
        // Инициализируем словарь, который будет хранить TF каждого слова
        Map<String, Integer> tfDict = new HashMap<>();
        // Считаем TF каждого слова в тексте
        for (String word : words) {
            // Пропускаем стоп-слова для русского языка
            if (isStopWord(word)) {
                continue;
            }
            // Удаляем окончания слов и приводим к нормальной форме
            word = normalizeWord(word);
            // Обновляем счетчик слов в словаре
            tfDict.put(word, tfDict.getOrDefault(word, 0) + 1);
        }
        // Вычисляем относительную частоту каждого слова (TF) в тексте
        Map<String, Double> tfFreqDict = new HashMap<>();
        int wordsCount = words.length;
        for (String word : tfDict.keySet()) {
            tfFreqDict.put(word, ((double) tfDict.get(word) / wordsCount)*100);
//            if (word.equalsIgnoreCase("медикаментозн")) {
//                System.out.println("CHECK: " + tfDict.get(word) + " / " + wordsCount);
//            }
        }
//        int count = 0;
//
//        for (String w : words) {
//            if (w.equalsIgnoreCase("табачн")) {
//                count++;
//            }
//        }
//        System.out.println("Слово 'табачн' повторилось " + count + " раз(а)");
//        System.out.println("wordsCount: " + wordsCount);
        return tfFreqDict;
    }


    private static boolean isStopWord(String word) {
        // Создаем список стоп-слов для русского языка
        String[] stopWords = {"и", "в", "во", "не", "что", "он", "на", "я", "с", "со", "как", "а", "то", "все", "она",
                "так", "его", "но", "да", "ты", "к", "у", "же", "вы", "за", "бы", "по", "только", "ее", "мне", "было",
                "вот", "от", "меня", "еще", "нет", "о", "из", "ему", "теперь", "когда", "даже", "ну", "вдруг", "ли",
                "если", "уже", "или", "ни", "быть", "был", "него", "до", "вас", "нибудь", "опять", "уж", "вам", "ведь",
                "там", "потом", "себя", "ничего", "ей", "может", "они", "тут", "где", "есть", "надо", "ней", "для",
                "мы", "тебя", "их", "чем", "была", "сам", "чтоб", "без", "будто", "чего"};
        return Arrays.asList(stopWords).contains(word);
    }

    public static String normalizeWord(String word) {
        // Удаляем знаки пунктуации
        word = word.replaceAll("[^a-zA-Zа-яА-Я0-9]", "");

        // Приводим слово к нижнему регистру
        word = word.toLowerCase();

        return word;
    }

    public static List<String> tokenize(String text) {
//        text = text.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "").toLowerCase();
        String[] words = text.split("\\s+");
        return Arrays.asList(words);
    }

    public static List<String> removeStopWords(List<String> words) {
        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            if (!isStopWord(word)) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

}
