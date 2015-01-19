package org.lastresponders.uberassignment.exception;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by sjan on 1/18/2015.
 */
public class NetworkException extends Exception {

    public NetworkException(IOException e) {
        super(e);
    }

    public NetworkException() {
        super();
    }

    public NetworkException(String s) {
        super(s);
    }
}
