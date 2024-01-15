package crolling;

public class info {
	private String title;
	private String contents;
	
	public info() {
		//
	}
	
	public info(String title,String contents) {
		this.title = title;
		this.contents = contents;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getContents() {
		return contents;
	}
	
	@Override
	public String toString(){
		 return "제목: " + title + "\n" +
	             "내용: " + contents + "\n" +
	             "--------------------------------------------";
	}

}
