package t1;
import java.io.IOException;
import java.io.File;
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

public class PDFTextStripping extends PDFStreamEngine{

	//Used in naming images. Ex. image_1, image_2, etc. It is incremented when images are extracted
	public int imageNumber = 1;
	
	public static void SaveImagesInPdf(PDDocument document, String fileName) throws IOException{
		try{
		    document = PDDocument.load(new File(fileName));
		    PDFTextStripping printer = new PDFTextStripping();
		    
		    int pageNum = 0;
		    
		    for(PDPage page : document.getPages()){
			pageNum++;
			System.out.println( "Processing page: " + pageNum );
			printer.processPage(page);
		    }
		}
		finally{
		    if( document != null ){
			document.close();
		    }
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
	
	//dk what this override is doing. It seems to do nothing when I comment it out
	//leaving it here tho.
	@Override
	protected void processOperator( Operator operator, List<COSBase> operands) throws IOException{
		String operation = operator.getName();
		//Do is the PDF operator for xobjects (like images)
		//there are a bunch of PDF operators for things like bolded text, titles, diagrams, etc.
		if( "Do".equals(operation) ){
		    COSName objectName = (COSName)operands.get( 0 );
		    PDXObject xobject = getResources().getXObject( objectName );
			
		    if( xobject instanceof PDImageXObject){
			PDImageXObject image = (PDImageXObject)xobject;
			
			// same image to project folder
			BufferedImage bImage = image.getImage();
			//you can change the name of the image here
			ImageIO.write(bImage,"PNG",new File("image_"+imageNumber+".png"));
			System.out.println("Image saved.");
			imageNumber++;
			
		    }
		    else if(xobject instanceof PDFormXObject){
			PDFormXObject form = (PDFormXObject)xobject;
			showForm(form);
		    }
		}
		else{
		    super.processOperator( operator, operands);
		}
	}
	
	public static void ExtractText(PDDocument document, String fileName) throws IOException{
	        document = PDDocument.load(new File(fileName)); 
	
	        //Creating PDFTextStripper obj
		PDFTextStripper pdfStripper = new PDFTextStripper();  
			
		//Retrieving and outputting text from PDF document  
		String text = pdfStripper.getText(document);
		System.out.println("Text in PDF\n---------------------------------");  
		System.out.println(text);  
	}
	
	public static void main(String[] args) throws IOException {
		PDDocument document = new PDDocument();
		//You can change the file to test here. Edit this file path.
	        String fileName = "C:/Users/admin/Downloads/Lizard_Research.pdf";
	        
	        //The methods. You can test each one separately if you want
	        SaveImagesInPdf(document, fileName);
	        ExtractText(document, fileName);
	        
	        document.close();
	}

}
