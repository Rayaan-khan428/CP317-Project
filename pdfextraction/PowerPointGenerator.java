package pdfextraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PowerPointGenerator {

    public static void create(String inputFileLocation, ArrayList<Slide> presentation) throws IOException { // creates
													    // powerpoint
													    // in
													    // inputFileLocation

	// Get the template folder path (project directory)
	String template = System.getProperty("user.dir");
	template += "//src//pdfextraction//templates//Pine Design.pptx";

	// Use template for PowerPoint
	XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(template));

	// Delete all existing slides in template before use
	for (int i = ppt.getSlides().size() - 1; i >= 0; i--) {
	    ppt.removeSlide(i);
	}

	// load slide master info at index 0 this will allow you to select layout
	// options
	XSLFSlideMaster slideOptions = ppt.getSlideMasters().get(0);

	// from the layout options we wish to use TITLE layout for the first slide
	XSLFSlideLayout titleForm = slideOptions.getLayout(SlideLayout.TITLE);

	// now we have loaded the layout, we create a new slide with said layout
	XSLFSlide slide1 = ppt.createSlide(titleForm);

	// add a title to the title slide from the first slide object in presentation
	// array list
	XSLFTextShape title1 = slide1.getPlaceholder(0);
	title1.setText(presentation.get(0).getTitle());

	// set subtitle to empty
	XSLFTextShape subtitle1 = slide1.getPlaceholder(1);
	subtitle1.setText("");

	// for every section we create one slide
	for (int i = 1; i < presentation.size(); i++) {
	    // get title and body format
	    XSLFSlideLayout titleAndBody = slideOptions.getLayout(SlideLayout.TITLE_AND_CONTENT);
	    // create new slide called midSlide with titleAndBody format
	    XSLFSlide midSlide = ppt.createSlide(titleAndBody);
	    // set title and body of slide to text from that slide object/section.txt file
	    XSLFTextShape slideTitle = midSlide.getPlaceholder(0);
	    slideTitle.setText(presentation.get(i).getTitle());
	    XSLFTextShape slideBody = midSlide.getPlaceholder(1);
	    slideBody.clearText();

	    // to get body of text we will need to use the section.txt file, this is done
	    // through getSectionBody helper
	    String bodyText = getSectionBody(inputFileLocation, i);

	    slideBody.addNewTextParagraph().addNewTextRun().setText(bodyText);

	}

	// creating an FileOutputStream object
	File file = new File(presentation.get(0).getTitle() + ".pptx");
	FileOutputStream out = new FileOutputStream(inputFileLocation + "/" + file);

	// saving the changes to a file
	ppt.write(out);
	System.out.println("Presentation created successfully");
	out.close();
	ppt.close();
    }

    public static String getSectionBody(String inputFileLocation, int i) throws IOException { // gets the sections body
											      // contents from the .txt
											      // file
	int sectionNum = i + 1; // increment section number to match section file
	File file = new File(inputFileLocation + "/section" + sectionNum + ".txt"); // open with scanner
	Scanner sc = new Scanner(file);
	String body = ""; // body string will hold body contents
	sc.nextLine(); // skip first line because that will hold the title
	while (sc.hasNextLine())// while we have not reached the end of scanner
	    body += sc.nextLine(); // add line to body and proceed
	sc.close();
	// close scanner and return body to add body to slide

	return body;
    }
}
