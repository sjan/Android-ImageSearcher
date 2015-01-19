package org.lastresponders.uberassignment;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.lastresponders.uberassignment.data.MessageFactory;
import org.lastresponders.uberassignment.data.access.GoogleImageSearchAccess;
import org.lastresponders.uberassignment.data.model.ImageSearchResult;
import org.lastresponders.uberassignment.mock.MockGoogleImageSearchClient;

import java.util.List;

/**
 * Created by sjan on 1/18/2015.
 */
public class GoogleSearchAccessTest extends ApplicationTestCase<Application> {
    private GoogleImageSearchAccess googleImageSearchAccess;

        public GoogleSearchAccessTest() {
        super(Application.class);

            MockGoogleImageSearchClient mockClient = new MockGoogleImageSearchClient();
            MessageFactory messageFactory = new MessageFactory();

            googleImageSearchAccess = new GoogleImageSearchAccess ();
            googleImageSearchAccess.setClient(mockClient);
            googleImageSearchAccess.setMessageFactory(messageFactory);

    }

    public void testNullInput() throws Exception {
           List<ImageSearchResult> ret = googleImageSearchAccess.searchImage(null, 0, 0);
           assertEquals(ret.size(), 0);
    }


    public void testNetworkExecptionInput() throws Exception {
        List<ImageSearchResult> ret = googleImageSearchAccess.searchImage(MockGoogleImageSearchClient.NETWORK_EXCEPTION, 0, 0);
        assertEquals(ret.size(), 0);
    }

    public void testMessageExecptionInput() throws Exception {
        List<ImageSearchResult> ret = googleImageSearchAccess.searchImage(MockGoogleImageSearchClient.MESSAGE_EXCEPTION, 0, 0);
        assertEquals(ret.size(), 0);
    }

    public void testMultipleResponse() throws Exception {
        List<ImageSearchResult> ret = googleImageSearchAccess.searchImage(MockGoogleImageSearchClient.DEFAULT, 0, 0);
        assertEquals(ret.size(), 8);
    }
}
