package rh.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import rh.dao.UserDao;
import rh.enums.ActionType;
import rh.exceptions.DAOException;
import rh.models.User;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static rh.controllers.Cookie.lookForUserInLoggedUsers;
import static rh.handlers.Common.*;

public class Login implements HttpHandler {
    private Map<UUID, User> loggedUsers;

    public Login(Map<UUID, User> loggedUsers) {
        this.loggedUsers = loggedUsers;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            handleLogin(httpExchange);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(HttpExchange httpExchange) throws IOException, DAOException {
        String response = null;

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/MainTPL.twig");
        JtwigModel model = JtwigModel.newModel();

        String path = "classpath:/" + "templates/snippets/LoginForm.twig";
        model.with("cssPath", "static/css/styles.css");
        model.with("templatePath", path);

        User user = getUserByCookie(httpExchange);
        Map<String, String> formData = getFormData(httpExchange);
        ActionType action = null;

        if (user == null) {
            boolean wasSignInSuccessfully = tryLoginUser(httpExchange, formData);

            if (wasSignInSuccessfully) {
                action = ActionType.REDIRECT;
                response = "/home";
            } else {
                action = ActionType.WRITE;
                response = template.render(model);
            }
        } else {
            action = ActionType.REDIRECT;
            response = "/home";
        }

        manageAction(httpExchange, action, response);
    }

    private User getUserByCookie(HttpExchange httpExchange) throws IOException {
        return lookForUserInLoggedUsers(httpExchange, this.loggedUsers);
    }


    private boolean tryLoginUser(HttpExchange httpExchange, Map<String, String> formData) throws DAOException, IOException {
        final String loginKey = "username";
        final String passwordKey = "password";

        if (formData.containsKey(loginKey) && formData.containsKey(passwordKey)) {
            String login = formData.get(loginKey);
            String password = formData.get(passwordKey);

            UserDao userDao = UserDao.getInstance();
            User user = userDao.getUser(login, password);

            if (user != null) {
                UUID generatedUUID = UUID.randomUUID();
                Cookie.setCookie(httpExchange, "UUID", generatedUUID);
                this.loggedUsers.put(generatedUUID, user);
                return true;
            }
        }

        return false;
    }
}
