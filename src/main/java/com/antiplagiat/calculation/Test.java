package com.antiplagiat.calculation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public static List<String> fun(String name1, String name2) {
//        gugo_otver_1
//        gogol_noch_1
//        voinaimir_1
//        dostoevskii_besi_1
//        dostoevskii_idiot
//        String result = null;
        System.out.println("---TF-IDF---");
        List<String> result = new ArrayList<String>();
        File file = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files\\" + name1);
        String text = null;
        try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            text = new String(chars);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files\\" + name2);
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

        System.out.println(st);
//        result = result + "\n" + st;
//        System.out.println("---cosineSimilarity---");

//        Map<String, Double> resTf = computeTF(st);
//        Map<String, Double> res2Tf = computeTF(st2);
//        System.out.println(resTf);
//        System.out.println(res2Tf);


        List<String> docs = Arrays.asList(st, st2);
        Map<String, Double> idfMap = calculateIdf(docs);

//        System.out.println("IDF map: ");
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
                    double tfIdf = (tfMap.get(word) * (idfMap.containsKey(word) ? idfMap.get(word) : 0));
//                    System.out.println(tfMap.get(word) + " * " + ((idfMap.containsKey(word) ? idfMap.get(word) : 0) + " = " + tfIdf));
                    tfIdfMap.put(word, tfIdf);
                }
                tfIdfDocs.add(tfIdfMap);
            }
        }

        // Выводим результаты
        for (int i = 0; i < tfIdfDocs.size(); i++) {
            System.out.println("TF-IDF для документа " + (i+1) + ": " + tfIdfDocs.get(i));
//            result = result + "\nTF-IDF для документа " + (i+1) + ": " + tfIdfDocs.get(i);
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
//        System.out.println(dotProduct + " / " + (Math.sqrt(norm1) + " * " + Math.sqrt(norm2)) + " = " + f1);
        System.out.println("Cosine similarity: " + f1);
        result.add("\nCosine similarity: " + f1);

        System.out.println("---calculateAverageSentenceLength---");
        result.add("\n---calculateAverageSentenceLength---");

        double avgSentenceLengthText1 = calculateAverageSentenceLength(text);
        System.out.println("text 1: " + avgSentenceLengthText1);
        result.add("\ntext 1: " + avgSentenceLengthText1);
        double avgSentenceLengthText2 = calculateAverageSentenceLength(text2);
        System.out.println("text 2: " + avgSentenceLengthText2);
        result.add("\ntext 2: " + avgSentenceLengthText2);


        System.out.println("---calculateAverageComplexity---");
        result.add("\n---calculateAverageComplexity---");

        double complexSentencesPercentText1 = calculateAverageComplexity(text);
        double complexSentencesPercentText2 = calculateAverageComplexity(text2);

        System.out.println("text 1: " + complexSentencesPercentText1);
        System.out.println("text 2: " + complexSentencesPercentText2);
        result.add("\ntext 1: " + complexSentencesPercentText1);
        result.add("\ntext 2: " + complexSentencesPercentText2);

        double[] t1 = {avgSentenceLengthText1, complexSentencesPercentText1};
        double[] t2 = {avgSentenceLengthText2, complexSentencesPercentText2};

        System.out.println("TESTt1=" + t1[0] + "; " + t1[1]);
        System.out.println("TESTt2=" + t2[0] + "; " + t2[1]);
        result.add("\nTESTt1=" + t1[0] + "; " + t1[1]);
        result.add("\nTESTt2=" + t2[0] + "; " + t2[1]);

        double cosineSimilarity = cosineSimilarity(t1, t2);
        System.out.println("Cosine similarity2: " + cosineSimilarity);
        result.add("\nCosine similarity2: " + cosineSimilarity);

        System.out.println("---SaatyMethod---");
        result.add("\n---SaatyMethod---");

        double resultSaaty = startSaaty(f1, cosineSimilarity);
        System.out.println("resultSaaty:" + resultSaaty);
        result.add("\nresultSaaty:" + resultSaaty);

        return result;
    }
}

