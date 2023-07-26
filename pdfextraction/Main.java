package pdfextraction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;

public class Main {
    public static void main(String[] args) throws IOException {
	String inputFileName = "C:/Users/brandon/Downloads/Lizard_Research (1).pdf"; // Specify the input file path here

	// Get the output folder path (project directory)
	String outputFolder = System.getProperty("user.dir");
	outputFolder += "//src//t1";

	// Create a new folder named "output" to store images and result.txt
	File folder = new File(outputFolder + "/output");
	folder.mkdirs();
	outputFolder = folder.getAbsolutePath(); // Update outputFolder with the new folder path

	PDFExtract pdfExtractor = new PDFExtract(outputFolder);
	PDDocument document = PDDocument.load(new File(inputFileName));

	// The methods. You can test each one separately if you want
	pdfExtractor.SaveImagesInPdf(document);
	pdfExtractor.getNumImages();
	pdfExtractor.getPage(1);
	pdfExtractor.ExtractText(document);
	pdfExtractor.onlyText(document);

	// Reference to the folder is passed
	// Needs Ryan's PDFDivision.java to be imported work

	ArrayList<Slide> presentation = PDFDivision.divide(outputFolder);
	PowerPointGenerator.create(outputFolder, presentation);

	document.close();
    }
}