package URLShortener.endpoints;

import URLShortener.DBAccess.DBAccess;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class ShortenServlet extends HttpServlet {
    //conditions for URL generation -- could be changed to environment variables supplied from cmd line
    private final int URLLength = 5; //916132832 available combinations with specified charset

    private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String lower = upper.toLowerCase();
    private final String nums = "0123456789";

    private final String charset = upper + lower + nums; //charset for random URLS
    private final Random random = new Random();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //read from request
        URLPair urlPair = new Gson().fromJson(req.getReader(), URLPair.class);

        //Validation done client-side
        System.out.println("SHORTEN request received  @ pref: " + urlPair.prefURL + ", redirect: " +urlPair.redirectURL);

        //send to DB
        String hostedURL;
        if(urlPair.prefURL.length() > 0){
            //attempt prefURL set
            if(!DBAccess.setNewURL(urlPair.prefURL, urlPair.redirectURL)){
                //respond that URL is taken
                System.out.println("SHORTEN request FAILED    @ host: " + urlPair.prefURL + ", redirect: " +urlPair.redirectURL);

                sendResponse(resp, "");
                return;
            }
            hostedURL = urlPair.prefURL;
        }else{
            String randURL = generateURL();

            //retry random URLs until success
            while(!DBAccess.setNewURL(randURL, urlPair.redirectURL)){
                randURL = generateURL();
            }
            hostedURL = randURL;
        }

        System.out.println("SHORTEN request fulfilled @ host: " + hostedURL + ", redirect: " +urlPair.redirectURL);

        //send response
        sendResponse(resp, hostedURL);
    }

    //update response object
    private void sendResponse(HttpServletResponse resp, String hostedURL) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.addHeader("Access-Control-Allow-Headers","content-type");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(buildResponse(hostedURL));
        out.flush();
    }

    //build json of response
    private String buildResponse(String hostedURL){
        String json = "{\n";
        json += "\"hostedURL\": \"" + hostedURL + "\"\n";
        json += "}";

        return json;
    }

    //generate random URL if no preferred given
    private String generateURL(){
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
