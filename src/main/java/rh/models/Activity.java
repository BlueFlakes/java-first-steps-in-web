package rh.models;

import lombok.Getter;
import rh.enums.ActionType;

@Getter
public class Activity {
    private ActionType action;
    private String response;

    public Activity(ActionType action, String response) {
        this.action = action;
        this.response = response;
    }

}
