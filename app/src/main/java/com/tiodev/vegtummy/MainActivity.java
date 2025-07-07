package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiodev.vegtummy.Adapter.Adaptar;
import com.tiodev.vegtummy.Model.ResModel;
import com.tiodev.vegtummy.RoomDB.AppDatabase;
import com.tiodev.vegtummy.RoomDB.User;
import com.tiodev.vegtummy.RoomDB.UserDao;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    RecyclerView recview;
    boolean connected = false;
    List<ResModel> data;
    List<User> dataFinal = new ArrayList<>();
    ImageView back;
    TextView tittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        back = findViewById(R.id.imageView2);
        tittle = findViewById(R.id.tittle);
        recview = (RecyclerView)findViewById(R.id.recview);

        // Set layout manager to recyclerView
        recview.setLayoutManager(new LinearLayoutManager(this));

        // Set recipe category title
        tittle.setText(getIntent().getStringExtra("tittle"));

        // Get database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "db_name").allowMainThreadQueries()
                .createFromAsset("database/recipe.db")
                .build();
        UserDao userDao = db.userDao();

        // Get all recipes from database
        List<User> recipes = userDao.getAll();

        // Filter category from recipes
        for(int i = 0; i<recipes.size(); i++){
            if(recipes.get(i).getCategory().contains(getIntent().getStringExtra("Category"))){
                dataFinal.add(recipes.get(i));
            }
        }

        // Set category list to adapter
        Adaptar adapter = new Adaptar(dataFinal, getApplicationContext());
        recview.setAdapter(adapter);

        back.setOnClickListener(v -> finish());


    }

}

//package com.tiodev.vegtummy;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tiodev.vegtummy.Adapter.Adaptar;
//import com.tiodev.vegtummy.Model.ResModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    RecyclerView recview;
//    List<ResModel> dataFinal = new ArrayList<>();
//    ImageView back;
//    TextView tittle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        back = findViewById(R.id.imageView2);
//        tittle = findViewById(R.id.tittle);
//        recview = findViewById(R.id.recview);
//        recview.setLayoutManager(new LinearLayoutManager(this));
//
//        tittle.setText(getIntent().getStringExtra("tittle"));
//
//        DBHelper dbHelper = new DBHelper(this);
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM recipes", null);
//
//        String filterCategory = getIntent().getStringExtra("Category");
//        while (cursor.moveToNext()) {
//            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
//            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
//            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
//
//            if (category.contains(filterCategory)) {
//                dataFinal.add(new ResModel(title, category, description));
//            }
//        }
//        cursor.close();
//
//        Adaptar adapter = new Adaptar(dataFinal, getApplicationContext());
//        recview.setAdapter(adapter);
//
//        back.setOnClickListener(v -> finish());
//    }
//
//    static class DBHelper extends SQLiteOpenHelper {
//        private static final String DB_NAME = "recipes.db";
//        private static final int DB_VERSION = 1;
//
//        public DBHelper(MainActivity context) {
//            super(context, DB_NAME, null, DB_VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE IF NOT EXISTS recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, category TEXT, description TEXT)");
//            insertInitialData(db);
//        }
//
//        private void insertInitialData(SQLiteDatabase db) {
//            insertDish(db, "Gỏi cuốn", "Salad", "Món ăn nhẹ, dễ làm.");
//            insertDish(db, "Cơm tấm", "Main Dish", "Món ăn đặc sản miền Nam.");
//            insertDish(db, "Chè ba màu", "Dessert", "Món tráng miệng mát lạnh.");
//        }
//
//        private void insertDish(SQLiteDatabase db, String title, String category, String description) {
//            ContentValues values = new ContentValues();
//            values.put("title", title);
//            values.put("category", category);
//            values.put("description", description);
//            db.insert("recipes", null, values);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS recipes");
//            onCreate(db);
//        }
//    }
//}