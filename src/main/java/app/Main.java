package app;

import static spark.Spark.*;

import org.apache.spark.sql.SparkSession;
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
import org.apache.velocity.app.Velocity;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Request;
import spark.Response;

import searchtools.*;


public class Main {
    public static void main(String[] args) {
//        get("/hello", (req, res) -> "Hello World");
    	//Remember to set master as something else! http://xcn00.cs-i.brandeis.edu[*]
    	//SparkConf conf = new SparkConf().setAppName("Spark Search").setMaster("local[*]")
    	//SparkContext context = new SparkContext(conf);
    	
    	SparkSession spark = SparkSession.builder().master("local[*]").appName("Spark Search").getOrCreate();
    	Velocity.setProperty("SET_NULL_ALLOWED", true);
    	
        get("/home", (request, response) -> {
        	Map<String, Object> model = new HashMap<>();
        	return new VelocityTemplateEngine().render(
        	        new ModelAndView(model, "/views/home.vm") //"/views/home.vm"
        	    );
        });
        
        //local testing index directory: "C:\\Users\\Cameron\\Desktop\\CS132a\\index"
        //cluster index directory: "/home/cs132g7/index"
        get("/results", (request, response) -> {
        	String query = request.queryParams("terms").toLowerCase();
        	
        	//List<String> results = SparkSearch.makeQuery(query, "/home/cs132g7/index", spark);
        	List<String> results = SparkSearch.makeQuery(query, "C:\\Users\\Cameron\\Desktop\\CS132a\\index", spark);
        	
        	List<String> idList = InvertedIndexParser.parse(results);
        	List<String[]> documents = GetWikiById.getDocuments(idList);
//        	List<String[]> documents = SampleWikiPages.getSampleDocs();
        	
        	Map<String, Object> model = new HashMap<>();
        	if (documents == null) {
        		documents = new ArrayList<String[]>();
        	}
        	model.put("documents", documents);
        	return new VelocityTemplateEngine().render(
        	        new ModelAndView(model, "/views/results.vm")
        	    );
        });
    }
}
