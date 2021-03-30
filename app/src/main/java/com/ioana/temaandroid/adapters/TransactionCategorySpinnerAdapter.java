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
import com.ioana.temaandroid.adapters.RowItem;

import java.util.List;

public class TransactionCategorySpinnerAdapter extends ArrayAdapter<RowItem> {

    LayoutInflater layoutInflater;
    List<RowItem> rowList;

    public void setRowList(List<RowItem> rowList) {
        this.rowList = rowList;
    }

    public TransactionCategorySpinnerAdapter(Activity context, int iconId, int categoryName, List<RowItem> list){

        super(context,iconId,categoryName, list);
        this.rowList =list;
    }

    public RowItem getRowItem(int i){
        return rowList.get(i);
    }

    public int getPositionByName(RowItem item){
        return rowList.indexOf(item);
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

        ViewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new ViewHolder();
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = layoutInflater.inflate(R.layout.transaction_category_row, null, false);

            holder.txtTitle = rowview.findViewById(R.id.categoryTextSpinner);
            holder.imageView =  rowview.findViewById(R.id.categoryIconSpinner);
            rowview.setTag(holder);

        }else{
            holder = (ViewHolder) rowview.getTag();
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

    /*
    @Override
    public String getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = infl.inflate(R.layout.transaction_category_row, null);
        ImageView icon = convertView.findViewById(R.id.categoryIconSpinner);
        TextView text = convertView.findViewById(R.id.categoryTextSpinner);
        icon.setImageResource(icons[position]);
        text.setText(categories[position]);
        return convertView;
    }*/
}
