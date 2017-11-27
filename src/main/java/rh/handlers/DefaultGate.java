package rh.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static rh.handlers.Common.redirect;

public class DefaultGate implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        redirect(httpExchange, "/login");
    }
}
