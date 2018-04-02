package com.nyc.game.xih.pinktap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

public class GVAdapter extends BaseAdapter {

    private Context context;
    private int[] data;

    public GVAdapter(Context context, int[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.gv_layout, null);
        }
        RelativeLayout block = (RelativeLayout) view.findViewById(R.id.block);
        if (data[i] == 0) {
            block.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if (data[i] == 1) {
            block.setBackgroundColor(context.getResources().getColor(R.color.black));
        } else if (data[i] == 2) {
            block.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }
        return view;
    }
}
