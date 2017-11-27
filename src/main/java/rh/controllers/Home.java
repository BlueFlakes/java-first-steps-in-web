package rh.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import rh.enums.ActionType;
import rh.handlers.Common;
import rh.models.Activity;
import rh.models.User;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Home implements HttpHandler {
    private Map<UUID, User> loggedUsers;

    public Home(Map<UUID, User> loggedUsers) {
        this.loggedUsers = loggedUsers;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/Home.twig");
        JtwigModel model = JtwigModel.newModel();
        Map<String, String> formData = Common.getFormData(httpExchange);
        String uriPath = httpExchange.getRequestURI().getPath();
        Activity activity;

        if (!isUserLoggedIn(httpExchange)) {
            activity = new Activity(ActionType.REDIRECT, "/login");

        } else if (!uriPath.equals("/home")) {
            activity = new Activity(ActionType.REDIRECT, "/home");

        } else {
            activity = handleLogoutRequest(httpExchange, formData);

            if (activity == null) {
                activity = new Activity(ActionType.WRITE, template.render(model));
            }
        }

        Common.manageAction(httpExchange, activity);
    }

    private boolean isUserLoggedIn(HttpExchange httpExchange) throws IOException {
        User user = Cookie.lookForUserInLoggedUsers(httpExchange, this.loggedUsers);
        return user != null;
    }

    private Activity handleLogoutRequest(HttpExchange httpExchange, Map<String, String> formData) throws IOException {
        if (formData.containsKey("logout")) {
            logoutThisUser(httpExchange);
            return new Activity(ActionType.REDIRECT, "/login");
        }

        return null;
    }

    private void logoutThisUser(HttpExchange httpExchange) throws IOException {
        User loggedUser = Cookie.lookForUserInLoggedUsers(httpExchange, this.loggedUsers);

        for (Map.Entry<UUID, User> user : this.loggedUsers.entrySet()) {
            User foundUser = user.getValue();

            if (foundUser == loggedUser) {
                this.loggedUsers.remove(user.getKey());
            }
        }
    }
}
