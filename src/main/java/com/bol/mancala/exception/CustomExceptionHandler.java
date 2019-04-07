package com.bol.mancala.exception;

import com.bol.mancala.util.Message;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import java.util.Iterator;
import java.util.logging.Logger;


public class CustomExceptionHandler extends ExceptionHandlerWrapper {


    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        this.wrapped = exceptionHandler;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        FacesContext context = FacesContext.getCurrentInstance();
        handleException(context);
        wrapped.handle();
    }


    /**
     * @param context
     * @throws Throwable
     */
    private void handleException(FacesContext context) {
        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandledExceptionQueuedEvents.hasNext()) {
            Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();
            unhandledExceptionQueuedEvents.remove();
            Throwable rootCause = unwrap(exception);

            if (rootCause instanceof MancalaException) {
                handleMancalaException(context, (MancalaException) rootCause);
                return;
            }

        }

    }

    public static <T extends Throwable> Throwable unwrap(Throwable exception, Class<T> type) {
        Throwable unwrappedException = exception;

        while (type.isInstance(unwrappedException) && unwrappedException.getCause() != null) {
            unwrappedException = unwrappedException.getCause();
        }

        return unwrappedException;
    }

    public static Throwable unwrap(Throwable exception) {
        return unwrap(unwrap(exception, FacesException.class), ELException.class);
    }

    /**
     * @param context
     * @param e       application business exception
     */
    private void handleMancalaException(FacesContext context, MancalaException e) {
        if (e.getExceptionList().size() > 0) {
            for (MancalaException mancalaException : e.getExceptionList()) {
                Message.showMessage(mancalaException.getSummary(), mancalaException.getDetail());
            }
        } else {
            Message.showMessage(e.getSummary(), e.getDetail());
        }

    }


}
