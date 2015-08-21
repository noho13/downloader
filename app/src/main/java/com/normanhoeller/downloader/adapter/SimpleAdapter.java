package com.normanhoeller.downloader.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.normanhoeller.downloader.R;
import com.normanhoeller.downloader.utils.DownloadImageView;
import com.normanhoeller.downloader.utils.Thumbnail;

/**
 * Created by norman on 19/09/14.
 */
public class SimpleAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public SimpleAdapter(Context ctx) {
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Thumbnail thumbnail = (Thumbnail) getItem(position);
        Drawable item = mContext.getResources().getDrawable(thumbnail.getDrawableResourceId());
        Holder holder;
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.grid_item, parent, false);
            holder = new Holder();
            holder.imageView = (DownloadImageView) view.findViewById(R.id.image_view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }

        holder.imageView.setImageDrawable(item);

        return view;
    }

    private Thumbnail[] items = {new Thumbnail(R.drawable.ic_launcher, "http://blog.jimdo.com/wp-content/uploads/2014/01/tree-247122.jpg"),
            new Thumbnail(R.drawable.ic_launcher, "http://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Rub_al_Khali_002.JPG/2560px-Rub_al_Khali_002.JPG"),
            new Thumbnail(R.drawable.ic_launcher, "http://upload.wikimedia.org/wikipedia/commons/6/66/Agasthiyamalai_range_and_Tirunelveli_rainshadow.jpg"),
            new Thumbnail(R.drawable.ic_launcher, "http://i.dailymail.co.uk/i/pix/2014/03/11/article-2578228-1C2F7AAB00000578-557_964x641.jpg"),
            new Thumbnail(R.drawable.ic_launcher, "http://teal-blog.s3.amazonaws.com/2013/10/wave-break.jpg"),
            new Thumbnail(R.drawable.ic_launcher, "http://www.coronadoclarion.net/wp-content/uploads/2013/06/s3.jpg")};

    public static class Holder {
        public DownloadImageView imageView;
    }

}
