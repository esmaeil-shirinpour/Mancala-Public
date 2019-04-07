package com.bol.mancala.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Message {

    /**
     * Creates message for showing in UI.
     */
    public static void showMessage(String summery,String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(summery, detail));
    }
}
