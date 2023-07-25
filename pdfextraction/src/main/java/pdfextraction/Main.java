package pdfextraction;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String inputFileName = "/Users/rayaankhan/repos/CP317-Project/pdfextraction/sample_reports/cloudcomputing.pdf"; // where PDF is located

        // Where results of pdf extraction will be output
        String outputFolder = System.getProperty("user.dir");
        outputFolder += "/pdfextraction/src";

        // Create a new folder named "output" to store images and result.txt
        File folder = new File(outputFolder + "/output");
        folder.mkdirs();
        outputFolder = folder.getAbsolutePath(); // Update outputFolder with the new folder path

        // create a TextExtraction Object
        TextExtraction pdfExtractor = new TextExtraction(outputFolder);
        PDDocument document = PDDocument.load(new File(inputFileName));

        // extract Images & Text from the Document
        pdfExtractor.runExtraction(document);

        // parse text and break up into paragraphs and store in a json
        TextSegmenter.divide(outputFolder);

        // summarize every paragraph

        document.close();

        //To delete the output folder
//        System.out.println("Output folder deleting...");
//        FileUtils.deleteDirectory(new File(outputFolder));
//        System.out.println("Output folder deleted.");

    }
}
