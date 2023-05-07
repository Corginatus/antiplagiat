package com.antiplagiat;

import com.antiplagiat.calculation.KeywordExtractor;
import com.antiplagiat.calculation.SaatyMethod;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.antiplagiat.calculation.AverageComplexSentences.calculateAverageComplexity;
import static com.antiplagiat.calculation.AverageSentenceLength.calculateAverageSentenceLength;
import static com.antiplagiat.calculation.CosineSimilarity.cosineSimilarity;


import static com.antiplagiat.calculation.SaatyMethod.startSaaty;

@SpringBootApplication
public class Test {
    public static void main(String[] args) {
        System.out.println("---TF-IDF---");
        File file = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files\\smoke.txt");

        Set<String> keywords = null;
        String text = null;
        try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            text = new String(chars);
            keywords = KeywordExtractor.extractKeywords(text);
            System.out.println("Keywords: " + keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files\\smoke2.txt");

        Set<String> keywords2 = null;
        String text2 = null;
        try (FileReader fr = new FileReader(file2)) {
            char[] chars = new char[(int) file2.length()];
            fr.read(chars);

            text2 = new String(chars);
//            System.out.println(text2);

            keywords2 = KeywordExtractor.extractKeywords(text2);
            System.out.println("Keywords: " + keywords2);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String text = "Быстрая бурая лиса перепрыгивает через ленивую собаку, а быстрый, который поезд" +
//                " проехал ленивое авто. Когда лиса прыгнула на поезд, который впрочем и не поезд, то упала. Конец.";
//        String text2 = "Быстрая коричневая собака перепрыгивает через ленивую лису, которая упала. А собака не падала";

        System.out.println("---cosineSimilarity---");

//        String[] x = keywords.toArray(new String[0]);
//        String[] y = keywords2.toArray(new String[0]);
//        Map<String, Integer> freqX = new HashMap<>();
//        Map<String, Integer> freqY = new HashMap<>();
        double f1 = 0.21301822767381443;
        System.out.println("F1 = " + f1);

        System.out.println("---calculateAverageSentenceLength---");

        double avgSentenceLengthText1 = calculateAverageSentenceLength(text);
        System.out.println("text 1: " + avgSentenceLengthText1);
        double avgSentenceLengthText2 = calculateAverageSentenceLength(text2);
        System.out.println("text 2: " + calculateAverageSentenceLength(text2));


        System.out.println("---calculateAverageComplexity---");

        double complexSentencesPercentText1 = calculateAverageComplexity(text);
        double complexSentencesPercentText2 = calculateAverageComplexity(text2);

        System.out.println("text 1: " + calculateAverageComplexity(text));
        System.out.println("text 2: " + calculateAverageComplexity(text2));

        System.out.println("---SaatyMethod---");

        startSaaty(f1, avgSentenceLengthText1, avgSentenceLengthText2,
                complexSentencesPercentText1, complexSentencesPercentText2);


    }




}

