package com.antiplagiat.calculation;

import java.util.Arrays;
import java.util.regex.Pattern;

public class AverageSentenceLength {


    public static double calculateAverageSentenceLength(String text) {
        // Define a regular expression pattern to match sentence boundaries
        Pattern pattern = Pattern.compile("[.!?]+\\s*");
        // Split the text into an array of sentences using the pattern
        String[] sentences = pattern.split(text);
        // Calculate the total number of words in all sentences
        int totalWords = Arrays.stream(sentences)
                .mapToInt(sentence -> sentence.split("\\s+").length)
                .sum();
        // Calculate the total number of sentences
        int numSentences = sentences.length;
        // Calculate the average sentence length
        double averageLength = (double) totalWords / numSentences;
        return averageLength;
    }
}

