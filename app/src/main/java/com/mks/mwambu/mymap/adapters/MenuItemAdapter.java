package com.mks.mwambu.mymap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mks.mwambu.mymap.R;
import com.mks.mwambu.mymap.menu.MenuItem;

import java.util.List;

public class MenuItemAdapter extends BaseAdapter {
    private Context context;
    private List<MenuItem> menuItemList;
    private LayoutInflater inflater;

    public MenuItemAdapter(Context context,List<MenuItem> menuItemList){
        this.context = context;
        this.menuItemList = menuItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menuItemList.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return menuItemList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listeitem,null);
        MenuItem curent = getItem(i);
        String itmname = curent.getName();

        TextView itmNameView = view.findViewById(R.id.contact);
        itmNameView.setText(itmname);
        return view;
    }
}
