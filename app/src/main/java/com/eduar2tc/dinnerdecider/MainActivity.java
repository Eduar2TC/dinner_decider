package com.eduar2tc.dinnerdecider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button decideBtn;
    Button addFoodBtn;
    EditText inputFood;
    TextView foodSelected;
    ArrayList<String> foodList = new ArrayList<>();
    Handler handler;
    Runnable runnable;
    ActivityResultLauncher<Intent> activityResultLaunch;
    public void initializeElements(){
        this.foodList.add("Mexican");
        this.foodList.add("Chinese");
        this.foodList.add("Hamburger");
        this.foodList.add("Geronimo´s Pizza");
        this.foodList.add("Hot Cakes");

        this.decideBtn = findViewById(R.id.decideBtn);
        this.addFoodBtn = findViewById(R.id.addFoodBtn);
        this.foodSelected = findViewById(R.id.selectedFoodTxt);
        this.inputFood = findViewById(R.id.inputFoodTxt);

    }
    public void addFood( String food ){
        this.foodList.add( food );
    }
    public int getRandomNumber(){
        Random random = new Random();
        return this.foodList.size() > 0 ? random.nextInt( this.foodList.size() ) : - 1;
    }
    public void rotateImage(){
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        ImageView image= (ImageView) findViewById(R.id.imageView);
        image.startAnimation(rotate);
    }
    public void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    public  void initTask(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int temp = getRandomNumber();
                decideBtn.setEnabled(false);
                foodSelected.setText(
                        temp > -1 ? foodList.get( temp ) : getResources().getString(R.string.list_view_empty)
                );
                handler.postDelayed(this, 100); //Creation a loop
            }
        };
        handler.postDelayed( runnable, 100 );
    }
    public void stopTask(){
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                decideBtn.setEnabled(true);
                handler.removeMessages(0);
                handler.removeCallbacks(runnable);
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu_main, menu );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.list) {
            Intent intent = new Intent(MainActivity.this, FoodListActivity.class);
            intent.putExtra("foodList", this.foodList);
            activityResultLaunch.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeElements();

        //Listening changes
        this.activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK ) {
                            Intent data = result.getData();
                            if (data != null) {
                                foodList = data.getStringArrayListExtra("result");
                            }
                        } else if(result.getResultCode() == Activity.RESULT_CANCELED ) {
                            return;
                        }
                    }
                });

        this.addFoodBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String food = inputFood.getText().toString();
                        if( food.isEmpty() ){
                            Toast.makeText( getApplicationContext(), R.string.toast_message,  Toast.LENGTH_SHORT).show();
                        }else{
                            food = (inputFood.getText().toString()).substring(0,1).toUpperCase() + (inputFood.getText().toString()).substring(1).toLowerCase();
                            addFood( food );
                            inputFood.getText().clear();
                            hideKeyBoard();
                        }
                    }
                }
        );
        this.decideBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rotateImage();
                        /*Init loop*/
                        initTask();
                        /*Stop loop wait 1 second after clicking the button*/
                        stopTask();
                    }
                }
        );
    }
}