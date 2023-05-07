package com.antiplagiat.calculation;

import javax.sound.midi.Soundbank;
import java.util.Arrays;
import java.util.regex.Pattern;


public class AverageSentenceLength {

    public static void main(String[] args) {
        double avgSentenceLengthText1 = calculateAverageSentenceLength("Быстрая бурая лиса перепрыгивает через ленивую собаку, а быстрый, который поезд\" +\n" +
                "//                \" проехал ленивое авто. Когда лиса прыгнула на поезд, который впрочем и не поезд, то упала. Конец.");

        System.out.println("text 1: " + avgSentenceLengthText1);
    }

    public static double calculateAverageSentenceLength(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
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
        if (numSentences == 0) {
            throw new IllegalArgumentException("Text does not contain any sentences");
        }
        // Calculate the average sentence length
        double averageLength = (double) totalWords / numSentences;
        // Define a regular expression pattern to match sentence boundaries
        Pattern patternl = Pattern.compile("[.!?]+[\\s\\u2014\\u2026]*");
        // Split the text into an array of sentences using the pattern
        String[] sentencesl = patternl.split(text);
        // Calculate the length of the longest sentence
        double maxLength = Arrays.stream(sentencesl)
                .mapToDouble(sentence -> sentence.split("\\s+").length)
                .max()
                .orElse(0);
//        System.out.println("maxLength:" + maxLength);
//        System.out.println("averageLength:" + averageLength);
        averageLength = averageLength / maxLength;
        return averageLength;
    }

}

