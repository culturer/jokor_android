package vip.jokor.im.pages.main.main_page.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUtil {
	private static final SearchUtil ourInstance = new SearchUtil();
	
	public static SearchUtil getInstance() {
		return ourInstance;
	}
	
	private SearchUtil() {
	
	}
	
	
	public boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
	
}
