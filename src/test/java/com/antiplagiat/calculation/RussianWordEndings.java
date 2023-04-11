package com.antiplagiat.calculation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RussianWordEndings {

    public static String removeRussianWordEnding(String word) {
        // Define a regular expression pattern for Russian word endings
        Pattern pattern = Pattern.compile("(а|я|о|е|ё|ы|и|у|ю|ей|ам|ям|ом|е|ев|ов|ий|ь|и|ы|у|а|е)?\\b");
        // Create a matcher object to match the pattern against the input word
        Matcher matcher = pattern.matcher(word);
        // Replace the matched substring with an empty string
        return matcher.replaceAll("");
    }
}
