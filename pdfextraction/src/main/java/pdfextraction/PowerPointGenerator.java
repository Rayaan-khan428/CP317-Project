package pdfextraction;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.pdfbox.io.IOUtils;
import org.apache.poi.common.usermodel.PictureType;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.*;

public class PowerPointGenerator {

    // enumeration holds the three types of possible scripts on a ppt
    enum Script {
        LAT, // Latin style script
        EA,  // Asian style script
        CS    // complex style script
    }

    /**
     * Method Name: creates powerpoint in inputFileLocation
     * Description: given inputFileLocation and presentation array of slides
     * this method creates a power point in File Location
     *
     * @return
     */
    public static void create(ArrayList<Slide> presentation, String projectRoot) throws IOException {
        //creating a new empty slide show
        XMLSlideShow ppt = new XMLSlideShow();

        //load slide master info at index 0 this will allow you to select layout options
        XSLFSlideMaster slideOptions = ppt.getSlideMasters().get(0);

        //we first need to change the fonts of the current master theme
        XSLFTheme theme = slideOptions.getTheme();

        //set font to Times New Roman
        setFont(theme, Script.LAT, "Times New Roman");

        // from the layout options we wish to use TITLE layout for the first slide
        XSLFSlideLayout titleForm = slideOptions.getLayout(SlideLayout.TITLE);

        // now we have loaded the layout, we create a new slide with said layout
        XSLFSlide slide1 = ppt.createSlide(titleForm);
        Color myColour = new Color(169, 191, 193);
        slide1.getBackground().setFillColor(myColour);

        //add a title to the title slide from the first slide object in presentation array list
        XSLFTextShape title1 = slide1.getPlaceholder(0);
        title1.setText(presentation.get(0).getTitle());

        //set subtitle to empty
        XSLFTextShape subtitle1 = slide1.getPlaceholder(1);
        subtitle1.setText("");


        // For every section we create one slide
        for (int i = 1; i < presentation.size(); i++) {

            // Get title and body format
            XSLFSlideLayout titleAndBody = slideOptions.getLayout(SlideLayout.TITLE_AND_CONTENT);

            // Create new slide called midSlide with titleAndBody format
            XSLFSlide midSlide = ppt.createSlide(titleAndBody);
            midSlide.getBackground().setFillColor(myColour);

            // Set title and body of slide to text from that slide object/section.txt file
            XSLFTextShape slideTitle = midSlide.getPlaceholder(0);
            slideTitle.setText(presentation.get(i).getTitle());
            XSLFTextShape slideBody = midSlide.getPlaceholder(1);
            slideBody.clearText();

            // To get body of text we will need to use the section.txt file, this is done through getSectionBody helper
            String bodyText = presentation.get(i).getParagraph();
            slideBody.addNewTextParagraph().addNewTextRun().setText(bodyText);

            // There are images to insert at the bottom of the section
//            if (presentation.get(i).getImage().size() != 0) {
//
//                for (int j = 0; j < presentation.get(i).getImage().size(); j++) {
//                    XSLFSlide imgSlide = ppt.createSlide();
//                    int imageNum = presentation.get(i).getImage().get(j) + 1;
//                    File image = new File(inputFileLocation +  "//image_" + imageNum + ".png");
//                    byte[] picture = IOUtils.toByteArray(new FileInputStream(image));
//                    XSLFPictureData idx = ppt.addPicture(picture, XSLFPictureData.PictureType.PNG);
//
//                    XSLFPictureShape pic = imgSlide.createPicture(idx);
//
//                }
//            }
        }

        // Saving powerpoint in src/output
        File file = new File(presentation.get(0).getTitle() + ".pptx");
        FileOutputStream out = new FileOutputStream(projectRoot + "/output/" + file);

        // saving the changes to a file
        ppt.write(out);
        System.out.println("Presentation created successfully");
        out.close();
        ppt.close();
    }

    /**
     * Method Name: setFont
     * Description: Sets the major and minor font to fontname in the theme
     * passed as theme.
     *
     * @return
     */
    static void setFont(XSLFTheme theme, Script scriptType, String fontName) {

        //load information from theme including the major and minor fonts
        CTOfficeStyleSheet styleSheet = theme.getXmlObject();
        CTBaseStyles themeElements = styleSheet.getThemeElements();
        CTFontScheme fontScheme = themeElements.getFontScheme();

        CTFontCollection fontMajor = fontScheme.getMajorFont();
        CTFontCollection fontMinor = fontScheme.getMinorFont();

        CTTextFont majorFont = null;
        CTTextFont minorFont = null;
        if (scriptType == Script.LAT) {
            majorFont = fontMajor.getLatin();
            majorFont.setTypeface(fontName);
            minorFont = fontMinor.getLatin();
            minorFont.setTypeface(fontName);
        } else if (scriptType == Script.EA) {
            majorFont = fontMajor.getEa();
            majorFont.setTypeface(fontName);
            minorFont = fontMinor.getEa();
            minorFont.setTypeface(fontName);
        } else if (scriptType == Script.CS) {
            majorFont = fontMajor.getCs();
            majorFont.setTypeface(fontName);
            minorFont = fontMinor.getCs();
            minorFont.setTypeface(fontName);
        }
    }


    /**
     * Method Name: setMinorFont
     * Description: Sets the minor font to fontname in the theme
     * passed as theme.
     *
     * @return
     */
    static void setMinorFont(XSLFTheme theme, Script scriptType, String fontName) {

        //load information from theme including minor font
        CTOfficeStyleSheet styleSheet = theme.getXmlObject();
        CTBaseStyles themeElements = styleSheet.getThemeElements();
        CTFontScheme fontScheme = themeElements.getFontScheme();
        CTFontCollection fontCollection = fontScheme.getMinorFont();
        CTTextFont textFont = null;
        if (scriptType == Script.LAT) {
            textFont = fontCollection.getLatin();
            textFont.setTypeface(fontName);
        } else if (scriptType == Script.EA) {
            textFont = fontCollection.getEa();
            textFont.setTypeface(fontName);
        } else if (scriptType == Script.CS) {
            textFont = fontCollection.getCs();
            textFont.setTypeface(fontName);
        }
    }

}






