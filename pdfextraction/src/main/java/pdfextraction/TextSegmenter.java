package pdfextraction;

import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextSegmenter {

    // fall back variable in case paragraphs cannot be split by title
    private static final int MIN_WORDS_PER_SECTION = 50;

    /**
     * Divides the extracted text into subsections. Creates a slide class that stores all neccessary info
     * that would be needed for each slide when we convert to a powerpoint
     * @param extractedText
     * @throws JsonProcessingException
     */
    public static ArrayList<Slide> divide(String extractedText) throws JsonProcessingException, InterruptedException {

        // Array of slide objects
        ArrayList<Slide> presentation = new ArrayList<>();

        // keep track of section
        int sectionCount = 1;

        // keep track of pages, updated when new page is created
        int pageCount = 0;

        // Not sure why this is needed but will keep for now
        // holds the essay in a formatted parsable format
        StringBuilder text = new StringBuilder();

        // breaks the massive string into individual lines by '\n' and stores as an index
        String[] allLines = extractedText.split("\\n");

//        System.out.println("Length of textSections: " + allLines.length); // debugging

        // for every line in the allLines array, append it to text and add a new line
        for (String allLine : allLines) {
            text.append(allLine).append("\n");
//            System.out.println(allLine);
        }

        // Use Java regex library to find numbers
        Pattern NumPattern = Pattern.compile("(?m)(?<=\\n\\n|^)\\d+\\.?\\s*?\\s*");
        Matcher NumMatcher = NumPattern.matcher(text.toString());

        // Check for letter formatting (roman numerals included)
        Pattern LetPattern = Pattern.compile("(?m)(?<=\\n)[IVXLCDM]+\\.|(?<=\\n)[A-Z]\\.");
        Matcher LetMatcher = LetPattern.matcher(text.toString());

        // A header was found (numbers present)
        if (NumMatcher.find()) {
            String[] sections = text.toString().split("(?m)(?<=\\n\\n|^)\\d+\\.?\\s*?\\s*");
            segment(presentation, sectionCount, pageCount, sections);
        }

        // A header was found (roman numerals)
        else if (LetMatcher.find()) {
            String[] sections = text.toString().split("(?m)(?<=\\n)[IVXLCDM]+\\.|(?<=\\n)[A-Z]\\.");
            segment(presentation, sectionCount, pageCount, sections);
        }

        else {

            // If no sections are identified after blank lines, split the text into paragraphs

            // splitting a multi-line string into an array of strings, using empty lines as the delimiters.
            String[] sections = text.toString().split("(?m)^\\s*$");

            // sectionBuilder is where we store a sections text
            StringBuilder sectionBuilder = new StringBuilder();
            int wordCount = 0;

            // for every string item...
            for (int i = 0; i < sections.length; i++) {

                // remove leading and trailing white space
                String section = sections[i].trim();

                // Skip empty paragraphs
                if (section.isEmpty()) {
                    continue;
                }

                // Split line by line for title and page detection
                String[] lines = section.split("\\n");

                // First line for title
                String firstLine = lines[0];

                // Remove numbers and colons and periods leaving us with just the textual title
                firstLine = firstLine.replaceAll("[0-9:.*]", "");

                // Remove leading spaces from the first line
                firstLine = firstLine.replaceAll("^\\s+", "");

                // Ensure it does use page start or end for the first title as it is not cut not * is already striped
                if (firstLine.startsWith("START OF PAGE") | firstLine.startsWith("END OF PAGE") ) {
                    firstLine = lines[1];
                    firstLine = firstLine.replaceAll("[^a-zA-Z\\s]", "");
                    firstLine = firstLine.replaceAll("^\\s+", "");
                }

                // Determine if the page ends during the paragraph and if so add 1 to page count
                for (String SingleLine : lines) {
                    if(SingleLine.startsWith("***START OF PAGE")) {
                        pageCount += 1;
                    }
                }

                // Count the words in the current paragraph
                int paragraphWordCount = section.split("\\s+").length;
                wordCount += paragraphWordCount;

                // Append the current paragraph to the sectionBuilder
                sectionBuilder.append(section).append("\n");

                // Check if the word count in the sectionBuilder meets the minimum requirement
                if (wordCount >= MIN_WORDS_PER_SECTION) {
                    // Save the section into "sectionX.txt", where X is the section number (starting from 1)

//                    String outputFilePath = inputFileLocation + "//section" + (sectionCount) + ".txt";
//                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
//                        writer.write(sectionBuilder.toString());
//                        // Create slide object and add to array
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    presentation.add(new Slide(sectionCount, pageCount, firstLine, section));
                    sectionCount += 1;

                    // Reset sectionBuilder and wordCount for the next section
                    sectionBuilder.setLength(0);
                    wordCount = 0;
                }
            }

            // Save the remaining text in sectionBuilder as the last section
            if (sectionBuilder.length() > 0) {

                presentation.add(new Slide(sectionCount, pageCount, "--last section --", sectionBuilder.toString()));

//                String outputFilePath = inputFileLocation + "//section" + sectionCount + ".txt";
//                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
//                    writer.write(sectionBuilder.toString());
//                    sectionCount += 1;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }

        // Call slide to match images to slides
        matchImages(presentation);



        // Display objects if you want to check titles and such
//        for (Slide slide : presentation) {
//            System.out.println("\n\n------------------------------------------------------------------------------------------");
//            System.out.println("Slide Num: " + slide.getSlideNum() + "\nPage Num: " + slide.getPageNum() + "\nSlide Title: " + slide.getTitle() + "\n\nSlide Paragraph: \n" + slide.getParagraph());
//            System.out.println("\nImage index contained: " + slide.getImage());
//            System.out.println("------------------------------------------------------------------------------------------\n\n");
//        }

        return presentation;

    }

    /**
     * Segments the content of each section in the input array and creates Slide objects for each section.
     * The Slide objects are added to the presentation ArrayList. Each section is saved to a separate file,
     * and the page count within each section is determined based on the occurrence of "***START OF PAGE" lines.
     *
     * @param presentation      An ArrayList to store Slide objects representing each section in the presentation.
     * @param sectionCount       An integer representing the starting section number.
     * @param pageCount          An integer representing the starting page count.
     * @param sections           A String array containing the content of each section in the presentation.
     */
    private static void segment(ArrayList<Slide> presentation, int sectionCount, int pageCount, String[] sections) throws JsonProcessingException {

        for (int i = 0; i < sections.length; i++) {

            String section = sections[i].trim();

            // Skip empty sections
            if (section.isEmpty()) {
                continue;
            }

            // Split line by "\n"
            String[] lines = section.split("\\n");

            // First line for title
            String firstLine = lines[0];

            // remove numbers, periods, colons, etc.
            firstLine = firstLine.replaceAll("[0-9:.*]", "");

            // Remove any leading spaces from the first line
            firstLine = firstLine.replaceAll("^\\s+", "");

            // Ensure it does use page start or end for the first title as it is not cut not * is already striped
            if (firstLine.startsWith("START OF PAGE") | firstLine.startsWith("END OF PAGE") ) {
                firstLine = lines[1];
                firstLine = firstLine.replaceAll("[^a-zA-Z\\s]", "");
                firstLine = firstLine.replaceAll("^\\s+", "");
            }

            // Determine if the page ends during the paragraph and if so add 1 to page count
            for (String SingleLine : lines) {
                if(SingleLine.startsWith("***START OF PAGE")) {
                    pageCount += 1;
                }
            }

            presentation.add(new Slide(sectionCount, pageCount, firstLine, section));

            // Use ObjectMapper to convert presentation to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(presentation);

            // Save JSON to a file in the current directory
            String fileName = "/Users/rayaankhan/repos/CP317-Project/pdfextraction/src/output/presentation.json";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(json);
            } catch (Exception e) {
                // Handle any exceptions that may occur during file writing
                e.printStackTrace();
            }

        }
    }

    // Update images to match with slides
    // Special function to match images to the last paragraph on the same page
    public static void matchImages(ArrayList<Slide> presentation) {
        // Get array with image locations from helper method in PDFExtract
        ArrayList<Integer> array = TextExtraction.getImageArray();

        // Temp is the page num, i is the slide num, x is the image array index
        int temp = 1;
        int i = 0;

        // For loop to look through slides and find where the page numbers change
        // Once the page number has changed apply the photo to the paragraph
        for (Slide slide : presentation) {

            // Check if the page number updated
            if (slide.PageNum > temp) {
                temp += 1;

                for (int x = 0; x < array.size(); x++) {
                    if (array.get(x) == (temp - 1))
                        // Add the image array location to the previous slide (last on page) list
                        presentation.get(i - 1).Image.add(x);
                }
            }

            // Increment the index number
            i += 1;
        }
    }

}