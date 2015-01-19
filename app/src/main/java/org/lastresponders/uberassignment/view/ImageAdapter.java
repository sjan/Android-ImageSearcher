package org.lastresponders.uberassignment.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.lastresponders.uberassignment.data.AdapterBackingImageArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjan on 1/19/2015.
 */
//adapter to hook into gridview
public class ImageAdapter extends BaseAdapter {
    public static int OBJECT_THRESHOLD = 256;

    private Context mContext;
    private String searchContext;
    private Map<Integer, ImageView> imageViewList; //keeps track of imageviews
    private AdapterBackingImageArray backingArray; //datastructure that backs the ui arrays

    public ImageAdapter(Context c, AdapterBackingImageArray array) {
        mContext = c;
        this.backingArray = array;
        imageViewList  = new HashMap<Integer,ImageView>();
    }

    public void setSearchContext (String s) {
        this.searchContext = s;
    }

    public AdapterBackingImageArray getBackingArray() {
        return backingArray;
    }

    public int getCount() {
        return backingArray.getSize(searchContext);
    }

    public ImageView getItem(int position) {
        return imageViewList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        if(convertView == null ) {
            imageView = new SquareImageView(mContext);
            imageView.defaultFormat();
        } else {
            imageView = (SquareImageView) convertView;
        }
        imageView.setImageBitmap(backingArray.getPosition(searchContext, position)); //just in case view was recycled
        imageViewList.put(position,  imageView);
        return imageView;
    }
}
