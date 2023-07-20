package t1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PDFDivision {

    public static void divide(String inputFile) {
        // Input file path from the main function
        String inputFilePath = inputFile;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            // Read the text from the input file
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            // Split the text based on empty lines (paragraphs)
            String[] paragraphs = text.toString().split("\\s*\\n\\s*");

            // Save each paragraph into separate files
            for (int i = 0; i < paragraphs.length; i++) {
                String paragraph = paragraphs[i];

                // Skip empty paragraphs
                if (paragraph.trim().isEmpty()) {
                    continue;
                }

                // Save paragraph into "sectionX.txt", where X is the paragraph number (starting from 1)
                String outputFilePath = "C:/Users/ryanm/CP317/PDFToPPT/src/t1/section" + (i + 1) + ".txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                    writer.write(paragraph);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
