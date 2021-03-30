package com.ioana.temaandroid.adapters;

public class RowItem {

    private String categoryRowName;
    private int iconRowId;
    private int categoryIdFromDB;

    public RowItem(String categoryRowName, int iconRowId, int categoryIdFromDB) {
        this.categoryRowName = categoryRowName;
        this.iconRowId = iconRowId;
        this.categoryIdFromDB =categoryIdFromDB;
    }

    public String getCategoryRowName() {
        return categoryRowName;
    }

    public void setCategoryRowName(String categoryRowName) {
        this.categoryRowName = categoryRowName;
    }

    public int getIconRowId() {
        return iconRowId;
    }

    public void setIconRowId(int iconRowId) {
        this.iconRowId = iconRowId;
    }

    public int getCategoryIdFromDB() {
        return categoryIdFromDB;
    }

    public void setCategoryIdFromDB(int categoryIdFromDB) {
        this.categoryIdFromDB = categoryIdFromDB;
    }
}
