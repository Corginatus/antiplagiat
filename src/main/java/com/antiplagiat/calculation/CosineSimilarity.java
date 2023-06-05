package com.antiplagiat.calculation;
import java.util.*;
public class CosineSimilarity {
    public static double cosineSimilarity(double[] a, double[] b) {
        double distance = vectorDistance(a, b);
        System.out.println("distance=" + distance);
        double normalizedDistance = 1 / (1 + distance);
        System.out.println("Normalized distance between vectors a and b: " + normalizedDistance);
        if (normalizedDistance == 1.0) return normalizedDistance;
        else return  1 - normalizedDistance;
    }

    public static double vectorDistance(double[] a, double[] b) {
        double dx = a[0] - b[0];
        double dy = a[1] - b[1];
        return Math.sqrt(dx * dx + dy * dy);
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
//            if (w.equalsIgnoreCase("говор")) {
//                count++;
//            }
//        }
//        System.out.println("Слово 'говор' повторилось " + count + " раз(а)");
//        System.out.println("wordsCount: " + wordsCount);
        System.out.println("tfFreqDict = " + tfFreqDict);
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
