package com.thuannguyen.vdeio130;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CongViecAdepter extends BaseAdapter {
    private MainActivity context;
    private  int layout;
    private List<CongViec> congViecList;

    public CongViecAdepter(MainActivity context, int layout, List<CongViec> congViecList) {
        this.context = context;
        this.layout = layout;
        this.congViecList = congViecList;
    }

    @Override
    public int getCount() {
        return congViecList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private  class Viewholder{
        TextView txtTen;
        ImageView imgdelete, imgupdate;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder holder;
        if (view == null){
            holder = new Viewholder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtTen = (TextView) view.findViewById(R.id.texttencv);
            holder.imgdelete = (ImageView) view.findViewById(R.id.imgdelete);
            holder.imgupdate = (ImageView) view.findViewById(R.id.imgupdate);
            view.setTag(holder);
        }else {
            holder = (Viewholder) view.getTag();
        }
        CongViec congViec = congViecList.get(i);
        holder.txtTen.setText(congViec.getTenCV());

        // bắt sự kiện xóa và sửa
        holder.imgupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.DialogSuaCV(congViec.getTenCV(), congViec.getIdCV());
            }
        });
        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.DialogXoaCV(congViec.getTenCV(), congViec.getIdCV());
            }
        });
        return view;
    }
}
