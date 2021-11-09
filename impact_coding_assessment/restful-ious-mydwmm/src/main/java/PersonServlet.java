import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class PersonServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String requestUrl = request.getRequestURI();
        String name = requestUrl.substring("/people/".length());

        Person person = DataStore.getInstance().getPerson(name);

        if(person != null){
            String json = "{\n";
            json += "\"name\": " + JSONObject.quote(person.getName()) + ",\n";
            json += "\"owes\": " + JSONObject.quote(person.owes()) + ",\n";
            json += "\"owedBy\": " + person.owedBy() + "\n";
            json += "}";
            response.getOutputStream().println(json);
        }
        else{
            //That person wasn't found, so return an empty JSON object. We could also return an error.
            response.getOutputStream().println("{}");
        }
    }



    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String name = request.getParameter("name");
        String owes = request.getParameter("owes");
        String owedBy = request.getParameter("owedBy");

        String authHeader = request.getHeader("authorization");
        String encodedToken = authHeader.substring(authHeader.indexOf(' ')+1);
        String decodedToken = new String(Base64.getDecoder().decode(encodedToken));
        String username = TokenStore.getInstance().getUsername(decodedToken);

        //token is invalid or expired
        if(username == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String nameToEdit = request.getParameter("name");
        String about = request.getParameter("owes");
        String owedBy = request.getParameter("owedBy");


        Person loggedInPerson = DataStore.getInstance().getPerson(username);

        //don't let users edit other users
        if(!nameToEdit.equals(loggedInPerson.getName())){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        loggedInPerson.setAbout(about);
        loggedInPerson.setBirthYear(birthYear);

        //if we made it here, everything is okay, save the user
        DataStore.getInstance().putPerson(loggedInPerson);
    }
    }
}