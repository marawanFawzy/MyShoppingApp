package com.example.myshoppingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                " job text , gender text not null )");

        db.execSQL("Create Table Categories " +
                "(Cat_id Integer Primary Key AUTOINCREMENT ," +
                " Cat_name text not null )");

        db.execSQL("Create Table Products " +
                "(P_id Integer not null," +
                " P_name text not null ," +
                " Price Float not null ," +
                " Quantity Integer not null," +
                " Fk_Cat_id Integer ,"+
                " PRIMARY KEY (P_id,P_name),"+
                " Foreign Key(Fk_Cat_id) References Categories(Cat_id))");

        db.execSQL("Create Table Orders " +
                "(O_id Integer Primary Key AUTOINCREMENT ," +
                " Order_date date ," +
                "Address text ," +
                " Cust_id integer ," +
                "Foreign Key(Cust_id) References Customers(C_id) )");

        db.execSQL("create table order_details " +
                "(Ord_id integer not null," +
                "prod_ID integer not null," +
                " quantity integer not null," +
                " PRIMARY KEY (Ord_id,prod_ID)," +
                " FOREIGN KEY(Ord_id) REFERENCES Orders(O_id)," +
                " FOREIGN KEY(prod_ID) REFERENCES Products(P_id))");

        db.execSQL("create table Cart" +
                "(pro_ID integer primary key ," +
                "qty integer not null ," +
                "cat_id integer not null)" );

        ContentValues catrow = new ContentValues();
        ContentValues prodrow = new ContentValues();

        catrow.put("Cat_name", "Eye Makeup");
        db.insert("Categories", null, catrow);
        catrow = new ContentValues();
        prodrow.put("P_id", "1");
        prodrow.put("P_name", "mascara loreal");
        prodrow.put("Price", "300");
        prodrow.put("Quantity", 5);
        prodrow.put("Fk_Cat_id", 1);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "2");
        prodrow.put("P_name", "mascra maybelline");
        prodrow.put("Price", "350");
        prodrow.put("Quantity", 4);
        prodrow.put("Fk_Cat_id", 1);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "3");
        prodrow.put("P_name", "Eyeliner paradise");
        prodrow.put("Price", "100");
        prodrow.put("Quantity", 3);
        prodrow.put("Fk_Cat_id", 1);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "4");
        prodrow.put("P_name", "Eyeliner Essence");
        prodrow.put("Price", "100");
        prodrow.put("Quantity", 5);
        prodrow.put("Fk_Cat_id", 1);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "5");
        prodrow.put("P_name", "Dipliner Amanda matt");
        prodrow.put("Price", "75");
        prodrow.put("Quantity", 10);
        prodrow.put("Fk_Cat_id", 1);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "6");
        prodrow.put("P_name", "Eyeliner Balm");
        prodrow.put("Price", "250");
        prodrow.put("Quantity", 3);
        prodrow.put("Fk_Cat_id", 1);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        catrow.put("Cat_name", "Lipsticks");
        db.insert("Categories", null, catrow);
        catrow = new ContentValues();

        prodrow.put("P_id", "1");
        prodrow.put("P_name", "mac");
        prodrow.put("Price", "300");
        prodrow.put("Quantity", 2);
        prodrow.put("Fk_Cat_id", 2);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "2");
        prodrow.put("P_name", "Amanda");
        prodrow.put("Price", "50");
        prodrow.put("Quantity", 7);
        prodrow.put("Fk_Cat_id", 2);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "3");
        prodrow.put("P_name", "Essence");
        prodrow.put("Price", "60");
        prodrow.put("Quantity", 5);
        prodrow.put("Fk_Cat_id", 2);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        catrow.put("cat_name", "Foundation&highlighter");
        db.insert("Categories", null, catrow);
        catrow = new ContentValues();

        prodrow.put("P_id", "1");
        prodrow.put("P_name", "Foundation makeupforever");
        prodrow.put("Price", "500");
        prodrow.put("Quantity", 6);
        prodrow.put("Fk_Cat_id", 3);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "2");
        prodrow.put("P_name", "Foundation Maybelline fit me");
        prodrow.put("Price", "185");
        prodrow.put("Quantity", 12);
        prodrow.put("Fk_Cat_id", 3);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "3");
        prodrow.put("P_name", "highlighter makeupforever");
        prodrow.put("Price", "350");
        prodrow.put("Quantity", 6);
        prodrow.put("Fk_Cat_id", 3);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "4");
        prodrow.put("P_name", "highlighter fentybeauty");
        prodrow.put("Price", "900");
        prodrow.put("Quantity", 3);
        prodrow.put("Fk_Cat_id", 3);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "4");
        prodrow.put("P_name", "Bronzer Kiko");
        prodrow.put("Price", "500");
        prodrow.put("Quantity", 6);
        prodrow.put("Fk_Cat_id", 3);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        catrow.put("Cat_name", "Women Fashion");
        db.insert("Categories", null, catrow);
        catrow = new ContentValues();

        prodrow.put("P_id", "1");
        prodrow.put("P_name", "Off Shoulder Blouse");
        prodrow.put("Price", "300");
        prodrow.put("Quantity", 6);
        prodrow.put("Fk_Cat_id", 4);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "2");
        prodrow.put("P_name", "Formal Chemise");
        prodrow.put("Price", "350");
        prodrow.put("Quantity", 7);
        prodrow.put("Fk_Cat_id", 4);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "3");
        prodrow.put("P_name", "Pants");
        prodrow.put("Price", "270");
        prodrow.put("Quantity", 16);
        prodrow.put("Fk_Cat_id", 4);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "4");
        prodrow.put("P_name", "Skirts");
        prodrow.put("Price", "200");
        prodrow.put("Quantity", 9);
        prodrow.put("Fk_Cat_id", 4);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        catrow.put("Cat_name", "Swatches");
        db.insert("Categories", null, catrow);

        prodrow.put("P_id", "1");
        prodrow.put("P_name", "Casio");
        prodrow.put("Price", "3000");
        prodrow.put("Quantity", 6);
        prodrow.put("Fk_Cat_id", 5);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "2");
        prodrow.put("P_name", "D&G");
        prodrow.put("Price", "2000");
        prodrow.put("Quantity", 5);
        prodrow.put("Fk_Cat_id", 5);
        db.insert("Products", null, prodrow);
        prodrow = new ContentValues();

        prodrow.put("P_id", "3");
        prodrow.put("P_name", "Daniel Hicher");
        prodrow.put("Price", "3500");
        prodrow.put("Quantity", 4);
        prodrow.put("Fk_Cat_id", 5);
        db.insert("Products", null, prodrow);
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

        long ret = CustomerRegesiter.insert("Customers", null, row);
        CustomerRegesiter.close();
    }

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

    public Cursor forgetpassword(String c_uname) {
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

    public Cursor Get_SelectedProduct_details(int id)   //assuming each product has only one name
    {
        CustomerRegesiter = getReadableDatabase();
        Cursor c = CustomerRegesiter.rawQuery("Select * From Products where P_id Like '" + id + "' ", null);
        if (c != null) {
            c.moveToFirst();
        }
        CustomerRegesiter.close();
        return c;
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


    public Cursor getProductInfo(Integer productID , Integer cat_id) {
        //productID = 3 ;
        //cat_id = 5 ;
        CustomerRegesiter = getReadableDatabase();
        Cursor cursor = CustomerRegesiter.rawQuery("select * from Products where P_id like '" + productID + "' AND Fk_Cat_id like  '"+cat_id +"' ", null);
        if (cursor.getCount() != 0)
            cursor.moveToFirst();
        CustomerRegesiter.close();
        return cursor;
    }

    public void editQuantity(Integer id, Integer newQuantity) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("qty", newQuantity);
        CustomerRegesiter.update("Cart", row, "pro_ID like '" + id + "' ", null);
        CustomerRegesiter.close();
    }

    public Cursor fetchCart() {
        CustomerRegesiter = getReadableDatabase();
        String[] rowDetails = {"pro_ID", "qty" , "cat_id"};
        Cursor cur = CustomerRegesiter.query("Cart", rowDetails, null, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();
        CustomerRegesiter.close();
        return cur;
    }

    public void deleteItem(Integer id) {
        CustomerRegesiter = getWritableDatabase();
        CustomerRegesiter.delete("Cart", "pro_ID='" + id + "'", null);
        CustomerRegesiter.close();
    }

    public Integer getQuantity(Integer id) {
        CustomerRegesiter = getReadableDatabase();
        Cursor cur = CustomerRegesiter.rawQuery("select qty from Cart where pro_ID like '" + id + "' ", null);
        Integer qnty = null;
        if (cur != null) {
            cur.moveToFirst();
            qnty = cur.getInt(0);
        }
        CustomerRegesiter.close();
        return qnty;
    }

    public String getProductPrice(Integer id) {
        CustomerRegesiter = getReadableDatabase();
        Cursor cursor = CustomerRegesiter.rawQuery("select Price from Products where P_id like '" + id + "' ", null);
        String price = null;
        if (cursor != null) {
            cursor.moveToFirst();
            price = cursor.getString(0);
        }
        CustomerRegesiter.close();
        return price;
    }

    public void addtoCart(Integer id, Integer cat_id, Integer q) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("pro_ID", id);
        row.put("qty", q);
        row.put("cat_id" , cat_id);
        CustomerRegesiter.insert("Cart", null, row);
        CustomerRegesiter.close();
    }

    public void OrderDetails(Integer ordID, Integer prodID, Integer qty) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("ord_ID", ordID);
        row.put("prod_ID", prodID);
        row.put("quantity", qty);

        CustomerRegesiter.insert("order_details", null, row);
        CustomerRegesiter.close();
    }

    public void CreateNewOrder(Integer custID, String date, String address) {
        CustomerRegesiter = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("Order_date", date);
        row.put("Cust_id", custID);
        row.put("Address", address);

        CustomerRegesiter.insert("Orders", null, row);
        CustomerRegesiter.close();
    }

    public Integer getLastOrderID() {
        CustomerRegesiter = getReadableDatabase();
        Cursor cursor = CustomerRegesiter.rawQuery("select * from Orders ", null);
        Integer countID = cursor.getCount();
        CustomerRegesiter.close();
        return countID;
    }


}
