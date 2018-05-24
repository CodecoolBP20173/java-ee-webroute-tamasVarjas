package hu.tomeczek.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class RequestHandler implements HttpHandler {

    private Method getRelevantRouteHandlerMethod(String route, String requestMethod, Class requestHandler) {
        return Stream.of(requestHandler.getDeclaredMethods())
                .filter(handlerMethod -> handlerMethod.isAnnotationPresent(WebRoute.class))
                .filter(handlerMethod -> isCurrentRoute(route, requestMethod, handlerMethod))
                .findFirst()
                .orElse(null);
    }

    private boolean isCurrentRoute(String route, String requestMethod, Method handlerMethod) {
        return handlerMethod.getAnnotation(WebRoute.class).path().equals(route)
                && handlerMethod.getAnnotation(WebRoute.class).method().equals(requestMethod);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Class requestHandler = RouteHandler.class;

        // get the route from which the request was received
        String requestRoute = httpExchange.getRequestURI().toString();
        String requestMethod = httpExchange.getRequestMethod();

        // get the necessary method for the current route
        Method routeHandler = getRelevantRouteHandlerMethod(requestRoute, requestMethod, requestHandler);
        if (routeHandler == null) {
            sendResponse(
                    httpExchange,
                    "The specified route does not exist on this server or cannot be accessed with this request method."
            );
            return;
        }

        // invoke the necessary method
        try {
            routeHandler.invoke(requestHandler.newInstance(), httpExchange);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }

        //get and send the relevant response
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

