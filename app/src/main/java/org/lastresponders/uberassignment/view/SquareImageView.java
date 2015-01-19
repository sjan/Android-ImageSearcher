package org.lastresponders.uberassignment.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by sjan on 1/18/2015.
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    //convenience method to format imageView
    public void defaultFormat() {
        this.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        this.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.setPadding(3, 3, 3, 3);
        this.setCropToPadding(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();

        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = widthMeasureSpec;
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}