package URLShortener.endpoints;

import URLShortener.DBAccess.DBAccess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.stream.Collectors;

public class ShortenServlet extends HttpServlet {
    private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String lower = upper.toLowerCase();
    private final String nums = "0123456789";

    private final String charset = upper + lower + nums;
    private final Random random = new Random();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO: validate inputs
        String body = req.getReader().lines().collect(Collectors.joining());

        System.out.println(body);

        //TODO: get inputs to java
        String redirectURL = "";
        String prefURL = "";

        //set host URL
        String hostedURL = "";
        if(prefURL != null){
            //Check prefURL not already used
            if(DBAccess.checkConflicts(prefURL)){
                //respond that URL is taken
                PrintWriter out = resp.getWriter();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                out.print(buildResponse("false", ""));
                out.flush();
                return;
            }

            hostedURL = prefURL;
        }else{
            String randURL = generateURL();

            //check URL not already used
            while(DBAccess.checkConflicts(randURL)){
                randURL = generateURL();
            }
            hostedURL = randURL;
        }

        //DB set
        String success = DBAccess.setNewURL(hostedURL, redirectURL);
        if (!Boolean.getBoolean(success)){
            hostedURL = "";
        }

        //send response
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(buildResponse(success, hostedURL));
        out.flush();
    }

    private String buildResponse(String success, String hostedURL){
        String json = "{\n";
        json += "\"success\": \"" + success + "\",\n";
        json += "\"hostedURL\": \"" + hostedURL + "\"\n";
        json += "}";

        return json;
    }

    private String generateURL(){
        int URLLength = 5;

        StringBuilder URL = new StringBuilder();
        for(int i = 0; i< URLLength; i++){
            URL.append(charset.charAt(random.nextInt(charset.length())));
        }

        return URL.toString();
    }
}
