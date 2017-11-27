package rh;

import com.sun.net.httpserver.HttpServer;
import rh.controllers.Home;
import rh.controllers.Login;
import rh.handlers.DefaultGate;
import rh.handlers.Static;
import rh.models.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class App {
    
    public static void main(String[] args) throws IOException {

        final Integer port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        Map<UUID, User> loggedUsers = new HashMap<>();

        server.createContext("/", new DefaultGate());
        server.createContext("/login", new Login(loggedUsers));
        server.createContext("/home", new Home(loggedUsers));
        server.createContext("/static", new Static());

        server.setExecutor(null);
        server.start();

        System.out.println("Server has started on port: " + port);
    }
}
