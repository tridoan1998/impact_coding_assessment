package restful-ious-mydwmm;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONObject;

public class RestApi {

    public static void main(String[] args) throws IOException{

        Scanner scanner = new Scanner(System.in);

        String getOrSet = scanner.nextLine();
        if("get".equalsIgnoreCase(getOrSet)){
            System.out.println("Enter name: ");
            String name = scanner.nextLine();

            String jsonString = getPersonData(name);
            JSONObject jsonObject = new JSONObject(jsonString);

            Builder owes = jsonObject.getPersonData("owes");

            Builder owesBy = jsonObject.getString("owesBy");
        }
        else if("set".equalsIgnoreCase(getOrSet)){
            System.out.println("Enter name: ");
            String name = scanner.nextLine();

            System.out.println("owes: ");
            Builder owes = scanner.nextLine();

            System.out.println("owned by: ");
            Builder owesBy = scanner.nextLine();

            setPersonData(name , owes, owesBy);
        }

        scanner.close();

        System.out.println("Thanks for using PICLER.");

    }

    public static String getPersonData(String name) throws IOException{

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/people/" + name).openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            String response = "";
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();

            return response;
        }

        // an error happened
        return null;
    }

    public static void setPersonData(String name, String birthYear, String about) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/people/" + name).openConnection();

        connection.setRequestMethod("POST");

        String postData = "name=" + URLEncoder.encode(name);
        postData += "&about=" + URLEncoder.encode(about);
        postData += "&birthYear=" + birthYear;

        connection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(postData);
        wr.flush();

        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            System.out.println("POST was successful.");
        }
        else if(responseCode == 401){
            System.out.println("Wrong password.");
        }
    }
}