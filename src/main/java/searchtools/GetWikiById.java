package searchtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.List;
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
	
	public static List<String[]> getDocuments(List<String> ids) {
		List<String[]> documents = new ArrayList<String[]>();
		for (String id: ids) {
			int wiki_id = Integer.parseInt(id);
			String wiki_file = getWikiFile(wiki_id);
			try {
				BufferedReader br = new BufferedReader(new FileReader("/class/cs132/wiki_csv/" + wiki_file));
				String l = br.readLine();
		        while (l != null) {
		        	String[] fields = l.split(",");
		        	documents.add(fields);
		        	l = br.readLine();
		        }
		        br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return documents;
	}
	
	public static String getWikiFile(int wiki_id) {
		File f = new File("/class/cs132/wiki_ranges.csv");
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
