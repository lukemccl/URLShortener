package URLShortener.endpoints;

import URLShortener.DBAccess.DBAccess;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class ShortenServlet extends HttpServlet {
    private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String lower = upper.toLowerCase();
    private final String nums = "0123456789";

    private final String charset = upper + lower + nums;
    private final Random random = new Random();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        URLPair urlPair = new Gson().fromJson(req.getReader(), URLPair.class);

        //TODO: validate inputs

        //set host URL
        String hostedURL = "";
        if(urlPair.prefURL.length() > 0){
            //Check prefURL not already used
            if(DBAccess.checkConflicts(urlPair.prefURL)){
                //respond that URL is taken
                sendResponse(resp, "false", "");
                return;
            }
            hostedURL = urlPair.prefURL;
        }else{
            String randURL = generateURL();

            //check URL not already used
            while(DBAccess.checkConflicts(randURL)){
                randURL = generateURL();
            }
            hostedURL = randURL;
        }

        //DB set
        String success = DBAccess.setNewURL(hostedURL, urlPair.redirectURL);

        if (!Boolean.getBoolean(success)){
            hostedURL = "";
        }

        //send response
        sendResponse(resp, success, hostedURL);
    }

    //update response object
    private void sendResponse(HttpServletResponse resp, String success, String hostedURL) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(buildResponse(success, hostedURL));
        out.flush();
    }

    //build json of response
    private String buildResponse(String success, String hostedURL){
        String json = "{\n";
        json += "\"success\": \"" + success + "\",\n";
        json += "\"hostedURL\": \"" + hostedURL + "\"\n";
        json += "}";

        return json;
    }

    //generate random URL if no preferred given
    private String generateURL(){
        int URLLength = 5;

        StringBuilder URL = new StringBuilder();
        for(int i = 0; i< URLLength; i++){
            URL.append(charset.charAt(random.nextInt(charset.length())));
        }

        return URL.toString();
    }

    //redirect, preferred URL pair
    private class URLPair{

        public String redirectURL;
        public String prefURL;

        URLPair(String redirectURL, String prefURL){
            this.redirectURL = redirectURL;
            this.prefURL = prefURL;
        }
    }
}
