package rh.controllers;

import com.sun.net.httpserver.HttpExchange;
import rh.models.User;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Map;
import java.util.UUID;


public class Cookie {

    public static User lookForUserInLoggedUsers(HttpExchange httpExchange, Map<UUID, User> loggedUsers) throws IOException {
        String cookies = httpExchange.getRequestHeaders().getFirst("Cookie");

        if (cookies != null) {
            String[] separatedCookies = cookies.split("; ");

            for (String cookie : separatedCookies) {
                HttpCookie parsedCookie = HttpCookie.parse(cookie).get(0);
                String name = parsedCookie.getName();

                if (name.equals("UUID")) {
                    UUID userUUID = UUID.fromString(parsedCookie.getValue());
                    return loggedUsers.getOrDefault(userUUID, null);
                }
            }
        }

        return null;
    }

    public static void setCookie(HttpExchange httpExchange, String name, UUID uuid) throws IOException {
        HttpCookie cookie = new HttpCookie(name, uuid.toString());
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }
}
