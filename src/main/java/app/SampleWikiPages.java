package app;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SampleWikiPages {
	protected static List<String[]> sampleDocs = new ArrayList<String[]>();
	
	public static List<String[]> getSampleDocs() {
		sampleDocs.clear();
		fillSample();
		return sampleDocs;
	}
	
	private static void fillSample() {
		for (int i=0; i<5; i++) {
			sampleDocs.add(new String[] {""+i, "http://en.wikipedia.org/wiki?curid=" +i, "A Wiki Page",
					"What follows is a series of letters that seek to provide the bare minimum of what could be considered"
					+ " a text string. What? You were expecting something profound, or that fake latin that you so"
					+ " often see for templates? Nope, all you're getting is me. You're welcome."});
		}
	}
}
