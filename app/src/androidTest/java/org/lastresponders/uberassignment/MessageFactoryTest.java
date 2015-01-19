package org.lastresponders.uberassignment;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import junit.framework.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import org.lastresponders.uberassignment.data.MessageFactory;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.mock.MockGoogleImageSearchClient;

import java.util.List;

/**
 * Created by sjan on 1/15/2015.
 */
public class MessageFactoryTest extends ApplicationTestCase<Application> {
    public MessageFactoryTest() {
        super(Application.class);
    }



    public void testSimpleMessageSerialize() throws Exception {
        MockGoogleImageSearchClient mockClient = new MockGoogleImageSearchClient();
        MessageFactory messageFactory = new MessageFactory();
        String sampleResponse = mockClient.searchImage(MockGoogleImageSearchClient.ONE_RESULT,0,0);
        JSONObject jsonObject = new JSONObject(sampleResponse);
        JSONArray resultArray = jsonObject.getJSONObject("responseData").getJSONArray("results");

        assertEquals(resultArray.length() , 1);

        ImageSearchResult result = messageFactory.imageSearchResult(resultArray.getJSONObject(0));

        assertEquals(result.getTbUrl(), "http://t3.gstatic.com/images?q\u003dtbn:ANd9GcRCQVXkikrYeJ4Uo02l_7debTFXTrYX0g2kIEiHqROYj9Uo6-j5THK756U");
        assertEquals(result.getHeight(), 2896);
        assertEquals(result.getWidth(),1944);
        assertEquals(result.getTbHeight(), 150);
        assertEquals(result.getTbWidth(), 101);
        assertEquals(result.getUrl(), "http://upload.wikimedia.org/wikipedia/commons/4/41/Fire.JPG");

    }

    public void testMultipleResultMessageSerialize() throws Exception {
        MockGoogleImageSearchClient mockClient = new MockGoogleImageSearchClient();
        MessageFactory messageFactory = new MessageFactory();
        String sampleResponse = mockClient.searchImage(MockGoogleImageSearchClient.DEFAULT,0,0);
        JSONObject jsonObject = new JSONObject(sampleResponse);
        JSONArray resultArray = jsonObject.getJSONObject("responseData").getJSONArray("results");

        assertEquals(resultArray.length() , 8);

        for(int i=0;i<resultArray.length();i++) {
            ImageSearchResult result = messageFactory.imageSearchResult(resultArray.getJSONObject(i));

            assertEquals(result.getTbUrl(), resultArray.getJSONObject(i).getString("tbUrl"));
            assertEquals(result.getHeight(), resultArray.getJSONObject(i).getInt("height"));
            assertEquals(result.getWidth(),resultArray.getJSONObject(i).getInt("width"));
            assertEquals(result.getTbHeight(),resultArray.getJSONObject(i).getInt("tbHeight"));
            assertEquals(result.getTbWidth(),resultArray.getJSONObject(i).getInt("tbWidth"));
            assertEquals(result.getUrl(),resultArray.getJSONObject(i).getString("url"));

        }


    }
}