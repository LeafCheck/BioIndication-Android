package com.special.ResideMenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.skoml.bioindication.R;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午11:05
 * Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout {

    /**
     * menu item  icon
     */
    private ImageView iv_icon;
    /**
     * menu item  title
     */
    private TextView tv_title;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        Drawable dr = getResources().getDrawable(icon);
        dr = DrawableCompat.wrap(dr);
        DrawableCompat.setTint(dr.mutate(), getResources().getColor(R.color.mainColor));
        iv_icon.setImageDrawable(dr);
        tv_title.setText(title);
        tv_title.setTextColor(getResources().getColor(R.color.mainColor));
    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        Drawable dr = getResources().getDrawable(icon);
        dr = DrawableCompat.wrap(dr);
        DrawableCompat.setTint(dr.mutate(), getResources().getColor(R.color.mainColor));
        iv_icon.setImageDrawable(dr);
        tv_title.setText(title);
        tv_title.setTextColor(getResources().getColor(R.color.mainColor));
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     *
     * @param title
     */
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
