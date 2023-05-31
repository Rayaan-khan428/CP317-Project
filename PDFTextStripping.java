package t1;
import java.io.IOException;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFTextStripping {

	public static void main(String[] args) throws IOException {
		//Creating a PDFBox object (used to create an empty pdf)
		PDDocument doc = new PDDocument();
		
		//Creating a file object for use with text stripping
		//Change location to test with different pdfs
		File file = new File("C:/Users/admin/Downloads/help for project 2-1.pdf"); 
		//Creating a PDFBox obj (for text stripping)
        PDDocument doc2 = PDDocument.load(file); 
        
//		Adding blank pages to the empty pdf
//		for (int i=0; i<5; i++) {  
//		    //Creating a blank page   
//		    PDPage blankPage = new PDPage();  
//		  
//		    //Adding the blank page to the document  
//		    doc.addPage( blankPage );  
//		}   
//		doc.save("C:/Users/admin/Documents/Java Workspace/folder/please.pdf");
	    
        //Creating PDFTextStripper obj
		PDFTextStripper pdfStripper = new PDFTextStripper();  
		  
	    //Retrieving text from PDF document  
	    String text = pdfStripper.getText(doc2);  
	    System.out.println("Text in PDF\n---------------------------------");  
	    System.out.println(text);  
		
	    //Closing files and pdfbox objs
	    doc.close();
		doc2.close();
	}

}
