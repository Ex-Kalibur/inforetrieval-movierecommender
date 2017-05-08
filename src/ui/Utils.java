package ui;

public class Utils {
	public static String getRatingString(int rating){
		String star = "[*]";
		String emptyStar = "   ";
		String ratingString = "";
		
		for(int i = 5; i > 0; i--){
			if(i > rating){
				ratingString += emptyStar;
			} else {
				ratingString += star;
			}
		}
		
		return ratingString;
	}
}
