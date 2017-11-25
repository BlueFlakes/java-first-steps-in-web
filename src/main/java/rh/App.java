package rh;

import com.sun.net.httpserver.HttpServer;
import rh.handlers.Static;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {


    public static void main(String[] args) throws IOException {

        final Integer port = 80;


        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);



        server.createContext("/static", new Static());

        server.setExecutor(null);
        server.start();

        System.out.println("Server has started ont port: " + port);
    }
}
