package pdfextraction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.PDFStreamEngine;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;


import java.awt.image.BufferedImage;
public class TextExtraction extends PDFStreamEngine {

    // Used in naming images. Ex. image_1, image_2, etc. It is incremented when images are extracted
    private int imageNumber = 1;
    private int pageNum = 0;
    private static ArrayList<Integer> array;
    private String outputFolder; // New instance variable to hold the output folder path
    // Constructor to set the output folder path

    public TextExtraction(String outputFolder) {
        this.outputFolder = outputFolder;
        this.array = new ArrayList<>();
    }

    public void runExtraction(PDDocument document, String projectRoot) throws IOException {

        SaveImagesInPdf(document);
        getNumImages();
        getPage(1);
        ExtractText(document, projectRoot);

    }

    public void SaveImagesInPdf(PDDocument document) throws IOException {
        try {
            for (PDPage page : document.getPages()) {
                pageNum++;
                System.out.println("Processing page: " + pageNum);
                this.processPage(page);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts text from a given PDDocument and writes it to a text file.
     *
     * @param document The PDDocument from which text needs to be extracted.
     * @throws IOException If an I/O error occurs while reading the document or writing the output.
     */
    public void ExtractText(PDDocument document, String projectRoot) throws IOException {

        // Creating PDFTextStripper obj
        PDFTextStripper pdfStripper = new PDFTextStripper();
        int totalPages = document.getNumberOfPages();

        String sPage = ""; // start page
        String ePage = ""; // end page
        String text = "";
        String text2 = "";
        List<String> lines;

        for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {

            pdfStripper.setStartPage(pageNumber);
            pdfStripper.setEndPage(pageNumber);

            sPage = "***START OF PAGE " + pageNumber + "***\n";
            ePage = "***END OF PAGE " + pageNumber + "***\n";

            text2 = pdfStripper.getText(document); // converts entire pdf to text
            lines = detectLines(text2); // returns the entire pdf as an ArrayList, each line is an item

            text += sPage;
            for (String line : lines) {
                if(!junkTest(line) && line.length() > 3) {
                    text+= line + "\n";
                }
            }
            text += ePage + "\n";

        }

        // Create a PdfContent object with the extracted text
        PdfContent pdfContent = new PdfContent(text);

        // Convert the PdfContent object to JSON using Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Configure Gson to preserve new lines and white spaces
        Gson gsonWithEscapeHtml = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping() // This will prevent escaping of new lines and other special characters
                .create();

        // Convert the PdfContent object to JSON
        String json = gsonWithEscapeHtml.toJson(pdfContent);

        // Save the JSON string to a file or use it as needed
        try (FileWriter fileWriter = new FileWriter(projectRoot + "/output/parsedPDF.json")) {
            fileWriter.write(json);
            System.out.println("JSON data has been written to 'output.json' successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Detects and extracts lines from the input text.
     *
     * @param text The input text from which paragraphs need to be detected.
     * @return A list of lines extracted from the input text.
     */
    private static List<String> detectLines(String text) {

        // create an Arraylist of Strings, each string represents a line
        List<String> lines = new ArrayList<>();

        // regex to match line boundaries based on two or more consecutive line breaks.
        String linePattern = "(?m)(?s)(^.*?)(?:\\r?\\n\\r?\\n|$)";

        Pattern pattern = Pattern.compile(linePattern);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            // Trim the line to remove leading and trailing whitespaces or line breaks.
            String line = matcher.group(1).trim();

            // Add the line to the list.
            lines.add(line);
        }

        return lines;
    }

    public void addItem(int item) {
        array.add(item);
    }

    public int getNumImages() {
        return array.size();
    }

    public int getPage(int imageNum) {
        if(imageNum > getNumImages()) {
            return -1;
        }
        return array.get(imageNum-1);
    }

    // Return array with image page numbers
    public static ArrayList<Integer> getImageArray() {
        return array;
    }

    /**
     * Helper Function for the Image extraction method
     *
     * @param operator The operation to perform.
     * @param operands The list of arguments.
     *
     * @throws IOException If there is an error processing the operation.
     */
    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();
        // Do is the PDF operator for xobjects (like images)
        // there are a bunch of PDF operators for things like bolded text, titles,
        // diagrams, etc.
        if ("Do".equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xobject = getResources().getXObject(objectName);

            if (xobject instanceof PDImageXObject) {
                PDImageXObject image = (PDImageXObject) xobject;

                // same image to output folder
                BufferedImage bImage = image.getImage();
                // you can change the name of the image here
                ImageIO.write(bImage, "PNG", new File(outputFolder + "/image_" + imageNumber + ".png"));
                System.out.println("Image saved.");
                this.addItem(pageNum);
                imageNumber++;

            } else if (xobject instanceof PDFormXObject) {
                PDFormXObject form = (PDFormXObject) xobject;
                showForm(form);
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

    /**
     * Checks if the input string contains more letters than numbers. to filter out non-image tables as we are unable
     * to extract them (no library in java exists for this)
     * @param s The input string to be tested.
     * @return True if the input string contains more letters than numbers, otherwise false.
     */
    private boolean junkTest(String s) {
        int letterCount = 0;
        int numberCount = 0;

        // Iterate through each character in the input string
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                letterCount++;
            } else if (Character.isDigit(c)) {
                numberCount++;
            }
        }
        if(letterCount>=numberCount) {
            return false;
        }
        return true;
    }
}