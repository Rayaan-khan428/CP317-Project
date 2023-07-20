package t1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFTextStripping {

    public static void main(String[] args) throws IOException {
        // Creating a file object for use with text stripping
        // Change location to test with different pdfs
        File file = new File("C:/Users/ryanm/Desktop/CP317/Lizard_Research.pdf");
        // Creating a PDFBox obj (for text stripping)
        PDDocument doc = PDDocument.load(file);

        // Creating PDFTextStripper obj
        PDFTextStripper pdfStripper = new PDFTextStripper();

        // Retrieving text from PDF document
        String text = pdfStripper.getText(doc);

        // Output file path where the stripped text will be saved
        String outputFilePath = "C:/Users/ryanm/CP317/PDFToPPT/src/t1/result.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing files and pdfbox objs
        doc.close();
        
        // Next function call 
        PDFDivision.divide(outputFilePath);
    }
}
