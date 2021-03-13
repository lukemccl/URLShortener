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
        //parsing URL
        String URL = req.getRequestURI();
        String key = "/api/Redirect/";
        String hostedURL = URL.substring(URL.lastIndexOf(key) + key.length());

        //validation of URL
        if(!hostedURL.matches("[A-Za-z0-9]*") || hostedURL.length()>40){
            sendResponse(resp,"");
        }
        //DB accessing -- change to hashmap backed to database?
        String redirectURL = DBAccess.getURL(hostedURL);

        //send response
        sendResponse(resp, redirectURL);
    }

    //update response object
    private void sendResponse(HttpServletResponse resp, String redirectURL) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(buildResponse(redirectURL));
        out.flush();
    }

    //build json of response
    private String buildResponse(String link){

        String json = "{\n";
        json += "\"link\": \"" + link + "\"\n";
        json += "}";
        return json;
    }
}
