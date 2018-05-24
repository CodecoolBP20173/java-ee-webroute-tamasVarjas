package hu.tomeczek.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler implements HttpHandler {

    private Method getMethodByRoute(String route, Class requestHandler) {
        for (Method method : requestHandler.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                if (isCurrentRoute(route, method)) {
                    return method;
                }
            }
        }
        return null;
    }

    private boolean isCurrentRoute(String route, Method method) {
        return method.getAnnotation(WebRoute.class).value().equals(route);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Class requestHandler = RouteHandler.class;

        // get the route from which the request was received
        String requestRoute = httpExchange.getRequestURI().toString();

        // get the necessary method for the current route
        Method routeHandler = getMethodByRoute(requestRoute, requestHandler);
        if (routeHandler == null) {
            sendResponse(httpExchange, "The specified route does not exist on this server.");
            return;
        }

        // invoke the necessary method
        try {
            routeHandler.invoke(requestHandler.newInstance(), httpExchange);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }

        //get and send the necessary response
        String response = (String) httpExchange.getAttribute("response");
        sendResponse(httpExchange, response);
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}

