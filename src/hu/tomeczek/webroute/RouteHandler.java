package hu.tomeczek.webroute;

import com.sun.net.httpserver.HttpExchange;

public class RouteHandler {

    @WebRoute(method = "GET", path = "/")
    void onRootWithGet (HttpExchange httpExchange) {
        String response = "This is the \"GET\" response for the root (\"/\") URL.";
        httpExchange.setAttribute("response", response);
    }

    @WebRoute(method = "POST", path = "/")
    void onRootWithPost (HttpExchange httpExchange) {
        String response = "This is the \"POST\" response for the root (\"/\") URL.";
        httpExchange.setAttribute("response", response);
    }

    @WebRoute(method = "GET", path = "/test")
    void onTestWithGet (HttpExchange httpExchange) {
        String response = "This is the \"GET\" response for the (\"/test\") URL.";
        httpExchange.setAttribute("response", response);
    }

    @WebRoute(method = "POST", path = "/test")
    void onTestWithPost (HttpExchange httpExchange) {
        String response = "This is the \"POST\" response for the (\"/test\") URL.";
        httpExchange.setAttribute("response", response);
    }

}
