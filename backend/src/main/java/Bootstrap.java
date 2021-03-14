import URLShortener.endpoints.RedirectServlet;
import URLShortener.endpoints.ShortenServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Bootstrap {

    public static void main(String[] args) throws Exception {

        //server configs
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        //Set initial server
        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        //attach servlets
        ServletContextHandler servletHandler = new ServletContextHandler ();
        servletHandler.setContextPath("/");
        servletHandler.addServlet(RedirectServlet.class, "/api/Redirect/*");
        servletHandler.addServlet(ShortenServlet.class, "/api/Shorten/");

        //enable CORS
        FilterHolder cors = servletHandler.addFilter(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,HEAD,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        // Create the server level handler list.
        HandlerList handlers = new HandlerList();
        // Make sure DefaultHandler is last (for error handling reasons)
        handlers.setHandlers(new Handler[] { servletHandler, new DefaultHandler() });

        //start server
        server.setHandler(handlers);
        server.start();
    }
}