# CP317-Project

PDFExtract.java Notes:
- Download pdfextraction2.zip and Import it into Eclipse as an "Archive File"
- PDFTextStripping class does not require additional JAR library downloads.
- It is a Maven project which gets its libraries from dependecies in the pom.xml file

PDFExtract.java Methods:
-SaveImagesInPdf(PDDocument document) -> Saves extracted images to specified output folder
-getNumImages() -> Returns an int, total number of images extracted
-getPage(int imageNum) -> Returns an int, page which the image was extracted from
-ExtractText(PDDocument document) -> Saves extracted text in results.txt in the specified output folder
-onlyText(PDDocument document) -> Returns a string, the entire extracted text in string form
