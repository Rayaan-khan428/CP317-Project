package pdfextraction;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

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

import java.awt.image.BufferedImage;
public class PDFExtract extends PDFStreamEngine {
    public int imageNumber = 1; // Used in naming images. Ex. image_1, image_2, etc. It is incremented when images are extracted
    private String outputFolder; // New instance variable to hold the output folder path

    public static void main(String[] args) throws IOException {
        PDDocument document = new PDDocument();
        String inputFileName = "C:/Users/ryanm/Desktop/CP317/Lizard_Research.pdf"; // Specify the input file path here

        // Get the output folder path (project directory)
        String outputFolder = System.getProperty("user.dir");
        outputFolder += "\\src\\t1";

        // Create a new folder named "output" to store images and result.txt
        File folder = new File(outputFolder + "/output");
        folder.mkdirs();
        outputFolder = folder.getAbsolutePath(); // Update outputFolder with the new folder path

        // The methods. You can test each one separately if you want
        SaveImagesInPdf(document, inputFileName, outputFolder);
        ExtractText(document, inputFileName, outputFolder);

        document.close();

        // Reference to the folder is passed
        // Needs Ryan's PDFDivision.java to be imported work
        //PDFDivision.divide(outputFolder);
    }

    /**
     * Extracts images from a PDF document and saves them to the specified output folder.
     *
     * @param document      The PDDocument object representing the PDF document.
     * @param fileName      The path of the PDF file to extract images from.
     * @param outputFolder  The path of the folder where extracted images will be saved.
     * @throws IOException  If there's an error reading the PDF file or writing images to the output folder.
     */
    public static void SaveImagesInPdf(PDDocument document, String fileName, String outputFolder) throws IOException {
        try {
            document = PDDocument.load(new File(fileName));
            PDFExtract printer = new PDFExtract(outputFolder); // Initialize the instance variable

            int pageNum = 0;

            for (PDPage page : document.getPages()) {
                pageNum++;
                System.out.println("Processing page: " + pageNum);
                printer.processPage(page);
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    // Constructor to set the output folder path
    public PDFExtract(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    /**
     * Helper Function for the Image extraction method
     * @param operator The operation to perform.
     * @param operands The list of arguments.
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
     * Extracts text from a PDF document and saves it to a text file in the specified output folder.
     *
     * @param document      The PDDocument object representing the PDF document.
     * @param fileName      The path of the PDF file to extract text from.
     * @param outputFolder  The path of the folder where the extracted text will be saved as a text file.
     * @throws IOException  If there's an error reading the PDF file or writing the text file to the output folder.
     */
    public static void ExtractText(PDDocument document, String fileName, String outputFolder) throws IOException {
        document = PDDocument.load(new File(fileName));

        // Creating PDFTextStripper obj
        PDFTextStripper pdfStripper = new PDFTextStripper();

        // Retrieving and outputting text from PDF document
        String text = pdfStripper.getText(document);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFolder + "/result.txt"))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}