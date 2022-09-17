package com.example.rayyantech.multiplechoicequiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.rayyantech.multiplechoicequiz.R;
import com.example.rayyantech.multiplechoicequiz.database.QuestionDatabaseHelper;
import com.example.rayyantech.multiplechoicequiz.models.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    AppCompatTextView textWelcome, textHighScore;
    AppCompatSpinner spinnerCategories;
    String defaultCategoryName = "Math", quizDifficulty;
    int highScore;
    QuestionDatabaseHelper questionDatabaseHelper = new QuestionDatabaseHelper(this);
    List<CategoriesModel> categoriesModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupTexts();
        setupSpinner();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.act_main_toolbar);
        setSupportActionBar(toolbar);
    }

    public void setupTexts() {
        textWelcome = findViewById(R.id.act_main_welcome_dear_text);
        textHighScore = findViewById(R.id.act_main_hgih_score_text);
    }

    public void setupSpinner() {
        spinnerCategories = findViewById(R.id.act_main_categories_spinner);
        categoriesModelList = questionDatabaseHelper.getCategoriesFromDatabase();
        List<String> categoryNameList = new ArrayList<>();
        for (int i = 0; i < categoriesModelList.size(); i++) {
            categoryNameList.add(categoriesModelList.get(i).getCategoryName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNameList);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(arrayAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                defaultCategoryName = categoriesModelList.get(position).getCategoryName();
//                TextView textView = ((TextView) parent.getChildAt(position)).setTextColor(getResources().getColor(R.color.colorOnPrimary));
//                ((TextView) parent.getChildAt(position)).setTextColor(getResources().getColor(R.color.colorOnPrimary));
                if (defaultCategoryName.equals("Math")) {
                    textHighScore.setText("Math high score: " + getSharedPreferences("Quiz", MODE_PRIVATE).
                            getInt("highScoreMath", 0));
                    highScore = getSharedPreferences("Quiz", MODE_PRIVATE).getInt("highScoreMath", 0);
                } else if (defaultCategoryName.equals("Programming")) {
                    textHighScore.setText("Programming High score: " + getSharedPreferences("Quiz", MODE_PRIVATE).
                            getInt("highScoreProgramming", 0));
                    highScore = getSharedPreferences("Quiz", MODE_PRIVATE).getInt("highScoreProgramming", 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void StartQuiz(View view) {
        quizDifficulty = "Easy";
        String[] items = {"Easy", "Medium", "Hard"};
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Select Difficulty")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quizDifficulty = items[which];
                    }
                })
                .setPositiveButton("Ready", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, QuizActivity.class)
                                .putExtra("highScore", highScore)
                                .putExtra("difficulty", quizDifficulty)
                                .putExtra("category", defaultCategoryName));
                    }
                })
                .setNegativeButton("Cancel", null).create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_profile:
                startActivity(new Intent(MainActivity.this, LoginUserActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}