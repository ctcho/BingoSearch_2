package searchtools;

import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;

public class SparkSearch {
//	private static ScriptEngineManager manager = new ScriptEngineManager();
//    private static ScriptEngine engine = manager.getEngineByName("js");
    private static JavaSparkContext spark;
    private static String fileName;
    private static HashMap<String, String> index;
    private static int termNo = 0;
    
    public static List<String> makeQuery(String query, String filename, JavaSparkContext spark) throws ScriptException {
    	setup(filename, spark);
    	List<String> results = evalLoop(query).collect();
    	System.out.println(results);
    	return results;
    }
    
    private static void setup(String file, JavaSparkContext sparkler) {
    	spark = sparkler;
    	fileName = file; //Most likely, the file name will be that of the inverted index folder.
    	index = new HashMap<String, String>();
    	fillHashMap();
    }
    
    private static void fillHashMap() {
    	File folder = new File(fileName);
		if (folder.isDirectory()) {
			for (File file: folder.listFiles()) {
				index.put(file.getName(), file.getAbsolutePath());
			}
		}
    }
    
    private static JavaRDD<String> evalLoop(String query) throws ScriptException {
    	String[] terms = query.split(" and ");
    JavaRDD<String> results = (terms.length > 0) ? subevaluate(terms[0]) : null;
    	for(int i = 1; i < terms.length; i++) {
    		results = results.intersection(subevaluate(terms[i]));
    	}
    	return results;
//		
//		for (int i=0; i < terms.length - 1; i++) {
//			if (terms[i].contains("(")) {
//	    		translated += construct(terms[i].substring(1, terms[i].length()-1)) + "&&";
//	    	}
//	    	else {
//	    		translated += construct(terms[i]) + "&&";
//	    	}
//		}
//		if (terms[terms.length-1].contains("(")) {
//			translated += construct(terms[terms.length-1].substring(1, terms[terms.length-1].length()-1));
//		}
//		else {
//			translated += construct(terms[terms.length-1]);
//		}
//		if (!searchDomain.isEmpty()) {
//			JavaRDD<String> results = spark.read().textFile(index.get(searchDomain.get(0))).toJavaRDD();
//			for (int j = 1; j < searchDomain.size(); j++) {
//				results = results.union(spark.read().textFile(index.get(searchDomain.get(j))).toJavaRDD());
//			}
//			results = results.filter(s -> evaluate(s, translated));
//			return results;
//		}
//		else {
//			return null;
//		}
	}
    
  
    
    
    private static JavaRDD<String> subevaluate(String term) throws ScriptException {
//		engine.put("s", s);
		String[] parts = term.split(" or ");
		List<String> filesToRead = new LinkedList<>();
		if (parts[0].startsWith("(")) parts[0] = parts[0].substring(1, parts[0].length());
		if (parts[parts.length -1].endsWith(")")) parts[parts.length-1] = parts[parts.length-1].substring(0, parts[parts.length-1].length() - 1);
		for(int i=0; i<parts.length - 1; i++) {
			if (!filesToRead.contains(parts[i].substring(0, 2) + ".txt")) {
				filesToRead.add(parts[i].substring(0, 2) + ".txt");
			}
//			String part = "term" + termNo;
//
//			engine.put(part, parts[i]);
//			if (parts[i].substring(0, 4).equals("not(")) {
//				predicate += "!(s === term" + termNo + ") || ";
//			}
//			else {
//				predicate += "(s === term" + termNo + ") || ";
//			}
//			termNo++;
			//System.out.println("Mapping in engine: " + "term" + i + " maps to " + engine.get("term" + i) + " The predicate is: " + predicate);
		}
		//The case where length = n
		if (!filesToRead.contains(parts[parts.length-1].substring(0, 2) + ".txt")) {
			filesToRead.add(parts[parts.length-1].substring(0, 2) + ".txt");
		}
//		String part = "term" + termNo;
//		engine.put(part, parts[parts.length-1]);
//		//System.out.println("Mapping in engine: " + "term" + (parts.length-1) + " maps to " + engine.get("term" + (parts.length-1)));
//		if (parts[parts.length-1].substring(0, 4).equals("not(")) {
//			predicate += "!(s === term" + termNo + ")";
//		}
//		else {
//			predicate += "(s === term" + termNo + ")";
//		}
//		final String finalPredicate = predicate;
		if (!filesToRead.isEmpty()) {
			JavaRDD<String> results = spark.textFile(index.get(filesToRead.get(0))).filter(s -> evaluate(s, parts[0])).flatMap(s -> InvertedIndexParser.parse(s).iterator());
			for (int j = 1; j < parts.length; j++) {
				final String partj = parts[j];
				results = results.union(spark.textFile(index.get(filesToRead.get(j))).filter(s -> evaluate(s, partj)).flatMap(s -> InvertedIndexParser.parse(s).iterator()));
			}
			System.out.println("Results are: " + results.collect().toString());
			return results;
		}
		else {
			return null;
		}
//		Object result = engine.eval(predicate);
//		return Boolean.TRUE.equals(result);
	}
    
    
    public static Tuple2<String,List<String>> mapping(String s){
    		String[] line = s.split(" -> ");
    		return new Tuple2<String, List<String>>(line[0].trim(), InvertedIndexParser.parse(s));
    }
    
	public static boolean evaluate(String line, String term){
//		engine.put("s", line.split(" -> ")[0]);
//		boolean tmp = Boolean.TRUE.equals(engine.eval(logic));
//		if(engine.get("s").toString().equals("vanilla")) System.out.println(tmp);
		//System.out.println("predicate: " + logic + "the term0 is: " +  engine.get("term0"));
		//if(tmp) System.out.println(engine.get("s").toString());
//		return tmp;
		if(term.length() > 4 && term.substring(0, 4).equals("not(")) {
			return !line.equals(term.trim().toLowerCase());
		}
		return line.equals(term.trim().toLowerCase());
	}

}
