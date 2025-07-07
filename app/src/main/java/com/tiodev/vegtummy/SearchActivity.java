package com.tiodev.vegtummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.tiodev.vegtummy.Adapter.SearchAdapter;
import com.tiodev.vegtummy.Model.ResModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    EditText search;
    ImageView back_btn;
    RecyclerView rcview;
    List<ResModel> dataPopular = new ArrayList<>();
    SearchAdapter adapter;
    List<ResModel> recipes;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find views
        search = findViewById(R.id.search);
        back_btn = findViewById(R.id.back_to_home);
        rcview = findViewById(R.id.rcview);
        search.clearFocus();

        // ChipGroup filter
        com.google.android.material.chip.ChipGroup chipGroup = findViewById(R.id.chip_group_filter);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String filterTag = "";
            switch (checkedId) {
                case R.id.chip_low_sugar:
                    filterTag = "low_sugar";
                    break;
                case R.id.chip_low_salt:
                    filterTag = "low_salt";
                    break;
                case R.id.chip_low_fat:
                    filterTag = "low_fat";
                    break;
                case R.id.chip_diabetic:
                    filterTag = "diabetic_friendly";
                    break;
                case R.id.chip_kidney:
                    filterTag = "kidney_friendly";
                    break;
                case R.id.chip_all:
                    filterTag = "all";
                    break;
                default:
                    filterTag = "";
                    break;
            }

            if (filterTag.equals("all") || filterTag.isEmpty()) {
                adapter.filterList(dataPopular);
            } else {
                List<ResModel> filtered = new ArrayList<>();
                for (ResModel r : recipes) {
                    if (r.getTag() != null && r.getTag().contains(filterTag)) {
                        filtered.add(r);
                    }
                }
                adapter.filterList(filtered);
            }
        });

        // Show and focus the keyboard
        search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        // Get database using SQLite
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        recipes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM recipes", null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String img = cursor.getString(cursor.getColumnIndexOrThrow("img"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String tag = cursor.getString(cursor.getColumnIndexOrThrow("tag"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

            ResModel item = new ResModel(img, title, description, category,time, tag);
            recipes.add(item);

            if (category.contains("Popular")) {
                dataPopular.add(item);
            }
        }
        cursor.close();

        // Set layout manager to recyclerView
        rcview.setLayoutManager(new LinearLayoutManager(this));

        // Hide keyboard when recyclerView item clicked
        rcview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                return false;
            }
        });

        // Set adapter to search recyclerView
        adapter = new SearchAdapter(dataPopular, getApplicationContext());
        rcview.setAdapter(adapter);


        // Search from all recipes when Edittext data changed
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Method required*
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Method required*
            }

            @Override
            public void afterTextChanged(Editable s) {
                 filter(s.toString());
            }
        });


        // Exit activity
        back_btn.setOnClickListener(v -> {
            imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
            finish();
        });
    }

    // Filter the searched item from all recipes
    public void filter(String text) {
        if (text.isEmpty()) {
            adapter.filterList(recipes);
        } else {
            List<ResModel> filterList = new ArrayList<>();
            for (int i = 0; i < recipes.size(); i++) {
                if (recipes.get(i).getTittle().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                    filterList.add(recipes.get(i));
                }
            }
            // Update search recyclerView with new item
            adapter.filterList(filterList);
        }
    }

    // SQLiteOpenHelper for recipes database
    class DBHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "recipes.db";
        private static final int DB_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Không tạo bảng hoặc thêm dữ liệu vì DB đã tồn tại sẵn
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS recipes");
            onCreate(db);
        }
    }
}