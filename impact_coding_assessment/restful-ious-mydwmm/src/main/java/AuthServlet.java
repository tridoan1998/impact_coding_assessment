import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authHeader = request.getHeader("authorization");
        String encodedAuth = authHeader.substring(authHeader.indexOf(' ')+1);
        String decodedAuth = new String(Base64.getDecoder().decode(encodedAuth));
        String username = decodedAuth.substring(0, decodedAuth.indexOf(':'));
        String password = decodedAuth.substring(decodedAuth.indexOf(':')+1);

        Person loggedInPerson = DataStore.getInstance().getPerson(username);

        //make sure user is in our data
        if(loggedInPerson == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //make sure password is valid
        //use hashed passwords in real life!
        if(!password.equalsIgnoreCase(loggedInPerson.getPassword())){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = TokenStore.getInstance().putToken(username);

        //the body of the response is just the token
        response.getOutputStream().print(token);
    }

}