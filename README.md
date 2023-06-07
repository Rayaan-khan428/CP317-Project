# CP317-Project

PDFTextStripping.java Notes:
- Copy code from PDFTestStripping.java file and add into src folder under package in Eclipse or other IDE.
- PDFTextStripping class requires addition JAR library downloads.
- From https://pdfbox.apache.org/download.html download the following JARs in the PDFBox 2.0.28 version:
                - PDFBox standalone
                - preflight
                - xmpbox
                - pdfbox-tools
                - pdfbox-debugger
- For Windows users: Line 17 is in proper format, change directories, user name, and file names as needed.
- For Mac users: Line 17 syntax must be changes to: new File("/Users/username/Directories/File Name.pdf"); 
              ("C:" must be removed and user name must be specified rather than use of "admin")
              
        PDF has now been stripped to txt in console.
