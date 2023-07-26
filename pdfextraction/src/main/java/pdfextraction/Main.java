package pdfextraction;

import java.io.File;
import java.io.IOException;

import com.google.gson.stream.JsonReader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        // stores presentation objects
        ArrayList<Slide> presentation = new ArrayList<>();

        String projectRoot = System.getProperty("user.dir");

        // where sample pdf is located
        String inputFileName = projectRoot + "/pdfextraction/sample_reports/Lizard_Research.pdf";

        // Where results of pdf extraction will be output
        String outputFolder = projectRoot += "/pdfextraction/src";

        // Create a new folder named "output" to store images and result.txt
        File folder = new File(outputFolder + "/output");
        folder.mkdirs();
        outputFolder = folder.getAbsolutePath(); // Update outputFolder with the new folder path

        // create a TextExtraction Object
        TextExtraction pdfExtractor = new TextExtraction(outputFolder);
        PDDocument document = PDDocument.load(new File(inputFileName));

        // extract Images & Text from the Document
        pdfExtractor.runExtraction(document, projectRoot);

        // Read the JSON file
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(new FileReader(projectRoot + "/output/parsedPDF.json")).getAsJsonObject();

        // Extract the content from the "text" field
        String extractedText = jsonObject.get("text").getAsString();

        // parse text and break up into paragraphs and store in a json
        presentation = TextSegmenter.divide(extractedText);

        // summarize the text
        TextSummarizer.summarize(presentation);

        // generate powerpoint
        PowerPointGenerator.create(presentation, projectRoot);

        document.close();

        System.exit(0);


    }
}
