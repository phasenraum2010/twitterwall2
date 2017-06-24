package org.woehlke.twitterwall.oodm.exceptions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.woehlke.twitterwall.oodm.exceptions.common.TwitterwallException;


/**
 * Created by tw on 17.06.17.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Controller Request Syntax Exception") //404
public class ControllerRequestParameterSyntaxException extends TwitterwallException {
    private static final long serialVersionUID = -3332292346834265371L;

    public ControllerRequestParameterSyntaxException(String path, String variable) {
        super("Controller Request Syntax Exception for path=" + path + ",variable=" + variable);
    }
}
