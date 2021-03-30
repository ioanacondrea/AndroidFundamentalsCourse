package com.ioana.temaandroid.classes;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TransactionCategory implements Comparable<TransactionCategory>, Parcelable{
   // @Ignore
   // private Context context;

    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private int categoryType;
    private String categoryName;
    private String iconName;

   // @Ignore
    //private int iconID;


    public int getCategoryType() {
        return categoryType;
    }

    @Ignore
    public TransactionCategory(int categoryId, int categoryType, String categoryName, String iconName) {
        this.categoryId = categoryId;
        this.categoryType = categoryType;
        this.categoryName = categoryName;
        this.iconName = iconName;
    }

    public int getImageId(Context context, String imageName) {
        int resId  = context.getResources().getIdentifier(imageName, "drawable", context.getOpPackageName());
        return resId;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public String getIconName() {
        return iconName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public int isCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }


    @Override
    public int compareTo(TransactionCategory o) {

        if(this.getCategoryName().equals("Others")){
            return 1;
        }
        else return this.getCategoryName().compareTo(o.getCategoryName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionCategory> CREATOR = new Creator<TransactionCategory>() {
        @Override
        public TransactionCategory createFromParcel(Parcel in) {
            return new TransactionCategory(in);
        }

        @Override
        public TransactionCategory[] newArray(int size) {
            return new TransactionCategory[size];
        }
    };


    public TransactionCategory(int categoryType, String categoryName, String iconName) {
        this.categoryType = categoryType;
        this.categoryName = categoryName;
        this.iconName = iconName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeInt(this.categoryType);
        dest.writeString(this.categoryName);
        dest.writeString(this.iconName);
    }

    private TransactionCategory(Parcel source){
        this.categoryId = source.readInt();
        this.categoryType = source.readInt();
        this.categoryName = source.readString();
        this.iconName = source.readString();
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                ", categoryId=" + categoryId +
                ", categoryType=" + categoryType +
                ", Name='" + categoryName + '\'' +
                ", iconName='" + iconName + '\'' +
                '}';
    }
}


