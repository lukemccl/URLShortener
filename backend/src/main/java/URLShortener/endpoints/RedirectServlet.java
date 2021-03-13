package URLShortener.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import URLShortener.DBAccess.DBAccess;

public class RedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //validation of req
        String URL = req.getRequestURI();
        String key = "/api/Redirect/";
        String hostedURL = URL.substring(URL.lastIndexOf(key) + key.length());

        if(!hostedURL.matches("[A-Za-z0-9]*")){
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(buildResponse("false", ""));
            out.flush();
            return;
        }

        //DB accessing -- change to hashmap backed to database?
        String redirectURL = DBAccess.getURL(hostedURL);

        //check redirectURL is not error
        Boolean errorFound = false;
        if (errorFound) redirectURL = "";

        //send response
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(buildResponse(errorFound.toString(), redirectURL));
        out.flush();
    }

    private String buildResponse(String valid, String link){

        String json = "{\n";
        json += "\"valid\": \"" + valid + "\",\n";
        json += "\"link\": \"" + link + "\"\n";
        json += "}";
        return json;
    }
}
