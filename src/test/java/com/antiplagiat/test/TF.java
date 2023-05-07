package com.antiplagiat.test;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.antiplagiat.calculation.KeywordExtractor;
import org.tartarus.snowball.ext.russianStemmer;

import java.util.*;
import java.util.Arrays;

public class TF {

    public static void main(String[] args){
//        String st = "Быстрая бурая лиса перепрыгивает через ленивую собаку, а быстрый, который поезд" +
//                " проехал ленивое авто. Когда лиса прыгнула на поезд, который впрочем и не поезд, то упала. Конец.";
//        String st2 = "Быстрая коричневая собака перепрыгивает через ленивую лису, которая упала. А собака не падала";


        File file = new File("C:\\Users\\37529\\antiplagiat\\src\\test\\java\\files\\smoke.txt");
        String st = null;
        try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            st = new String(chars);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("C:\\Users\\37529\\antiplagiat\\src\\test\\java\\files\\smoke2.txt");
        String st2 = null;
        try (FileReader fr = new FileReader(file2)) {
            char[] chars = new char[(int) file2.length()];
            fr.read(chars);

            st2 = new String(chars);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(st);
//        System.out.println(st2);

        st = st.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "").toLowerCase();
        st2 = st2.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "").toLowerCase();

//        russianStemmer stemmer = new russianStemmer();
//        stemmer.setCurrent(st);
//        stemmer.stem();
//        st = stemmer.getCurrent();
//
//        stemmer.setCurrent(st2);
//        stemmer.stem();
//        st2 = stemmer.getCurrent();


        russianStemmer stemmer = new russianStemmer();
        StringBuilder processedText = new StringBuilder();
        for (String word : st.split("\\s+")) {
            stemmer.setCurrent(word);
            stemmer.stem();
            processedText.append(stemmer.getCurrent());
            processedText.append(" ");
        }
        st = processedText.toString().trim();

        StringBuilder processed2Text = new StringBuilder();
        for (String word : st2.split("\\s+")) {
            stemmer.setCurrent(word);
            stemmer.stem();
            processed2Text.append(stemmer.getCurrent());
            processed2Text.append(" ");
        }
        st2 = processed2Text.toString().trim();

        System.out.println(st);
        System.out.println(st2);

        Map<String, Double> resTf = computeTF(st);
        Map<String, Double> res2Tf = computeTF(st2);
        System.out.println(resTf);

//        for (Map.Entry<String, Double> entry : resTf.entrySet()) {
//            System.out.printf("%-10s %f%n", entry.getKey(), entry.getValue());
//        }
        System.out.println(res2Tf);

        List<String> docs = Arrays.asList(st, st2);
        Map<String, Double> idfMap = calculateIdf(docs);

        for (Map.Entry<String, Double> entry : idfMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Вычисляем TF-IDF для каждого документа
        List<Map<String, Double>> tfIdfDocs = new ArrayList<>();
        for (String doc : docs) {
            if (!doc.isEmpty()) {
                Map<String, Double> tfMap = computeTF(doc);
                Map<String, Double> tfIdfMap = new HashMap<>();
                for (String word : tfMap.keySet()) {
                    double tfIdf = tfMap.get(word) * (idfMap.containsKey(word) ? idfMap.get(word) : 0);
                    tfIdfMap.put(word, tfIdf);
                }
                tfIdfDocs.add(tfIdfMap);
            }
        }

        // Выводим результаты
        for (int i = 0; i < tfIdfDocs.size(); i++) {
            System.out.println("TF-IDF для документа " + (i+1) + ": " + tfIdfDocs.get(i));
        }


        String[] x = st.split(" ");
        String[] y = st2.split(" ");
        double cosineSim = cosineSimilarity(x, y, tfIdfDocs.get(0), tfIdfDocs.get(1));
        System.out.println("Cosine Similarity: " + cosineSim);

        double[] t1 = {0.477, 0.181};
        double[] t2 = {0.651, 0.4};

        double cosineSimilarity = cosineSimilarity(t1, t2);
        System.out.println("Cosine similarity: " + cosineSimilarity);



    }

    public static double cosineSimilarity(double[] vector1, double[] vector2) {
        // вычисляем скалярное произведение векторов
        double dotProduct = 0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
        }

        // вычисляем длины векторов
        double norm1 = 0;
        double norm2 = 0;
        for (int i = 0; i < vector1.length; i++) {
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);

        // вычисляем косинус угла между векторами
        double cosineSimilarity = dotProduct / (norm1 * norm2);

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
            double idf = 1 + Math.log((double) docCount / count);
            idfMap.put(word, idf);
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
            tfFreqDict.put(word, (double) tfDict.get(word) / wordsCount);
        }
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

