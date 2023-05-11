package com.antiplagiat;

import com.antiplagiat.calculation.KeywordExtractor;
import com.antiplagiat.calculation.SaatyMethod;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.tartarus.snowball.ext.russianStemmer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.antiplagiat.calculation.AverageComplexSentences.calculateAverageComplexity;
import static com.antiplagiat.calculation.AverageSentenceLength.calculateAverageSentenceLength;


import static com.antiplagiat.calculation.CosineSimilarity.*;
import static com.antiplagiat.calculation.SaatyMethod.startSaaty;

@SpringBootApplication
public class Test {
    public static void main(String[] args) {
        System.out.println("---TF-IDF---");
        File file = new File("C:\\Users\\37529\\antiplagiat\\src\\test\\java\\files\\gogol_noch2.txt");
        String text = null;
        try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            text = new String(chars);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("C:\\Users\\37529\\antiplagiat\\src\\test\\java\\files\\gugo.txt");
        String text2 = null;
        try (FileReader fr = new FileReader(file2)) {
            char[] chars = new char[(int) file2.length()];
            fr.read(chars);

            text2 = new String(chars);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String st = text.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "").toLowerCase();
        String st2 = text2.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "").toLowerCase();

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

//        System.out.println("---cosineSimilarity---");

        Map<String, Double> resTf = computeTF(st);
        Map<String, Double> res2Tf = computeTF(st2);
//        System.out.println(resTf);
//        System.out.println(res2Tf);


        List<String> docs = Arrays.asList(st, st2);
        Map<String, Double> idfMap = calculateIdf(docs);

//        for (Map.Entry<String, Double> entry : idfMap.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }

        // Вычисляем TF-IDF для каждого документа
        List<Map<String, Double>> tfIdfDocs = new ArrayList<>();
        for (String doc : docs) {
            if (!doc.isEmpty()) {
                Map<String, Double> tfMap = computeTF(doc);
                Map<String, Double> tfIdfMap = new HashMap<>();
                for (String word : tfMap.keySet()) {
                    double tfIdf = tfMap.get(word) * (idfMap.containsKey(word) ? idfMap.get(word) : 0);
//                    System.out.println(tfMap.get(word) + " * " + ((idfMap.containsKey(word) ? idfMap.get(word) : 0) + " = " + tfIdf));
                    tfIdfMap.put(word, tfIdf);
                }
                tfIdfDocs.add(tfIdfMap);
            }
        }

        // Выводим результаты
        for (int i = 0; i < tfIdfDocs.size(); i++) {
            System.out.println("TF-IDF для документа " + (i+1) + ": " + tfIdfDocs.get(i));
        }

        Set<String> words = new HashSet<>(tfIdfDocs.get(0).keySet());
        words.addAll(tfIdfDocs.get(1).keySet());

//        System.out.println("Words\tTFIDF1\tTFIDF2");

        List<Double> tfidf1List = new ArrayList<>();
        List<Double> tfidf2List = new ArrayList<>();

        for (String word : words) {
            Double tfidf1 = tfIdfDocs.get(0).getOrDefault(word, 0.0);
            Double tfidf2 = tfIdfDocs.get(1).getOrDefault(word, 0.0);
            tfidf1List.add(tfidf1);
            tfidf2List.add(tfidf2);
//            System.out.printf("%s\t%.8f\t%.8f\n", word, tfidf1, tfidf2);
        }

//        String[] x = st.split(" ");
//        String[] y = st2.split(" ");
//        double f1 = cosineSimilarity(x, y, tfIdfDocs.get(0), tfIdfDocs.get(1));
//        System.out.println("Cosine Similarity: " + f1);

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < words.size(); i++) {
            dotProduct += tfidf1List.get(i) * tfidf2List.get(i);
            norm1 += tfidf1List.get(i) * tfidf1List.get(i);
            norm2 += tfidf2List.get(i) * tfidf2List.get(i);
        }

        double f1 = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        System.out.println(dotProduct + " / " + (Math.sqrt(norm1) * Math.sqrt(norm2)) + " = " + f1);
        System.out.println("Cosine similarity: " + f1);

        Map<String, Double> resTfNull = new HashMap<>();
        Map<String, Double> res2TfNull = new HashMap<>();

//        System.out.printf("%-15s%-10s%-10s\n", "", "resTfnull", "res2Tfnull");
        for (String word : idfMap.keySet()) {
            Double idfValue = idfMap.get(word);
            Double tfValue1 = resTf.getOrDefault(word, 0.0);
            Double tfValue2 = res2Tf.getOrDefault(word, 0.0);
            if (idfValue == 0.0) {
                resTfNull.put(word, tfValue1);
                res2TfNull.put(word, tfValue2);
            }
//            System.out.printf("%-15s%-10f%-10f\n", word, resTfNull.get(word), res2TfNull.get(word));
        }

//        System.out.printf("%-15s%-10s%-10s\n", "", "resTfnull", "res2Tfnull");
        for (String word : idfMap.keySet()) {
            Double idfValue = idfMap.get(word);
            if (idfValue == 0.0) {
                Double tfValue1 = resTf.getOrDefault(word, 0.0);
                Double tfValue2 = res2Tf.getOrDefault(word, 0.0);
                resTfNull.put(word, tfValue1);
                res2TfNull.put(word, tfValue2);
//                System.out.printf("%-15s%-10f%-10f\n", word, resTfNull.get(word), res2TfNull.get(word));
            }
        }

        double[] vector1 = resTfNull.values().stream().mapToDouble(Double::doubleValue).toArray();
        double[] vector2 = res2TfNull.values().stream().mapToDouble(Double::doubleValue).toArray();

        double cosineSimilaritytest = cosineSimilarity(vector1, vector2);
        System.out.println("cosineSimilarity=" + cosineSimilaritytest);
//        System.out.println("vector1=" + vector1.length);

        System.out.println("---calculateAverageSentenceLength---");

        double avgSentenceLengthText1 = calculateAverageSentenceLength(text);
        System.out.println("text 1: " + avgSentenceLengthText1);
        double avgSentenceLengthText2 = calculateAverageSentenceLength(text2);
        System.out.println("text 2: " + avgSentenceLengthText2);


        System.out.println("---calculateAverageComplexity---");

        double complexSentencesPercentText1 = calculateAverageComplexity(text);
        double complexSentencesPercentText2 = calculateAverageComplexity(text2);

        System.out.println("text 1: " + complexSentencesPercentText1);
        System.out.println("text 2: " + complexSentencesPercentText2);

        double[] t1 = {avgSentenceLengthText1, complexSentencesPercentText1};
        double[] t2 = {avgSentenceLengthText2, complexSentencesPercentText2};

        System.out.println("TESTt1=" + t1[0] + "; " + t1[1]);
        System.out.println("TESTt2=" + t2[0] + "; " + t2[1]);

        double cosineSimilarity = cosineSimilarity(t1, t2);
        System.out.println("Cosine similarity2: " + cosineSimilarity);

        System.out.println("---SaatyMethod---");

        double resultSaaty = startSaaty(f1, cosineSimilarity);
        System.out.println("resultSaaty:" + resultSaaty);

    }




}

