package pdfextraction;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFExtract extends PDFStreamEngine {

    // Used in naming images. Ex. image_1, image_2, etc. It is incremented when
    // images are extracted
    private int imageNumber = 1;
    private int pageNum = 0;
    private static ArrayList<Integer> array;
    private String outputFolder; // New instance variable to hold the output folder path
    // Constructor to set the output folder path

    public PDFExtract(String outputFolder) {
	this.outputFolder = outputFolder;
	PDFExtract.array = new ArrayList<>();
    }

    public void SaveImagesInPdf(PDDocument document) throws IOException {
	try {
	    // PDFExtract printer = new PDFExtract(this.outputFolder); // Initialize the
	    // instance variable

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

    public void addItem(int item) {
	array.add(item);
    }

    public int getNumImages() {
	return array.size();
    }

    public int getPage(int imageNum) {
	if (imageNum > getNumImages()) {
	    return -1;
	}
	return array.get(imageNum - 1);
    }

    // Return array with image page numbers
    public static ArrayList<Integer> getImageArray() {
	return array;
    }

    public void ExtractText(PDDocument document) throws IOException {

	// Creating PDFTextStripper obj
	PDFTextStripper pdfStripper = new PDFTextStripper();
	int totalPages = document.getNumberOfPages();
	String text = "";
	String text2 = "";
	List<String> paragraphs;

	try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFolder + "/result.txt"))) {
	    for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
		pdfStripper.setStartPage(pageNumber);
		pdfStripper.setEndPage(pageNumber);

		text = "***START OF PAGE " + pageNumber + "***";

		text2 = pdfStripper.getText(document);
		paragraphs = detectParagraphs(text2);

		writer.write(text + "\n");
		for (String paragraph : paragraphs) {
		    if (!junkTest(paragraph) && paragraph.length() > 3) {
			writer.write(paragraph + "\n");
		    }
		}
		text = "***END OF PAGE " + pageNumber + "***\n";
		writer.write(text + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public String onlyText(PDDocument document) throws IOException {
	// Creating PDFTextStripper obj
	PDFTextStripper pdfStripper = new PDFTextStripper();

	int totalPages = document.getNumberOfPages();
	String text = "";
	String text2 = "";
	List<String> paragraphs;

	for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
	    pdfStripper.setStartPage(pageNumber);
	    pdfStripper.setEndPage(pageNumber);

	    text += "***START OF PAGE " + pageNumber + "***";

	    text2 = pdfStripper.getText(document);
	    paragraphs = detectParagraphs(text2);

	    for (String paragraph : paragraphs) {
		if (!junkTest(paragraph) && paragraph.length() > 3) {
		    text += paragraph + "\n";
		}
	    }

	    text += "***END OF PAGE " + pageNumber + "***\n";
	}

	return text;
    }

    private static List<String> detectParagraphs(String text) {
	List<String> paragraphs = new ArrayList<>();

	// Define a regular expression to match paragraph boundaries based on two or
	// more consecutive line breaks.
	String paragraphPattern = "(?m)(?s)(^.*?)(?:\\r?\\n\\r?\\n|$)";

	Pattern pattern = Pattern.compile(paragraphPattern);
	Matcher matcher = pattern.matcher(text);

	while (matcher.find()) {
	    // Trim the paragraph to remove leading and trailing whitespaces or line breaks.
	    String paragraph = matcher.group(1).trim();

	    // Add the paragraph to the list.
	    paragraphs.add(paragraph);
	}

	return paragraphs;
    }

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
	if (letterCount >= numberCount) {
	    return false;
	}
	return true;
    }
}