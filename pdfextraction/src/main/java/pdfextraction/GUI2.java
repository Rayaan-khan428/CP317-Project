package pdfextraction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;
//import javax.swing.SwingConstants;
//import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class GUI2 implements ActionListener{
	final JFileChooser fc = new JFileChooser();
	private JPanel cardPanel;
    private CardLayout cardLayout;
    
    JFrame frame = new JFrame("PDF-to-PPT");
    
    JButton selectButton = new JButton("Select a file");
	JButton generateButton = new JButton("Generate PPT");
	JButton downloadButton = new JButton("Download PPT");
    JButton switchToScreen1 = new JButton("Back");
    JButton switchToScreen2 = new JButton("Next");
    
    JLabel fileLabel = new JLabel("No File Selected");
    
	JPanel panelBody = new JPanel();
	JPanel panelBodyLower = new JPanel();
	JPanel screen1 = createMainScreen("PDF-to-PPT");
    JPanel screen2 = createScreen("PDF-to-PPT");
	
    Border borderCommon = BorderFactory.createEmptyBorder(0, 10, 10, 10);
    Border borderBodyLower = BorderFactory.createEmptyBorder(70, 10, 10, 10);
    
    Color lightGray = Color.decode("#F2F2F2");
    Color buttonColor = Color.decode("#20C997");
    Color textColor = Color.WHITE;
    
    public GUI2() {
    	fc.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
    	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);


        // Add the screens to the cardPanel
        cardPanel.add(screen1, "SCREEN1");
        cardPanel.add(screen2, "SCREEN2");
        
        // Set up main buttons
		
		BoxLayout layoutBodyLower = new BoxLayout(panelBodyLower, BoxLayout.PAGE_AXIS);
		
		downloadButton.addActionListener(this);
		downloadButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		      
        panelBodyLower.setLayout(layoutBodyLower);
        panelBodyLower.setBorder(borderBodyLower);
		
        frame.getContentPane().setBackground(lightGray);
        selectButton.setBackground(buttonColor);
        selectButton.setForeground(textColor);
        generateButton.setBackground(buttonColor);
        generateButton.setForeground(textColor);
        downloadButton.setBackground(buttonColor);
        downloadButton.setForeground(textColor);
        
        
		//-------------------------------------------------------------------------
		
        // Set up buttons to switch between screens
        switchToScreen1.addActionListener(this);
        switchToScreen2.addActionListener(this);
        
        frame.getContentPane().setBackground(lightGray);
        switchToScreen1.setBackground(buttonColor);
        switchToScreen1.setForeground(textColor);
        switchToScreen2.setBackground(buttonColor);
        switchToScreen2.setForeground(textColor);        
        
        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(switchToScreen1);
        buttonPanel.add(switchToScreen2);
        

        // Add the buttonPanel to the SOUTH position
        frame.add(cardPanel, BorderLayout.NORTH);
        frame.add(Box.createRigidArea(new Dimension(0, 25)), BorderLayout.CENTER); 
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
    private JPanel createMainScreen(String title) {
    	BoxLayout layoutBody = new BoxLayout(panelBody, BoxLayout.PAGE_AXIS);
        
        Color titleTextColor = Color.decode("#333333");
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Dialog",Font.PLAIN,72));
		titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        titleLabel.setForeground(titleTextColor);
    	
    	selectButton.addActionListener(this);
		selectButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		fileLabel.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		generateButton.addActionListener(this);
		generateButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
    	
    	panelBody.setLayout(layoutBody);
        panelBody.setBorder(borderCommon);
        panelBody.add(titleLabel, BorderLayout.NORTH);
        panelBody.add(selectButton);
        panelBody.add(fileLabel);
        
    	return panelBody;
    }
    // Helper method to create a screen panel 
    private JPanel createScreen(String title) {
        JPanel panel = new JPanel();
        
        BoxLayout layoutTitle = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layoutTitle);
        
        panel.setBorder(borderCommon);
        
        Color titleTextColor = Color.decode("#333333");
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Dialog",Font.PLAIN,72));
		titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        titleLabel.setForeground(titleTextColor);
        
        generateButton.addActionListener(this);
		generateButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
    	new GUI2();
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == selectButton) {	    	
	    	//----------------FILE NAME/PATH STUFF---------------------
	        int returnVal = fc.showOpenDialog(selectButton);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            //String filePath = file.getAbsolutePath();
	            
	            Icon fileIcon = FileSystemView.getFileSystemView().getSystemIcon(file);
	            
	            //-----------------------Terrible Code, run if you want lol------------------------------------
//	            int newWidth = fileIcon.getIconWidth() * 10;
//	            int newHeight = fileIcon.getIconHeight() * 10;
//
//	            // Get the Image object from the ImageIcon
//	            Image originalImage = ((ImageIcon)fileIcon).getImage();
//
//	            // Use Image#getScaledInstance() to resize the Image
//	            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
//
//	            // Create a new ImageIcon with the resized Image
//	            ImageIcon resizedIcon = new ImageIcon(resizedImage);
	            //------------------------------------------------------------
	            
	            JLabel iconLabel = new JLabel(fileIcon);
	            //Uncomment below if running terrible code
	            //JLabel iconLabel = new JLabel(resizedIcon);
	            screen1.add(iconLabel);
	            
	            selectButton.setText("Select another");
	            switchToScreen2.setBackground(buttonColor);
	            fileLabel.setText("Selected file: " + file.getName() + ".");
	        //----------------------------------------------------------
	            
	        //----------------GENERATE BUTTON STUFF--------------------
	            if (!screen2.isAncestorOf(generateButton)) {
                    // Add panelBodyLower to the frame only if it's not already added
                    screen2.add(generateButton);
                    //frame.add(screen1);
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
		   screen2.add(Box.createRigidArea(new Dimension(0, 5))); // Vertical spacing of 10 pixels 
		   screen2.add(downloadButton);
		   //---------------------------------------------------------
		   frame.pack();
	   }
	   if(e.getSource() == downloadButton) {
		   //Add functionality
		   frame.pack();
	   
	   }
    	if (e.getSource() == switchToScreen1) {
    		cardLayout.show(cardPanel, "SCREEN1");
    	}
    	if (e.getSource() == switchToScreen2) {
    		cardLayout.show(cardPanel, "SCREEN2");
    	}
    }
	    	
}
