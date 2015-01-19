package org.lastresponders.uberassignment.exception;

import java.io.UnsupportedEncodingException;

/**
 * Created by sjan on 1/18/2015.
 */
public class MessageException extends Exception {

    public MessageException(UnsupportedEncodingException e) {
        super(e);
    }

    public MessageException(String s) {
        super(s);
    }

    public MessageException() {
        super();
    }
}
