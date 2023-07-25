# CP317-Project

PDFExtract.java Notes:
- Extracts text and images from a PDF file
- Does not work with text-based tables yet

PDFExtract.java Methods:
- SaveImagesInPdf(PDDocument document) -> Saves extracted images to specified output folder
- getNumImages() -> Returns an int, total number of images extracted
- getPage(int imageNum) -> Returns an int, page which the image was extracted from
- ExtractText(PDDocument document) -> Saves extracted text in results.txt in the specified output folder
- onlyText(PDDocument document) -> Returns a string, the entire extracted text in string form

PDFDvision.java Notes
- First looks for formatting to divde text by being numbers, letters or roman numerials
- If no formatting is found it will divide by paragprahs with a 50 word min per section
- This ensures there is enough information to summerize for a slide

PDFDivison.java Methods: 
- Divide(String inputFileLocation) -> Divides input file of extracted text into sections numbered counting up from 1 in the format: sectionX with x being the number

Slide.java notes: 
- Slide class in which each section will be an instance
- Containts int SlideNum being the numbered slide it represents (counting from 1 up)
- Also has string Title being the title of the given slide (defualt "temp") *Still needs to be found and set*

Slide.java Methods: 
- Slide(int SlideNum, String Title) -> constructor that sets given values for slide number and title (temp by defualt)
- setTitle(String Title) -> sets a title passed as a parameter for a given slide 
- setSlideNum(int SlideNum) -> sets a slide number passed as a parameter for a given slide 
- getTitle() -> returns the title of the given slide 
- getSlideNum() -> returns the slide number of a given slide 
