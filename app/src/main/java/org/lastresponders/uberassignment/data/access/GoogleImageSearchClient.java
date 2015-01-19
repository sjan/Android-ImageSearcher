package org.lastresponders.uberassignment.data.access;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.lastresponders.uberassignment.exception.MessageException;
import org.lastresponders.uberassignment.exception.NetworkException;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sjan on 1/16/2015.
 */
public class GoogleImageSearchClient {
    private static final String DEFAULT_VERSION = "1.0";
    private static final String URL = "https://ajax.googleapis.com/ajax/services/search/images";
    private static int DEFAULT_TIMEOUT = 10000;

    public String searchImage(String searchTerm, int count, int start) throws NetworkException, MessageException {
        Log.d("GoogleImageSearchClient", "search:" + searchTerm + " count:" + count + " start:" + start);
        String ret = null;
        StringBuilder queryUrl = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            queryUrl = new StringBuilder(URL + "?q=" + URLEncoder.encode(searchTerm, "utf-8") + "&v=" + DEFAULT_VERSION);
            if (searchTerm == null) {
                throw new MessageException("Null search term");
            }

            queryUrl.append("&rsz=" + count);
            queryUrl.append("&start=" + start);


            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_TIMEOUT);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

            String rawQuery = queryUrl.toString();
            Log.d("GoogleImageSearchClient", "rawQuery " + rawQuery);
            HttpGet httpGet = new HttpGet(rawQuery);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int code = httpResponse.getStatusLine().getStatusCode(); //check http code

            if(badCode(code) ) {
                throw new NetworkException("http status code " + code);
            }


            HttpEntity httpEntity = httpResponse.getEntity();



            is = httpEntity.getContent();
            reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }

            ret = sb.toString();
            Log.d("GoogleImageSearchClient", "rawResponse " + ret);

        } catch (UnsupportedEncodingException e) { //call never went out, encoding error
               throw new MessageException(e);
        } catch (ClientProtocolException e) { //error somwhere in http
            throw new NetworkException(e);
        } catch (IOException e) { //network error, try again
            throw new NetworkException(e);
        } finally {
            closeStream(reader);
            closeStream(is);
        }

        return ret;
    }

    private void closeStream(Closeable s){
        try{
            if(s!=null)s.close();
        }catch(IOException e){
            e.printStackTrace();
            Log.e("GoogleImageSearchClient", "error closing stream", e);
        }
    }

    private boolean badCode(int code) {
        if(code == HttpStatus.SC_OK ||
                code == HttpStatus.SC_ACCEPTED  ||
                code == HttpStatus.SC_NO_CONTENT) {

            return false;
        }

        return false;
    }
}
