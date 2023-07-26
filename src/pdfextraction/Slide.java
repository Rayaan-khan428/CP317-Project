package pdfextraction;

public class Slide {

    public int SlideNum;
    public int PageNum;
    public String Title;
    public String Image;

    // Constructor
    public Slide(int SlideNum, int PageNum, String Title, String Image) {
	this.SlideNum = SlideNum;
	this.PageNum = PageNum;
	this.Title = Title;
	this.Image = Image;
    }

    // Setters and Getters my fav :)

    public void setTitle(String Title) {
	this.Title = Title;
    }

    public void setImage(String Image) {
	this.Image = Image;
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

    public String getImage() {
	return this.Image;
    }

    public int getSlideNum() {
	return this.SlideNum;
    }

    public int getPageNum() {
	return this.PageNum;
    }

}