package jokesAPI;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class JokesTests {

	 private static Logger Log = Logger.getLogger(JokesTests.class.getName());
	 List<String> myList = null;

	@Test (priority=1)	
	public void getCategories()
	{   
		RestAssured.baseURI = "https://api.chucknorris.io";
		DOMConfigurator.configure("log4j.xml");
		Log.info("*************************************************************");
		Log.info("                     jokes/categories                        ");


		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "/jokes/categories");


		String responseBody = response.getBody().asString();
		Assert.assertEquals(200, response.getStatusCode());
		Map<Character, Character> replacements = new HashMap<>();
	    replacements.put('[', ' ');
	    replacements.put('"', ' ');
	    replacements.put(']', ' ');

	    StringBuilder output = new StringBuilder();
	    for (Character c : responseBody.toCharArray()) {
	        output.append(replacements.getOrDefault(c, c));
	    }
	
	    String st =  output.toString().replaceAll("\\s+","");
		myList = new ArrayList<String>(Arrays.asList(st.split(",")));


		Log.info("status code is =>  " + response.getStatusCode());
		Log.info("current categories are =>  " + responseBody);
		Log.info("We have " + myList.size()+" categories");
		
		System.out.println("status code is =>  " + response.getStatusCode());
		System.out.println("current categories are =>  " + responseBody);
		System.out.println("We have " + myList.size()+" categories");
		
		Log.info("                         Enf of test1                        ");
		Log.info("*************************************************************");
	
	}

	@Test (priority=2)	
	public void getRandomCategories() throws InterruptedException
	{ 
		RestAssured.baseURI = "https://api.chucknorris.io";
		DOMConfigurator.configure("log4j.xml");
		Log.info("                                                             ");
		Log.info("                                                             ");
		Log.info("*************************************************************");
		Log.info("                       jokes/random                          ");


		//RequestSpecification httpRequest = RestAssured.given();

		for (int i=0; i< myList.size(); i++)
		{
			RequestSpecification httpRequest = RestAssured.given();
			Log.info("*************************************************************");
			Response response = httpRequest.when().queryParam("category", myList.get(i).toString()).get("/jokes/random");
			String responseBody = response.getBody().asString();
			JsonPath jsonPathEvaluator = response.jsonPath();
			
			String category = jsonPathEvaluator.get("categories").toString();;
			Assert.assertEquals(category, "["+myList.get(i).toString()+"]");

		    System.out.println("current category is =>  " + responseBody);
		    Log.info("current category is =>  " + responseBody);
		    Log.info("category in request is " + "["+myList.get(i).toString()+"]" + 
		              " and it is the same as the one in the body which is " + category);

		    Log.info("*************************************************************");

		}
		
		
		Log.info("                         Enf of test2                        ");
		Log.info("*************************************************************");

	}
	
	@Test (priority=3)	
	public void JokesChecks() throws ParseException, MalformedURLException, IOException
	{
		RestAssured.baseURI = "https://api.chucknorris.io";
		DOMConfigurator.configure("log4j.xml");
		
		Log.info("                                                             ");
		Log.info("                                                             ");
		Log.info("*************************************************************");
		Log.info("                     test #3  jokes/1BYqNs0MSzmtl9ivZikisA                          ");
		Log.info("*************************************************************");
		Log.info("                                                             ");

        // is [value] contain => [entire song]  ??

		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "/jokes/1BYqNs0MSzmtl9ivZikisA");
		JsonPath jsonPathEvaluator = response.jsonPath();
		String valueText = jsonPathEvaluator.get("value").toString();
	  
		Log.info("is [value] contain => [entire song] >>> " + valueText.contains("entire song"));
		Assert.assertTrue(valueText.contains("entire song"));
		System.out.println("is [value] contain => [entire song] ===> " + valueText.contains("entire song"));

		Log.info("*************************************************************");
		Log.info("                                                             ");
        // last Modification was later than January 2016 ??
		
		String updated_at = jsonPathEvaluator.get("updated_at").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(updated_at);
        Date date2 = sdf.parse("2016-01-01");
        Boolean lastModification= false;
        //Date1 is after Date2
        if(date1.compareTo(date2) > 0)
        {
        	lastModification = true;
        }
        	
        	
	    Log.info("last Modification was later than January 2016 >>> " + lastModification);
		Assert.assertTrue(lastModification);
	    System.out.println("last Modification was later than January 2016 >>> " + lastModification);
	    
	    Log.info("*************************************************************");
		Log.info("                                                             ");
	    // icon_url has valid image ?
		String icon_url = jsonPathEvaluator.get("icon_url");
        URL imageUrl;
        imageUrl = new URL(icon_url);
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");

        BufferedImage image = ImageIO.read(connection.getInputStream());  
	    Boolean validImage = false;
        if (image != null) {  
        	validImage = true; 
        } 
		
	    Log.info("icon_url has valid image ? >>> " + validImage);
		Assert.assertTrue(validImage);
	    System.out.println("icon_url has valid image ? >>> " + validImage);
		
		Log.info("                         End of test3                        ");
		Log.info("*************************************************************");
	}
	 
	@Test (priority=4)	
	public void NoJokesTest()
	{
		RestAssured.baseURI = "https://api.chucknorris.io";
		DOMConfigurator.configure("log4j.xml");
		
		Log.info("                                                             ");
		Log.info("                                                             ");
		Log.info("*************************************************************");
		Log.info("                     test #4  jokes/1BYqNs0MSzmtl9ivZikisAxxxxxx                           ");
		Log.info("*************************************************************");
		Log.info("                                                             ");

		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET, "/jokes/1BYqNs0MSzmtl9ivZikisAxxxxxx");
		Log.info("status code is =>  " + response.getStatusCode());
		Assert.assertEquals(404, response.getStatusCode());
	    System.out.println("response status code is >>> " + response.getStatusCode());

	    Log.info("                         End of test4                        ");
		Log.info("*************************************************************");
		 
	}
	
	

}
