package searchtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

/*
#! /usr/bin/python
import sys
wiki_id = int(sys.argv[1])
def get_wiki_file(wiki_id):
    with open("/class/cs132/wiki_ranges.csv") as f:
        for l in f:
            wiki_file, start, stop = l.split(",")
            start = int(start)
            stop = int(stop)

            if wiki_id >= start and wiki_id <= stop:
                return wiki_file
wiki_file = get_wiki_file(wiki_id)
with open("/class/cs132/wiki_csv/" + wiki_file) as f:
    for l in f:
        fields = l.split(",")
        curr_id = int(fields[0])
        if curr_id == wiki_id:
            print(fields[1])
            print(fields[2])
            print(fields[3])
            exit()
 */

public class GetWikiById {

	public static final String WIKIFOLDER = "C:\\Users\\Cameron\\Desktop\\CS132a\\wiki\\";
	public static final String WIKI_RANGES = "C:\\Users\\Cameron\\Desktop\\CS132a\\wiki_ranges.csv";

	//Cluster address: "/class/cs132/wiki_csv/"
	//Local address: "C:\\Users\\Cameron\\Desktop\\CS132a\\wiki\\"
	public static List<String[]> getDocuments(List<String> ids) {
		List<String[]> documents = new ArrayList<String[]>();
		if (!ids.isEmpty()) {
			int limit = (ids.size() < 10) ? ids.size() : 10;
			for (int id=0; id<limit; id++) {
				int wiki_id = Integer.parseInt(ids.get(id));
				String wiki_file = getWikiFile(wiki_id);
				try {
					BufferedReader br = new BufferedReader(new FileReader(WIKIFOLDER + wiki_file));
					String l = br.readLine();
			        while (l != null) {
			        	String[] fields = l.split(",");
			        	if (Integer.parseInt(fields[0]) == wiki_id)  {
			        		StringTokenizer tokenizer = new StringTokenizer(fields[3]);
//			        		String[] split = fields[3].split(" ");
			        		String snippet = "";
			        		int count =0;
			        		while(tokenizer.hasMoreTokens() && count < 50) {
			        			snippet += tokenizer.nextToken() + " ";
			        			count++;
			        		}


			        		fields[3] = snippet.substring(0, snippet.length()-1) + "...";
			        		documents.add(fields);
			        		break;
			        	}

			        	l = br.readLine();
			        }
			        br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return documents;
	}

	//cluster address: "/class/cs132/wiki_ranges.csv"
	//local address: "C:\\Users\\Cameron\\Desktop\\CS132a\\wiki_ranges.csv"
	public static String getWikiFile(int wiki_id) {
		File f = new File(WIKI_RANGES);
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(f));
			String l = br.readLine();
	        while (l != null) {
	        	String[] fields = l.split(",");
	        	String wiki_file = fields[0];
	        	int start = Integer.parseInt(fields[1]);
	        	int stop = Integer.parseInt(fields[2]);

	        	if (wiki_id >= start && wiki_id <= stop) {
	        		return wiki_file;
	        	}
	        	l = br.readLine();
	        }
	        br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
