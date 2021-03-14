package URLShortener.endpoints;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import URLShortener.DBAccess.DBAccess;

public class RedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //parsing URL
        String URL = req.getRequestURI();
        String key = "/api/Redirect/";
        String hostedURL = URL.substring(URL.lastIndexOf(key) + key.length());

        System.out.println("REDIREC request received  @ " +hostedURL);

        //validation of URL (matches client-side input validation)
        if(!hostedURL.matches("^[A-Za-z0-9]+$") || hostedURL.length()>40){
            System.out.println("REDIREC request FAILED    @ " +hostedURL);

            sendResponse(resp,"");
            return;
        }
        //DB accessing -- possibly change to hashmap backed to database for faster recent requests?
        String redirectURL = DBAccess.getURL(hostedURL);

        System.out.println("REDIREC request fulfilled @ " +redirectURL);

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
