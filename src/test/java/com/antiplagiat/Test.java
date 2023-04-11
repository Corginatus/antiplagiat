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
        File file = new File("C:\\Users\\37529\\antiplagiat\\src\\test\\java\\files\\text_3.txt");

        Set<String> keywords = null;
        String text = null;
        try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);

            text = new String(chars);
//            System.out.println(text);

            keywords = KeywordExtractor.extractKeywords(text);
            System.out.println("Keywords: " + keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file2 = new File("C:\\Users\\37529\\antiplagiat\\src\\test\\java\\files\\text_4.txt");

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

        System.out.println("---cosineSimilarity---");

        String[] x = keywords.toArray(new String[0]);
        String[] y = keywords2.toArray(new String[0]);
        Map<String, Integer> freqX = new HashMap<>();
        Map<String, Integer> freqY = new HashMap<>();
        double f1 = cosineSimilarity(x, y, freqX, freqY);
        System.out.println("F1 = " + f1);

        System.out.println("---calculateAverageSentenceLength---");

        double avgSentenceLengthText1 = calculateAverageSentenceLength(text);
        double avgSentenceLengthText2 = calculateAverageSentenceLength(text2);
        System.out.println("text 1: " + calculateAverageSentenceLength(text));
        System.out.println("text 2: " + calculateAverageSentenceLength(text2));

        System.out.println("---calculateAverageComplexity---");

        double complexSentencesPercentText1 = calculateAverageComplexity(text);
        double complexSentencesPercentText2 = calculateAverageComplexity(text2);

        System.out.println("text 1: " + calculateAverageComplexity(text) + "%");
        System.out.println("text 2: " + calculateAverageComplexity(text2) + "%");

        System.out.println("---SaatyMethod---");

        // Рассчитываем значения критериев
        // Создаем матрицу попарных сравнений критериев
//        double f1 = 0.14285714285714285;
//        double avgSentenceLengthText1 = 28.272727272727273;
//        double avgSentenceLengthText2 = 14.692307692307692;
//        double complexSentencesPercentText1 = 72.72727272727273;
//        double complexSentencesPercentText2 = 28.125;

        startSaaty(f1, avgSentenceLengthText1, avgSentenceLengthText2,
                complexSentencesPercentText1, complexSentencesPercentText2);


    }




}

