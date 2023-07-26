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
- Now will also call matchImages to match images with the section at the bottom of their page

PDFDivison.java Methods: 
- Divide(String inputFileLocation) -> Divides input file of extracted text into sections numbered counting up from 1 in the format: sectionX with x being the number
- matchImages(ArrayList<Slide> presentation) -> Alters image string on objects to add index of images in format: index space index space... to the paragraph at the bottom of the page

Slide.java notes: 
- Slide class in which each section will be an instance
- Containts int SlideNum being the numbered slide it represents (counting from 1 up)
- Also has string Title being the title of the given slide 

Slide.java Methods: 
- Slide(int SlideNum, String Title) -> constructor that sets given values for slide number and title (temp by defualt)
- setTitle(String Title) -> sets a title passed as a parameter for a given slide
- setImage(String Image) -> sets array containing image index 
- setPageNum(int PageNum) -> sets the page number for a given section 
- setSlideNum(int SlideNum) -> sets a slide number passed as a parameter for a given slide 
- getTitle() -> returns the title of the given slide
- getImage() -> returns the image array containing the images
- getSlideNum() -> returns the slide number of a given slide
- getPageNum() -> returns the page number that a section was on (if it is across two it returns the higher page number)
