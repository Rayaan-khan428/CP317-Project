package t1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PDFDivision {

	// Word min to stop it from splitting small useless paragraphs (on zero for testing but probs gonna be like 50?) 
	// Still need to work out regex stuff to make the word limit only apply to paragraphs split 
	// Rn it only checks the first line for regex 
	
	// Mabye do the opposite and split the paragprahs based on regex then split again as needed if they are too large? 
	private static final int MIN_WORDS_PER_SECTION = 0;

    public static void divide(String inputFileLocation) {
        String inputFilePath = inputFileLocation + "\\result.txt";
        int sectionCount = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            // Check if the entire text contains the regex pattern
            boolean containsRegexPattern = text.toString().matches("(?s).*[IVXLCDM]+|[A-Z]+\\b|[0-9]+\\b.*");

            if (containsRegexPattern) {
                // Find all sections using the regex pattern
                String[] sections = text.toString().split("[IVXLCDM]+|[A-Z]+\\b|[0-9]+\\b");

                
                // Save each section into separate files
                for (int i = 0; i < sections.length; i++) {
                    String section = sections[i].trim();

                    // Skip empty sections
                    if (section.isEmpty()) {
                        continue;
                    }

                    // Save section into "sectionX.txt", where X is the section number (starting from 1)
                    String outputFilePath = inputFileLocation + "\\section" + (sectionCount) + ".txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                        writer.write(section);
                        sectionCount += 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // If no sections are identified, split the text into paragraphs
                String[] paragraphs = text.toString().split("(?m)^\\s*$");

                StringBuilder sectionBuilder = new StringBuilder();
                int wordCount = 0;

                for (int i = 0; i < paragraphs.length; i++) {
                    String paragraph = paragraphs[i].trim();

                    // Skip empty paragraphs
                    if (paragraph.isEmpty()) {
                        continue;
                    }

                    // Count the words in the current paragraph
                    int paragraphWordCount = paragraph.split("\\s+").length;
                    wordCount += paragraphWordCount;

                    // Append the current paragraph to the sectionBuilder
                    sectionBuilder.append(paragraph).append("\n");

                    // Check if the word count in the sectionBuilder meets the minimum requirement
                    if (wordCount >= MIN_WORDS_PER_SECTION) {
                        // Save the section into "sectionX.txt", where X is the section number (starting from 1)
                        String outputFilePath = inputFileLocation + "\\section" + (sectionCount) + ".txt";
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                            writer.write(sectionBuilder.toString());
                            sectionCount += 1;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Reset sectionBuilder and wordCount for the next section
                        sectionBuilder.setLength(0);
                        wordCount = 0;
                    }
                }

                // Save the remaining text in sectionBuilder as the last section
                if (sectionBuilder.length() > 0) {
                    String outputFilePath = inputFileLocation + "\\section" + sectionCount + ".txt";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                        writer.write(sectionBuilder.toString());
                        sectionCount += 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}