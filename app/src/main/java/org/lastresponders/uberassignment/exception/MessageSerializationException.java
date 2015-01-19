package org.lastresponders.uberassignment.exception;

import org.json.JSONException;

/**
 * Created by sjan on 1/18/2015.
 */
public class MessageSerializationException extends Exception {
    public MessageSerializationException(JSONException e) {
        super(e);
    }
}
