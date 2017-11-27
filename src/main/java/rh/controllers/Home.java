package rh.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import rh.enums.ActionType;
import rh.handlers.Common;
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

        ActionType action = null;
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/MainTPL.twig");
        JtwigModel model = JtwigModel.newModel();
        Map<String, String> formData = Common.getFormData(httpExchange);
        String path = "classpath:/" + "templates/snippets/LoggedIn.twig";
        model.with("cssPath", "static/css/loggedInStyles.css");
        model.with("templatePath", path);
        response = template.render(model);

        if (!isUserLoggedIn(httpExchange)) {
            action = ActionType.REDIRECT;
            response = "/login";

        } else {
            action = ActionType.WRITE;
        }

        if (formData.containsKey("logout")) {
            logoutThisUser(httpExchange);
            action = ActionType.REDIRECT;
            response = "/login";
        }

        System.out.println(httpExchange.getRequestURI());

        Common.manageAction(httpExchange, action, response);
    }

    private boolean isUserLoggedIn(HttpExchange httpExchange) throws IOException {
        User user = Cookie.lookForUserInLoggedUsers(httpExchange, this.loggedUsers);
        return user != null;
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
