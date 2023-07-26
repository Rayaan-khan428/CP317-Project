package pdfextraction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GUI implements ActionListener{
	
	final JFileChooser fc = new JFileChooser();
	JButton selectButton = new JButton("Select a file");
	JButton generateButton = new JButton("Generate PPT");
	JButton downloadButton = new JButton("Download PPT");
	
	JLabel title = new JLabel("PDF-to-PPT");
	JFrame frame = new JFrame();
	JLabel fileLabel = new JLabel("No File Selected");
	
	JPanel panelTitle = new JPanel();
	JPanel panelBody = new JPanel();
	JPanel panelBodyLower = new JPanel();
	
	public GUI() {
		Border borderCommon = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		Border borderBodyLower = BorderFactory.createEmptyBorder(70, 10, 10, 10);
		
		BoxLayout layoutTitle = new BoxLayout(panelTitle, BoxLayout.PAGE_AXIS);
		BoxLayout layoutBody = new BoxLayout(panelBody, BoxLayout.PAGE_AXIS);
		BoxLayout layoutBodyLower = new BoxLayout(panelBodyLower, BoxLayout.PAGE_AXIS);
		
		//Change title font and stuff here
		title.setFont(new Font("Dialog",Font.PLAIN,72));
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		selectButton.addActionListener(this);
		selectButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		fileLabel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		generateButton.addActionListener(this);
		generateButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		downloadButton.addActionListener(this);
		downloadButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		panelTitle.setBorder(borderCommon);
		panelTitle.setLayout(layoutTitle);
		panelTitle.add(title);
		
        panelBody.setLayout(layoutBody);
        panelBody.setBorder(borderCommon);
        panelBody.add(selectButton);
        panelBody.add(fileLabel);
        
        panelBodyLower.setLayout(layoutBodyLower);
        panelBodyLower.setBorder(borderBodyLower);
		
        //--------------------COLOUR STUFF---------------
        Color lightGray = Color.decode("#F2F2F2");
        Color buttonColor = Color.decode("#20C997");
        Color textColor = Color.WHITE;
        Color titleTextColor = Color.decode("#333333");

        frame.getContentPane().setBackground(lightGray);
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        generateButton.setBackground(buttonColor);
        generateButton.setForeground(textColor);
        downloadButton.setBackground(buttonColor);
        downloadButton.setForeground(textColor);
        title.setForeground(titleTextColor);
        //------------------------------------------------
        
		frame.add(panelTitle, BorderLayout.NORTH);
		frame.add(panelBody);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//To open the GUI at the center of the screen
		frame.setLocationRelativeTo(null);
		frame.setTitle("PDF-to-PPT");
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new GUI();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == selectButton) {	    	
	    	//----------------FILE NAME/PATH STUFF---------------------
	        int returnVal = fc.showOpenDialog(selectButton);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //String filePath = file.getAbsolutePath();
	            
	            selectButton.setText("Select another");
	            fileLabel.setText("Selected file: " + file.getName() + ".");
	        //----------------------------------------------------------
	            
	        //----------------GENERATE BUTTON STUFF--------------------
	            if (!frame.getContentPane().isAncestorOf(panelBodyLower)) {
                    // Add panelBodyLower to the frame only if it's not already added
                    panelBodyLower.add(generateButton);
                    frame.add(panelBodyLower);
                }
	        //---------------------------------------------------------
	            //Resizes window to fit label/new buttons if needed
	            frame.pack();
	        } else {
	        	fileLabel.setText("File selection cancelled.");
	        }
	   } 
	   
	   if(e.getSource() == generateButton) {
		   //Basically move the main method here once we have it
		   File file = fc.getSelectedFile();
           String filePath = file.getAbsolutePath();
           
		   //----------------DOWNLOAD BUTTON STUFF--------------------
		   panelBodyLower.add(Box.createRigidArea(new Dimension(0, 5))); // Vertical spacing of 10 pixels 
		   panelBodyLower.add(downloadButton);
		   //---------------------------------------------------------
		   frame.pack();
	   }
	   if(e.getSource() == downloadButton) {
		   //Add functionality
		   frame.pack();
	   }
	}
	
	

}
