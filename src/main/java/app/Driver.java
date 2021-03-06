package app;

import static spark.Spark.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
//import org.apache.velocity.app.Velocity;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import spark.Request;
import spark.Response;

import searchtools.*;


public class Driver {
    public static final String INDEXPATH = "C:\\Users\\Cameron\\Desktop\\CS132a\\index";
    public static void main(String[] args) {
//        get("/hello", (req, res) -> "Hello World");
    	//Remember to set master as something else! http://xcn00.cs-i.brandeis.edu[*]
    	SparkConf conf = new SparkConf().setAppName("Spark Search").setMaster("local[*]");
    	JavaSparkContext spark = new JavaSparkContext(conf);
    	//local implementation: SparkSession spark = SparkSession.builder()
    	//.master("local[*]").appName("Spark Search").getOrCreate();

//    	JavaSparkContext spark = JavaSparkContext.build()
//    			.master("local[*]").appName("Spark Search").getOrCreate();

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

//        	List<String> results = SparkSearch.makeQuery(query, "/home/cs132g7/index", spark);
        	List<String> results = SparkSearch.makeQuery(query, INDEXPATH , spark);

        	//List<String> idList = InvertedIndexParser.parse(results);
        	List<String[]> documents = GetWikiById.getDocuments(results);
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
