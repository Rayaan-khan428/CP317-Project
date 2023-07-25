package pdfextraction;
public class Paragraph {
    private String header;
    private String content;

    public Paragraph() {
    }

    public Paragraph(String header, String content) {
        this.header = header;
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Paragraph{" +
                "header='" + header + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
