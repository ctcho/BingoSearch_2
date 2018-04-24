package app;

import static spark.Spark.*;

import org.apache.spark.sql.SparkSession;

import java.util.List;
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
    	//Remember to set master as something else!
    	SparkSession spark = SparkSession.builder().master("local[*]").appName("Spark Search").getOrCreate();
        
        get("/home", (request, response) -> {
        	Map<String, Object> model = new HashMap<>();
        	return new VelocityTemplateEngine().render(
        	        new ModelAndView(model, "/HTML/homePage.html") //"/views/home.vm"
        	    );
        });
        
        //local testing index directory: "C:\\Users\\Cameron\\Desktop\\CS132a\\index"
        get("/results", (request, response) -> {
        	String query = request.attribute("terms");
        	List<String> results = SparkSearch.makeQuery(query, "/home/cs132g7/index", spark);
        	List<String> idList = InvertedIndexParser.parse(results);
        	List<String[]> documents = GetWikiById.getDocuments(idList);
        	Map<String, Object> model = new HashMap<>();
        	model.put("documents", documents);
        	return new VelocityTemplateEngine().render(
        	        new ModelAndView(model, "/views/results.vm")
        	    );
        });
    }
}
