package pdfextraction;

import java.util.ArrayList;
import java.util.List;

public class Slide {
	
	public int SlideNum;
	public int PageNum;
	public String Title; 
	List<Integer> Image = new ArrayList<>();

	
	// Constructor 
	public Slide(int SlideNum, int PageNum, String Title) {
		this.SlideNum = SlideNum;
		this.PageNum = PageNum;
		this.Title = Title;
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
	
	public String getTitle() {
		return this.Title;
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
	
}
