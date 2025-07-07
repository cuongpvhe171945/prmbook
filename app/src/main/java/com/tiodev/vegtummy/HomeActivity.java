package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.tiodev.vegtummy.Adapter.AdapterPopular;
import com.tiodev.vegtummy.Model.ResModel;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    class DBHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "recipes.db";
        private static final int DB_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS recipes (code INTEGER PRIMARY KEY AUTOINCREMENT, img TEXT, title TEXT, category TEXT ,tag TEXT, description TEXT, time TEXT)");
            insertInitialData(db);
        }

        private void insertInitialData(SQLiteDatabase db) {
            String des =
                    "1. Luộc thịt heo với ít muối trong khoảng 20 phút. Vớt ra ngâm nước đá rồi thái lát mỏng.\n" +
                            "2. Luộc tôm khoảng 2 phút đến khi chín. Bóc vỏ, bỏ chỉ lưng và có thể chẻ đôi.\n" +
                            "3. Chần bún khô trong nước sôi 3–5 phút, sau đó xả nước lạnh cho ráo.\n" +
                            "4. Rửa sạch rau thơm, xà lách, hẹ. Cắt dưa leo và cà rốt thành sợi.\n" +
                            "5. Làm nước chấm: phi tỏi, thêm đường, nước luộc thịt, bơ, nêm tương. Nấu sệt, thêm ớt và đậu phộng.\n" +
                            "6. Trải bánh tráng, xếp thịt, tôm, bún, rau củ và rau thơm rồi cuốn chặt tay.\n" +
                            "7. Dùng kèm với nước chấm và thưởng thức khi còn tươi ngon.";
   String des2 =
                    "1. lăn bột.\n" +
                            "2. Luộc tôm khoảng 2 phút đến khi chín. Bóc vỏ, bỏ chỉ lưng và có thể chẻ đôi.\n" +
                            "3. Chần bún khô trong nước sôi 3–5 phút, sau đó xả nước lạnh cho ráo.\n" +
                            "4. Rửa sạch rau thơm, xà lách, hẹ. Cắt dưa leo và cà rốt thành sợi.\n" +
                            "5. Làm nước chấm: phi tỏi, thêm đường, nước luộc thịt, bơ, nêm tương. Nấu sệt, thêm ớt và đậu phộng.\n" +
                            "6. Trải bánh tráng, xếp thịt, tôm, bún, rau củ và rau thơm rồi cuốn chặt tay.\n" +
                            "7. Dùng kèm với nước chấm và thưởng thức khi còn tươi ngon.";

            insertDish(db, "https://cdn.apartmenttherapy.info/image/upload/f_auto,q_auto:eco,c_fill,g_auto,w_1456,h_1092/k%2FPhoto%2FSeries%2F2020-07-medi-monday-one-pot-eggplant-caponata-pasta%2FKitchn_MediMonday_Eggplant_1","Gỏi cuốn", "Popular", "low_salt", des, "20 min");
            insertDish(db, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXawXk-cdl2WUmMvAtFmtP6_MX2jYfJo0OVtxX0UxjFhFj8_Mq-kAfXS5SzJNQxlzAzYRiPA9baZPjEJ3e1FaJYv50xCLl1T-Ir0M5xKcf","Salad hoa quả", "Popular", "low_sugar", des2, "20 min");
            insertDish(db, "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/caponata-pasta-a0027c4.jpg?resize=960,872?quality=90&webp=true&resize=600,545","Bò xào ớt chuông", "Popular", "low_sugar", des, "20 min");
            insertDish(db, "https://delizabeth.com/wp-content/uploads/2023/05/IMG_4796-scaled.jpeg","Nộm rau củ", "Popular", "low_fat", des, "20 min");
            insertDish(db, "https://images.immediate.co.uk/production/volatile/sites/30/2021/07/Vegan-apple-cake-c466e14.jpg?resize=960,872?quality=90&webp=true&resize=600,545","Bún chả", "Popular", "low_sugar", des, "20 min");
            insertDish(db, "https://delizabeth.com/wp-content/uploads/2023/05/IMG_4796-scaled.jpeg","Chả cá", "Popular", "diabetic_friendly", des, "20 min");
            insertDish(db, "https://delizabeth.com/wp-content/uploads/2023/05/IMG_4796-scaled.jpeg","Bún bò huế", "Popular", "kidney_friendly", des, "20 min");
        }

        private void insertDish(SQLiteDatabase db,String img, String title, String category, String tag, String description, String time) {
            ContentValues values = new ContentValues();
            // Do not set code manually; it will auto-increment
            values.put("title", title);
            values.put("img", img);
            values.put("category", category);
            values.put("tag", tag);
            values.put("description", description);
            values.put("time", time);
            db.insert("recipes", null, values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS recipes");
            onCreate(db);
        }
    }

    ImageView salad, main, drinks, dessert, menu;
    RecyclerView rcview_home;
    List<ResModel> dataPopular = new ArrayList<>();
    LottieAnimationView lottie;
    EditText editText;
    List<ResModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        salad = findViewById(R.id.salad);
        main = findViewById(R.id.MainDish);
        drinks = findViewById(R.id.Drinks);
        dessert = findViewById(R.id.Desserts);
        rcview_home = findViewById(R.id.rcview_popular);
        lottie = findViewById(R.id.lottie);
        editText = findViewById(R.id.editText);
        menu = findViewById(R.id.imageView);
        rcview_home.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setPopularList();
        salad.setOnClickListener(v -> start("Salad","Salad"));
        main.setOnClickListener(v -> start("Dish", "Main dish"));
        drinks.setOnClickListener(v -> start("Drinks", "Drinks"));
        dessert.setOnClickListener(v -> start("Desserts", "Dessert"));

        editText.setOnClickListener(v ->{
            Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(intent);
        });

        menu.setOnClickListener(v -> showBottomSheet());
    }

    public void setPopularList() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recipes", null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String img = cursor.getString(cursor.getColumnIndexOrThrow("img"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            String tag = cursor.getString(cursor.getColumnIndexOrThrow("tag"));
            if (category.contains("Popular")) {
                dataPopular.add(new ResModel(img, title, description, category, time, tag));
            }
        }
        cursor.close();

        AdapterPopular adapter = new AdapterPopular(dataPopular, getApplicationContext());
        rcview_home.setAdapter(adapter);

        lottie.setVisibility(View.GONE);
    }

    public void start(String p, String tittle){
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        intent.putExtra("Category", p);
        intent.putExtra("tittle", tittle);
        startActivity(intent);
    }

    private void showBottomSheet() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}