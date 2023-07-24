package pdfextraction;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFDivision {
	
	// Word count if dividing by paragraphs so there is enough info per slide if it divides by paragraph 
	private static final int MIN_WORDS_PER_SECTION = 50;

    public static void divide(String inputFileLocation) {
    	
    	// Array of slide objects 
    	ArrayList<Slide> presentation = new ArrayList<>();
    	
    	// Get file open and stuff 
        String inputFilePath = inputFileLocation + "\\result.txt";
        int sectionCount = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            // Use Java regex library to find numbers
            Pattern NumPattern = Pattern.compile("(?m)(?<=\\n\\n|^)\\d+\\.?\\s*?\\s*");
            Matcher NumMatcher = NumPattern.matcher(text.toString());        
            
            // Check for letter formatting (romal numerals included)
            Pattern LetPattern = Pattern.compile("(?m)(?<=\\n)[IVXLCDM]+\\.|(?<=\\n)[A-Z]\\.");
            Matcher LetMatcher = LetPattern.matcher(text.toString());

            if (NumMatcher.find()) {
                String[] sections = text.toString().split("(?m)(?<=\\n\\n|^)\\d+\\.?\\s*?\\s*");

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
                        
                        // Create slide object and add to array 
                        presentation.add(new Slide(sectionCount,"temp"));
                        
                        sectionCount += 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } 
            else if (LetMatcher.find()) {
            	String[] sections = text.toString().split("(?m)(?<=\\n)[IVXLCDM]+\\.|(?<=\\n)[A-Z]\\.");

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
                        
                        // Create slide object and add to array 
                        presentation.add(new Slide(sectionCount,"temp"));
                        
                        sectionCount += 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                // If no sections are identified after blank lines, split the text into paragraphs
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
                            // Create slide object and add to array 
                            presentation.add(new Slide(sectionCount,"temp"));
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
        
//		  Display objects if you want to check titles and such 
//        for (Slide slide : presentation) {
//            System.out.println(slide.getSlideNum() + ": " + slide.getTitle());
//        }
    }
    
}

