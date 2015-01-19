package org.lastresponders.uberassignment.data;

import android.graphics.Bitmap;

import org.lastresponders.uberassignment.MainActivity;
import org.lastresponders.uberassignment.view.ImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sjan on 1/16/2015.
 */
public class AdapterBackingImageArray {
    private Set<String> urlSet;
    private Map <String , List <Bitmap>> imageMap;

    private ImageAdapter imageAdapter;

    public AdapterBackingImageArray() {
        imageMap = new HashMap<String , List <Bitmap>> ();
        urlSet = new HashSet<String>();

    }


    public int getTotalUrlCount() {
        return this.urlSet.size();
    }

    public void addImage(String searchString,
                         Bitmap bitmap, String url) {
        urlSet.add(url);
        List<Bitmap> list = imageMap.get(searchString);
        if(list == null) {
            list = new ArrayList<Bitmap>();
            imageMap.put(searchString, list);
        }
        list.add(bitmap);
    }

    public void clear() {
        urlSet.clear();
        imageMap.clear();
    }


    public int getSize(String searchContext) {
        List list = imageMap.get(searchContext);

        if(list == null)
            return 0;
        else
            return list.size();
    }

    public Bitmap getPosition(String search, int position) {
        List <Bitmap>list = imageMap.get(search);

        if(list == null || position > list.size())
            return null;
        else
            return list.get(position);
    }

    public boolean containsUrl (String url) {
        return this.urlSet.contains(url);
    }
}
