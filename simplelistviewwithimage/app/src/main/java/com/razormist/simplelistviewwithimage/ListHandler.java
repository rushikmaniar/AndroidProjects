package com.razormist.simplelistviewwithimage;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Comparator;

/**
 * Created by Arvin on 2/23/2018.
 */

public class ListHandler extends ArrayAdapter<String>{
    String[] name_list;
    int[] laptop;
    Context context;

    public ListHandler(Context context, String[] name_list, int[] laptop) {
        super(context, R.layout.laptop_list);
        this.name_list = name_list;
        this.laptop = laptop;
        this.context = context;
    }

    @Override
    public int getCount() {
        return name_list.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagHolder tagHolder = new TagHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.laptop_list, parent, false);
            tagHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tagHolder.tv_list = (TextView) convertView.findViewById(R.id.tv_list);
            convertView.setTag(tagHolder);
        }else{
            tagHolder = (TagHolder)convertView.getTag();
        }

        tagHolder.iv_image.setImageResource(laptop[position]);
        tagHolder.tv_list.setText(name_list[position]);
        return convertView;
    }

    static class TagHolder{
        ImageView iv_image;
        TextView tv_list;

    }


}
