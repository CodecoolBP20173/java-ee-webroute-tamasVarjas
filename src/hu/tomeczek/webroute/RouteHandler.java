package hu.tomeczek.webroute;

import com.sun.net.httpserver.HttpExchange;

public class RouteHandler {

    @WebRoute("/")
    void onRoot (HttpExchange httpExchange) {
        String response = "This is the response for the root (\"/\") URL.";
        httpExchange.setAttribute("response", response);
    }

    @WebRoute("/test")
    void onTest (HttpExchange httpExchange) {
        String response = "This is the response for the (\"/test\") URL.";
        httpExchange.setAttribute("response", response);
    }

}
