package org.lastresponders.uberassignment.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.exception.MessageSerializationException;



/**
 * Created by sjan on 1/15/2015.
 */
public class MessageFactory {


    public ImageSearchResult imageSearchResult(JSONObject object) throws MessageSerializationException{
        ImageSearchResult ret = new ImageSearchResult();
        try {
            ret.setWidth(Integer.parseInt(object.getString("width")));
            ret.setHeight(Integer.parseInt(object.getString(("height"))));
            ret.setTbWidth(Integer.parseInt(object.getString("tbWidth")));
            ret.setTbHeight(Integer.parseInt(object.getString("tbHeight")));
            ret.setUrl(object.getString("url"));
            ret.setTbUrl(object.getString("tbUrl"));
        } catch (JSONException e) {
           throw new MessageSerializationException(e) ;
        }
        return ret;
    }
}
