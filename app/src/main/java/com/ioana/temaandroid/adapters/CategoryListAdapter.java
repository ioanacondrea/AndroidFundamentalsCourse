package com.ioana.temaandroid.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ioana.temaandroid.R;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<RowItem> {
    LayoutInflater layoutInflater;
    List<RowItem> rowList;

    public List<RowItem> getRowList() {
        return rowList;
    }

    public void setRowList(List<RowItem> rowList) {
        this.rowList = rowList; notifyDataSetChanged();
    }

    public CategoryListAdapter(Activity context, int iconId, int categoryName, List<RowItem> list){

        super(context,iconId,categoryName, list);
        this.rowList =list;
    }

    public RowItem getRowItem(int i){
        return rowList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowView(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowView(convertView,position);
    }

    private View rowView(View convertView , int position){

        RowItem rowItem = getItem(position);

        CategoryListAdapter.ViewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new CategoryListAdapter.ViewHolder();
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = layoutInflater.inflate(R.layout.category_list_row, null, false);

            holder.txtTitle = rowview.findViewById(R.id.textListView);
            holder.imageView =  rowview.findViewById(R.id.iconListView);
            rowview.setTag(holder);

        }else{
            holder = (CategoryListAdapter.ViewHolder) rowview.getTag();
        }
        holder.imageView.setImageResource(rowItem.getIconRowId());
        holder.txtTitle.setText(rowItem.getCategoryRowName());

        return rowview;
    }


    private class ViewHolder {
        TextView txtTitle;
        ImageView imageView;
    }

    public int getCount() {
        return rowList.size();
    }
}
