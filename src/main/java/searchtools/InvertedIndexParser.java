package searchtools;

import java.util.List;
import java.util.ArrayList;

public class InvertedIndexParser {
	public static List<String> parse(List<String> index) {
		List<String> idList = new ArrayList<String>();
		for (String entry:index) {
    		String[] keyValue = entry.split(" -> "); //splits into word and {docIDs=[indices]}
			String[] indices = keyValue[1].substring(1, keyValue[1].length()-1).split("],"); //splits {docIDs=[indices]}
			//into docID, [indices]
			
			for (String i : indices) {
				String[] units = i.split("="); //splits docID, indices into docID and [indices]
				idList.add(units[0].trim());
			}
    	}
		return idList;
	}
	public static List<String> parse(String line){
		List<String> idList = new ArrayList<String>();
		String[] keyValue = line.split(" -> ");
		String[] indices = keyValue[1].substring(1, keyValue[1].length()-1).split("],");
		for (String i : indices) {
			String[] units = i.split("="); //splits docID, indices into docID and [indices]
			idList.add(units[0].trim());
		}
		return idList;
	}
}
