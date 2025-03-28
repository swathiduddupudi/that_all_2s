/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;
import java.nio.file.*;

public class Main {

    // List of words to excluded from analysis
    private static final Set<String> exclusions = new HashSet<>(Arrays.asList(
            "in", "on", "at", "by", "for", "with", "about", "as", "of", "to", "and", "but", "or", "nor", "so", "the", "a", "an", 
            "he", "she", "it", "they", "we", "you", "is", "was", "were", "are", "be", "been", "being", "has", "have", "had", 
            "having", "does", "do", "did", "doing"
    ));

    public static void main(String[] args) {
        try {
            // URL for the text file
            URL oracle = new URL("https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt");


            // Use a map to store the word counts
            Map<String, Integer> wordCountMap = new HashMap<>();
            Set<String> uniqueWords = new TreeSet<>();

            // Read the file and process it
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(oracle.openStream()))) {
                String line;
                Pattern wordPattern = Pattern.compile("[a-zA-Z]+");

                while ((line = reader.readLine()) != null) {
                    line = line.toLowerCase(); // Case insensitive processing
                    Matcher matcher = wordPattern.matcher(line);

                    while (matcher.find()) {
                        String word = matcher.group();

                        // Exclude words with 's' at the end (plural form)
                        if (word.endsWith("'s")) {
                            word = word.substring(0, word.length() - 2);
                        }

                        // Ignore excluded words
                        if (!exclusions.contains(word)) {
                            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                            uniqueWords.add(word);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading from the URL: " + e.getMessage());
                return;
            }

            // Calculate total valid word count
            int totalValidWords = wordCountMap.values().stream().mapToInt(Integer::intValue).sum();

            // Find top 5 most frequent words
            List<Map.Entry<String, Integer>> topWords = new ArrayList<>(wordCountMap.entrySet());
            topWords.sort((a, b) -> b.getValue().compareTo(a.getValue())); // Sort by frequency
            topWords = topWords.subList(0, Math.min(5, topWords.size()));

            // Output Results
            System.out.println("Total word count: " + totalValidWords);

            System.out.println("\nTop 5 most frequent words:");
            for (int i = 0; i < topWords.size(); i++) {
                System.out.println((i + 1) + ". " + topWords.get(i).getKey() + ": " + topWords.get(i).getValue());
            }

            System.out.println("\nAlphabetically sorted unique words (Top 50):");
            int count = 0;
            for (String word : uniqueWords) {
                System.out.println(word);
                count++;
                if (count == 50) break;
            }
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + e.getMessage());
        }
    }
}
