package com.eduar2tc.dinnerdecider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity {
    private ArrayList<String> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        Intent intent = this.getIntent();
        this.foodList = intent.getStringArrayListExtra("foodList");
        ListAdapter listAdapter = new ListAdapter(this, foodList);
        ListView listView = findViewById(R.id.foodList);
        View empty = findViewById(R.id.empty); //Empty list view
        listView.setAdapter(listAdapter);
        //Check if listView is empty
        listView.setEmptyView( empty );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("result", this.foodList);
        this.setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}