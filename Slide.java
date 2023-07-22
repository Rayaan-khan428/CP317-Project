package t1;

public class Slide {
	
	public int SlideNum;
	public String Title; 
	
	// Constructor 
	public Slide(int SlideNum, String Title) {
		this.SlideNum = SlideNum;
		this.Title = Title;
	}
	
	// Setters and Getters my fav :)
	
	public void setTitle(String Title) {
		this.Title = Title;
	}
	
	public void setSlideNum(int SlideNum) {
		this.SlideNum = SlideNum;
	}
	
	public String getTitle() {
		return this.Title;
	}
	
	public int getSlideNum() {
		return this.SlideNum;
	}
}
