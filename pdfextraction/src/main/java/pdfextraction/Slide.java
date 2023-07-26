package pdfextraction;

import java.util.ArrayList;
import java.util.List;

public class Slide {
	
	public int SlideNum;
	public int PageNum;
	public String Title;
	public String Paragraph;
	List<Integer> Image = new ArrayList<>();

	
	// Constructor 
	public Slide(int SlideNum, int PageNum, String Title, String Paragraph) {
		this.SlideNum = SlideNum;
		this.PageNum = PageNum;
		this.Title = Title;
		this.Paragraph = Paragraph;
	}
	
	// Setters and Getters my fav :)
	
	public void setTitle(String Title) {
		this.Title = Title;
	}
	
	public void setImage(List<Integer> Image) {
        this.Image.addAll(Image);
    }
	
	public void setSlideNum(int SlideNum) {
		this.SlideNum = SlideNum;
	}

	public void setPageNum(int PageNum) {
		this.PageNum = PageNum;
	}

	public void setParagraph(String Paragraph) {
		this.Paragraph = Paragraph;
	}

	public List<Integer> getImage() {
		return this.Image;
	}
	
	public int getSlideNum() {
		return this.SlideNum;
	}
	
	public int getPageNum() {
		return this.PageNum;
	}

	public String getTitle() {
		return this.Title;
	}

	public String getParagraph() {
		return this.Paragraph;
	}
}
