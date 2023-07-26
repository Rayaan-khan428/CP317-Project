package pdfextraction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFDivision {

    // Word count if dividing by paragraphs so there is enough info per slide if it
    // divides by paragraph
    private static final int MIN_WORDS_PER_SECTION = 50;

    public static ArrayList<Slide> divide(String inputFileLocation) {

	// Array of slide objects
	ArrayList<Slide> presentation = new ArrayList<>();

	// Get file open and stuff
	String inputFilePath = inputFileLocation + "//result.txt";
	int sectionCount = 1;

	// Page count is updated every time a new page is started
	int pageCount = 0;

	try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
	    StringBuilder text = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
		text.append(line).append("\n");
	    }

	    // Use Java regex library to find numbers
	    Pattern NumPattern = Pattern.compile("(?m)(?<=\\n\\n|^)\\d+\\.?\\s*?\\s*");
	    Matcher NumMatcher = NumPattern.matcher(text.toString());

	    // Check for letter formatting (roman numerals included)
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

		    // Split line by line for title and page detection
		    String[] lines = section.split("\\n");

		    // First line for title
		    String firstLine = lines[0];

		    // Strip of numbers and colons and periods
		    firstLine = firstLine.replaceAll("[0-9:.*]", "");

		    // Remove leading spaces from the first line
		    firstLine = firstLine.replaceAll("^\\s+", "");

		    // Ensure it does use page start or end for the first title as it is not cut not
		    // * is already striped
		    if (firstLine.startsWith("START OF PAGE") | firstLine.startsWith("END OF PAGE")) {
			firstLine = lines[1];
			firstLine = firstLine.replaceAll("[^a-zA-Z\\s]", "");
			firstLine = firstLine.replaceAll("^\\s+", "");
		    }

		    // Determine if the page ends during the paragraph and if so add 1 to page count
		    for (String SingleLine : lines) {
			if (SingleLine.startsWith("***START OF PAGE")) {
			    pageCount += 1;
			}
		    }

		    // Save section into "sectionX.txt", where X is the section number (starting
		    // from 1)
		    String outputFilePath = inputFileLocation + "//section" + (sectionCount) + ".txt";
		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			writer.write(section);

			// Create slide object and add to array, 0 temp as page num for now
			presentation.add(new Slide(sectionCount, pageCount, firstLine, ""));

			sectionCount += 1;

		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    } else if (LetMatcher.find()) {
		String[] sections = text.toString().split("(?m)(?<=\\n)[IVXLCDM]+\\.|(?<=\\n)[A-Z]\\.");

		for (int i = 0; i < sections.length; i++) {
		    String section = sections[i].trim();

		    // Skip empty sections
		    if (section.isEmpty()) {
			continue;
		    }

		    // Split line by line for title and page detection
		    String[] lines = section.split("\\n");

		    // First line for title
		    String firstLine = lines[0];

		    // Strip of numbers and colons and periods
		    firstLine = firstLine.replaceAll("[0-9:.*]", "");

		    // Remove leading spaces from the first line
		    firstLine = firstLine.replaceAll("^\\s+", "");

		    // Ensure it does use page start or end for the first title as it is not cut not
		    // * is already striped
		    if (firstLine.startsWith("START OF PAGE") | firstLine.startsWith("END OF PAGE")) {
			firstLine = lines[1];
			firstLine = firstLine.replaceAll("[^a-zA-Z\\s]", "");
			firstLine = firstLine.replaceAll("^\\s+", "");
		    }

		    // Determine if the page ends during the paragraph and if so add 1 to page count
		    for (String SingleLine : lines) {
			if (SingleLine.startsWith("***START OF PAGE")) {
			    pageCount += 1;
			}
		    }

		    // Save section into "sectionX.txt", where X is the section number (starting
		    // from 1)
		    String outputFilePath = inputFileLocation + "//section" + (sectionCount) + ".txt";
		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			writer.write(section);

			// Create slide object and add to array
			presentation.add(new Slide(sectionCount, pageCount, firstLine, ""));

			sectionCount += 1;
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    } else {
		// If no sections are identified after blank lines, split the text into
		// paragraphs
		String[] sections = text.toString().split("(?m)^\\s*$");

		StringBuilder sectionBuilder = new StringBuilder();
		int wordCount = 0;

		for (int i = 0; i < sections.length; i++) {
		    String section = sections[i].trim();

		    // Skip empty paragraphs
		    if (section.isEmpty()) {
			continue;
		    }

		    // Split line by line for title and page detection
		    String[] lines = section.split("\\n");

		    // First line for title
		    String firstLine = lines[0];

		    // Strip of numbers and colons and periods
		    firstLine = firstLine.replaceAll("[0-9:.*]", "");

		    // Remove leading spaces from the first line
		    firstLine = firstLine.replaceAll("^\\s+", "");

		    // Ensure it does use page start or end for the first title as it is not cut not
		    // * is already striped
		    if (firstLine.startsWith("START OF PAGE") | firstLine.startsWith("END OF PAGE")) {
			firstLine = lines[1];
			firstLine = firstLine.replaceAll("[^a-zA-Z\\s]", "");
			firstLine = firstLine.replaceAll("^\\s+", "");
		    }

		    // Determine if the page ends during the paragraph and if so add 1 to page count
		    for (String SingleLine : lines) {
			if (SingleLine.startsWith("***START OF PAGE")) {
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
			// Save the section into "sectionX.txt", where X is the section number (starting
			// from 1)
			String outputFilePath = inputFileLocation + "//section" + (sectionCount) + ".txt";
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			    writer.write(sectionBuilder.toString());
			    // Create slide object and add to array
			    presentation.add(new Slide(sectionCount, pageCount, firstLine, ""));
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
		    String outputFilePath = inputFileLocation + "//section" + sectionCount + ".txt";
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

	// Call slide to match images to slides
	matchImages(presentation);

	// Display objects if you want to check titles and such
	// for (Slide slide : presentation) {
	// System.out.println("Slide Num: " + slide.getSlideNum() + ", Page Num: " +
	// slide.getPageNum() + ", Slide Title: " + slide.getTitle());
	// System.out.println("Image index contained: " + slide.getImage());
	// }
	return presentation;

    }

    // Update images to match with slides
    // Special function to match images to last paragraph on the same page
    public static void matchImages(ArrayList<Slide> presentation) {
	// Get array with image locations from healper method in PDFExtract
	ArrayList<Integer> array = PDFExtract.getImageArray();

	// Temp is page num, i is slide num, x is image array index
	int temp = 1;
	int i = 0;

	// For loop to look through slides and find where the page numbers change
	// Once the page number has changed apply the photo to the paragraph(s)
	for (Slide slide : presentation) {

	    // Check if page number updated
	    if (slide.PageNum > temp) {
		temp += 1;

		for (int x = 0; x < array.size(); x++) {
		    if (array.get(x) == (temp - 1))
			// Add images array location to previous slide (last on page) string
			presentation.get(i - 1).Image += (x) + " ";
		}
	    }

	    // Increment index number
	    i += 1;
	}

    }

}