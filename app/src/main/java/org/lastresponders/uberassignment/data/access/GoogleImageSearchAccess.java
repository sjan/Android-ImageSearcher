package org.lastresponders.uberassignment.data.access;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lastresponders.uberassignment.data.MessageFactory;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.exception.MessageException;
import org.lastresponders.uberassignment.exception.MessageSerializationException;
import org.lastresponders.uberassignment.exception.NetworkException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjan on 1/15/2015.
 */
public class GoogleImageSearchAccess {
    private static int API_MAX_FETCHSIZE = 8; //google photo api fixed to 8 results per query
    private MessageFactory messageFactory ;

    public GoogleImageSearchClient getClient() {
        return client;
    }

    public void setClient(GoogleImageSearchClient client) {
        this.client = client;
    }

    private GoogleImageSearchClient client;

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }


    public List<ImageSearchResult> searchImage (String searchTerm) {
        return this.searchImage(searchTerm, API_MAX_FETCHSIZE,0);
    }

    public List<ImageSearchResult> searchImage(String searchTerm, Integer count) {
       return this.searchImage(searchTerm,count,0);
    }

    public List<ImageSearchResult> searchImage(String searchTerm, Integer count, Integer initialStart)  {

        List<ImageSearchResult> ret = new ArrayList<ImageSearchResult>();

        int startIndex =initialStart;
        String response = null;



            do {
                if (startIndex + API_MAX_FETCHSIZE > (count + initialStart)) { //at the upper limit of the count
                    API_MAX_FETCHSIZE = count + initialStart - startIndex;
                }

                try {
                    Log.d("GoogleImageSearchAccess", "parameters s:" + searchTerm + " c:" + API_MAX_FETCHSIZE + " i:" + startIndex);
                     response = client.searchImage(searchTerm, API_MAX_FETCHSIZE, startIndex);
                    Log.d("GoogleImageSearchAccess", "response:" + response);
                    //process response
                    if(response == null) { //should never happen. check anyway
                        throw new MessageException("unexpected null response");
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());

                    if(jsonObject.has("responseStatus") && jsonObject.getString("responseStatus").equals("400")) {
                        Log.e("GoogleImageSearchAccess", "Google api limit hit!");
                        break;
                    }

                    if(jsonObject!=null && jsonObject.has("responseData") && jsonObject.getJSONObject("responseData").has("results")) {
                        JSONArray resultArray =  jsonObject.getJSONObject("responseData").getJSONArray("results");
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject obj = resultArray.getJSONObject(i);
                            ImageSearchResult result = messageFactory.imageSearchResult(obj);
                            ret.add(result);
                        }
                    }

                } catch (NetworkException e) {
                    Log.e("GoogleImageSearchAccess", "Network error for s:" + searchTerm + " c:" + API_MAX_FETCHSIZE + " i:" + startIndex + " skipped..." , e);
                } catch (MessageException e) {
                    Log.e("GoogleImageSearchAccess", "Message error for s:" + searchTerm + " c:" + API_MAX_FETCHSIZE + " i:" + startIndex + " skipped..." , e);
                } catch (MessageSerializationException e) {
                    Log.e("GoogleImageSearchAccess", "Message Serialization error for s:" + response.toString(), e);
                } catch (JSONException e) {
                    Log.e("GoogleImageSearchAccess", "Could not process json " + response.toString() , e);
                }
                startIndex = startIndex + API_MAX_FETCHSIZE; //get next block
            } while (startIndex < (count + initialStart));
        return ret;
    }
}
