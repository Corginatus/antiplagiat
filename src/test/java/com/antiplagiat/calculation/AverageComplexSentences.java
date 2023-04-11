package com.antiplagiat.calculation;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AverageComplexSentences {

    public static double calculateAverageComplexity(String text) {
        // Split text into sentences using regular expressions
        String[] sentences = text.split("[.!?]\\s*");

        int complexSentencesCount = 0;
        int totalSentencesCount = sentences.length;

        for (String sentence : sentences) {
            // Check if the sentence contains one of the complex conjunctions
            if (sentence.contains(" что ") || sentence.contains(" как ") || sentence.contains(" где ")
                    || sentence.contains(" когда ") || sentence.contains(" пока ") || sentence.contains(" поскольку ")
                    || sentence.contains(" потому что ") || sentence.contains(" так как ") || sentence.contains(" также как ")
                    || sentence.contains(" которая ") || sentence.contains(" это ") || sentence.contains(" если ")
                    || sentence.contains(" который ") || sentence.contains(" которые ") || sentence.contains(" которому ")
                    || sentence.contains(" тогда как ") || sentence.contains(" хотя ") || sentence.contains(" чем ")
                     || sentence.contains(" которой ") || sentence.contains(" чтобы ")) {
                complexSentencesCount++;
            }
        }

        // Calculate the percentage of complex sentences
        double percentage = ((double) complexSentencesCount / totalSentencesCount) * 100;

        return percentage;
    }

}

