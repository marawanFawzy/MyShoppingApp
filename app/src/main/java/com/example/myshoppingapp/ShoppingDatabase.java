package com.example.myshoppingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class ShoppingDatabase extends SQLiteOpenHelper {
    public static final String databaseName = "CustomerRegesiter";
    SQLiteDatabase CustomerRegesiter;

    public ShoppingDatabase(Context context) {
        super(context, databaseName, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Customers " +
                "(C_id Integer Primary Key AUTOINCREMENT," +
                " C_Name text not null," +
                " Username text not null ," +
                " Password text not null ," +
                " Birthdate text not null ," +
                " flag INTEGER DEFAULT 0 ," +
                " job text , gender text not null )");

        db.execSQL("Create Table Categories " +
                "(Cat_id Integer Primary Key AUTOINCREMENT ," +
                " Cat_name text not null )");

        db.execSQL("Create Table Products " +
                "(P_id Integer not null," +
                " P_name text not null ," +
                " Price Float not null ," +
                " Quantity Integer not null," +
                " Fk_Cat_id Integer ," +
                " PRIMARY KEY (P_id,P_name)," +
                " Foreign Key(Fk_Cat_id) References Categories(Cat_id))");

        db.execSQL("Create Table Orders " +
                " (O_id Integer Primary Key AUTOINCREMENT ," +
                " Order_date date ," +
                " Latitude number ," +
                " Longitude number ," +
                " name text not null ," +
                " Cust_id integer ," +
                "Foreign Key(Cust_id) References Customers(C_id) )");

        db.execSQL("create table order_details " +
                " (Ord_id integer not null," +
                " prod_ID integer not null," +
                " cat_id integer not null," +
                " quantity integer not null," +
                " PRIMARY KEY (Ord_id,prod_ID)," +
                " FOREIGN KEY(Ord_id) REFERENCES Orders(O_id)," +
                " FOREIGN KEY(cat_id) REFERENCES Categories(Cat_id)," +
                " FOREIGN KEY(prod_ID) REFERENCES Products(P_id))");

        db.execSQL("create table Cart" +
                "(pro_ID integer not null ," +
                "qty integer not null ," +
                "cat_id integer not null," +
                "PRIMARY KEY (pro_ID,cat_id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table if Exists Customers");
        db.execSQL("Drop Table if Exists Categories");
        db.execSQL("Drop Table if Exists Products");
        db.execSQL("Drop Table if Exists Orders");
        db.execSQL("Drop Table if Exists OrderDetails");
        onCreate(db);
    }

    public void addNewCustomer(String name, String usern, String pass, String bd, String job, String gender) {
        ContentValues row = new ContentValues();
        CustomerRegesiter = this.getWritableDatabase();
        row.put("C_Name", name);
        row.put("Username", usern);
        row.put("Password", pass);
        row.put("Birthdate", bd);
        row.put("job", job);
        row.put("gender", gender);

        CustomerRegesiter.insert("Customers", null, row);
        CustomerRegesiter.close();
    }

    @SuppressLint("Range")
    public Cursor CheckUser(String username, String pass) {
        CustomerRegesiter = this.getReadableDatabase();
        Cursor desireUser = CustomerRegesiter.rawQuery("select * from Customers where Username like '" + username + "' ", null);
        desireUser.moveToFirst();
        while (!desireUser.isAfterLast()) {
            if (desireUser.getString(desireUser.getColumnIndex("Password")).equals(pass)) {
                CustomerRegesiter.close();
                return desireUser;
            }
            desireUser.moveToNext();
        }
        CustomerRegesiter.close();
        return null;
    }

    public Cursor CheckUser(String username) {
        CustomerRegesiter = this.getReadableDatabase();
        Cursor desireUser = CustomerRegesiter.rawQuery("select * from Customers where Username like '" + username + "' ", null);
        desireUser.moveToFirst();
        if (desireUser.getCount() != 0) {
            desireUser.moveToFirst();
            CustomerRegesiter.close();
            return desireUser;
        }

        CustomerRegesiter.close();
        return null;
    }

    public Cursor forgetPassword(String c_uname) {
        CustomerRegesiter = getReadableDatabase();
        Cursor c = CustomerRegesiter.rawQuery("Select Username , Password from Customers where Username like '" + c_uname + "' ", null);
        if (c != null) {
            c.moveToFirst();
        }
        CustomerRegesiter.close();
        return c;
    }

    public Cursor Select_Categories() {
        CustomerRegesiter = getReadableDatabase();
        Cursor c = CustomerRegesiter.rawQuery("Select * From Categories", null);
        if (c != null) {
            c.moveToFirst();
        }
        CustomerRegesiter.close();
        // c.close();
        return c;
    }

    public Cursor Select_Products(int id) {
        CustomerRegesiter = getReadableDatabase();
        Cursor c = CustomerRegesiter.rawQuery("Select * From Products where Fk_Cat_id Like '" + id + "'", null);
        if (c != null) {
            c.moveToFirst();
        }
        CustomerRegesiter.close();
        //c.close();
        return c;
    }

    public Cursor getProductInfo(int productID, int cat_id) {
        CustomerRegesiter = getReadableDatabase();
        Cursor cursor = CustomerRegesiter.rawQuery("select * from Products where P_id like '" + productID + "' AND Fk_Cat_id like  '" + cat_id + "' ", null);
        if (cursor.getCount() != 0)
            cursor.moveToFirst();
        CustomerRegesiter.close();
        return cursor;
    }

    public void editQuantity(int id, int newQuantity) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("qty", newQuantity);
        CustomerRegesiter.update("Cart", row, "pro_ID like '" + id + "' ", null);
        CustomerRegesiter.close();
    }

    public Cursor fetchCart() {
        CustomerRegesiter = getReadableDatabase();
        String[] rowDetails = {"pro_ID", "qty", "cat_id"};
        Cursor cur = CustomerRegesiter.query("Cart", rowDetails, null, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();
        CustomerRegesiter.close();
        return cur;
    }

    public void deleteItem(int id) {
        CustomerRegesiter = getWritableDatabase();
        CustomerRegesiter.delete("Cart", "pro_ID='" + id + "'", null);
        CustomerRegesiter.close();
    }

    public int getQuantity(int id, int cat_id) {
        CustomerRegesiter = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cur = CustomerRegesiter.rawQuery("select qty from Cart where pro_ID like '" + id + "'AND cat_id like  '" + cat_id + "' ", null);
        Integer qnty = null;
        if (cur != null) {
            cur.moveToFirst();
            qnty = cur.getInt(0);
        }
        CustomerRegesiter.close();
        return qnty;
    }

    public String getProductPrice(int productID, int cat_id) {
        CustomerRegesiter = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = CustomerRegesiter.rawQuery("select Price from Products where P_id like '" + productID + "' AND Fk_Cat_id like  '" + cat_id + "' ", null);
        String price = null;
        if (cursor != null) {
            cursor.moveToFirst();
            price = cursor.getString(0);
        }
        CustomerRegesiter.close();
        return price;
    }

    public void addToCart(int id, int cat_id, int q) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("pro_ID", id);
        row.put("qty", q);
        row.put("cat_id", cat_id);
        CustomerRegesiter.insert("Cart", null, row);
        CustomerRegesiter.close();
    }

    public void OrderDetails(int ordID, int prodID, int qty, int cat_id) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("ord_ID", ordID);
        row.put("prod_ID", prodID);
        row.put("quantity", qty);
        row.put("cat_id", cat_id);

        CustomerRegesiter.insert("order_details", null, row);
        CustomerRegesiter.close();
    }

    public void CreateNewOrder(Integer custID, Date date, String Latitude, String Longitude, String name) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("Order_date", String.valueOf(date));
        row.put("Cust_id", custID);
        row.put("Latitude", Latitude);
        row.put("Longitude", Longitude);
        row.put("name", name);

        CustomerRegesiter.insert("Orders", null, row);
        CustomerRegesiter.close();
    }

    public Integer test() {
        CustomerRegesiter = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = CustomerRegesiter.rawQuery("select * from order_details ", null);
        Integer countID = cursor.getCount();
        CustomerRegesiter.close();
        return countID;
    }

    public Integer getLastOrderID() {
        CustomerRegesiter = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = CustomerRegesiter.rawQuery("select * from Orders ", null);
        Integer countID = cursor.getCount();
        CustomerRegesiter.close();
        return countID;
    }

    public Cursor Search_By_Text(String name) {
        CustomerRegesiter = getReadableDatabase();
        Cursor c = CustomerRegesiter.rawQuery("Select * From Products where P_name Like '" + name + "' ", null);
        if (c != null) {
            c.moveToFirst();
        }
        CustomerRegesiter.close();
        //c.close();
        return c;
    }


}
