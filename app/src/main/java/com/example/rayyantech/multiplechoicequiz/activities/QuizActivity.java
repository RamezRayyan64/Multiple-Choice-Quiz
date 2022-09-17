package com.example.rayyantech.multiplechoicequiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rayyantech.multiplechoicequiz.R;
import com.example.rayyantech.multiplechoicequiz.database.QuestionDatabaseHelper;
import com.example.rayyantech.multiplechoicequiz.models.QuestionModel;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    Toolbar toolbar;
    AppCompatTextView textScore, textQuestionCount, textTimer, textQuestion, textAnswer, textDifficulty, textCategory;
    RadioGroup radioGroupAnswerChoices;
    AppCompatRadioButton radioButtonOptionOne, radioButtonOptionTwo, radioButtonOptionThree, radioButtonOptionFour, checkedRadioButton;
    QuestionDatabaseHelper questionDatabaseHelper = new QuestionDatabaseHelper(this);
    List<QuestionModel> questionModelList;
    AppCompatButton buttonConfirm;
    int questionCounter = 0, score = 0, highScorePlayed, highScoreFromDatabase, radioButtonCheckedIndex;
    boolean isSelected = false, isNext = true, isExit = false;
    CountDownTimer countDownTimer;
    long getCurrentTime;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setupToolbar();
        getQuestionsFromDatabase();
        setupTexts();
        setupRadioButtons();
        setupButton();
        setupTimer();
        countDownTimer.start();
    }

    public void setupToolbar() {
        toolbar = findViewById(R.id.act_quiz_toolbar);
        setSupportActionBar(toolbar);
    }

    public void setupButton() {
        buttonConfirm = findViewById(R.id.act_quiz_confirm_button);
    }

    public void setupTexts() {
        textScore = findViewById(R.id.act_quiz_score_text);
        textScore.setText("Score: 0");
        textQuestionCount = findViewById(R.id.act_quiz_question_count_text);
        textTimer = findViewById(R.id.act_quiz_timer_text);
        textQuestion = findViewById(R.id.act_quiz_question_text);
        textQuestion.setText(questionModelList.get(questionCounter).getQuestion());
        textQuestionCount.setText("Question: " + (questionCounter + 1) + "/" + questionModelList.size());
        textAnswer = findViewById(R.id.act_quiz_answer_text);
        textDifficulty = findViewById(R.id.act_quiz_difficulty_text);
        textDifficulty.setText("Difficulty: " + getIntent().getStringExtra("difficulty"));
        textCategory = findViewById(R.id.act_quiz_category_text);
        textCategory.setText("Category: " + getIntent().getStringExtra("category"));
    }

    public void setupTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished / 1000) >= 10) {
                    textTimer.setText("0:" + String.valueOf(millisUntilFinished / 1000));
                } else if ((millisUntilFinished / 1000) < 10) {
                    textTimer.setText("0:0" + String.valueOf(millisUntilFinished / 1000));
                    textTimer.setTextColor(Color.RED);
                } else {
                    if (radioGroupAnswerChoices.getCheckedRadioButtonId() == -1) {
                        textAnswer.setVisibility(View.VISIBLE);
                        textAnswer.setTextColor(Color.RED);
                        textAnswer.setText("Your answer is incorrect!");
                    }
                    textTimer.setText("0:00");
                }
            }

            @Override
            public void onFinish() {
                if (radioButtonCheckedIndex == -1) {
                    textAnswer.setVisibility(View.VISIBLE);
                    textAnswer.setTextColor(Color.RED);
                    textAnswer.setText("Your answer is incorrect");
                } else {
                    checkAnswer();
                }
            }
        };
    }

    public void setupRadioButtons() {
        radioGroupAnswerChoices = findViewById(R.id.act_main_question_radio_group);
        radioButtonOptionOne = findViewById(R.id.act_main_option_one_radio_button);
        radioButtonOptionOne.setText(questionModelList.get(questionCounter).getOptionOne());
        radioButtonOptionTwo = findViewById(R.id.act_main_option_two_radio_button);
        radioButtonOptionTwo.setText(questionModelList.get(questionCounter).getOptionTwo());
        radioButtonOptionThree = findViewById(R.id.act_main_option_three_radio_button);
        radioButtonOptionThree.setText(questionModelList.get(questionCounter).getOptionThree());
        radioButtonOptionFour = findViewById(R.id.act_main_option_four_radio_button);
        radioButtonOptionFour.setText(questionModelList.get(questionCounter).getOptionFour());
    }

    public void getQuestionsFromDatabase() {
        questionModelList = questionDatabaseHelper.getQuestionsFromDatabase(getIntent().getStringExtra("difficulty"),
                getIntent().getStringExtra("category"));
        for (int i = 0; i < questionModelList.size(); i++) {
            Log.i("Question", questionModelList.get(i).getQuestion());
        }
    }

    public void ConfirmQuestion(View view) {
        checkedRadioButton = findViewById(radioGroupAnswerChoices.getCheckedRadioButtonId());
        radioButtonCheckedIndex = radioGroupAnswerChoices.indexOfChild(checkedRadioButton) + 1;
        if (radioButtonCheckedIndex != -1) {
            if (isNext) {
                questionCounter++;
            }
            if (questionCounter <= questionModelList.size() - 1) {
                if (!isNext) {
                    buttonConfirm.setText("Confirm");
                    textQuestion.setText(questionModelList.get(questionCounter).getQuestion());
                    radioButtonOptionOne.setText(questionModelList.get(questionCounter).getOptionOne());
                    radioButtonOptionTwo.setText(questionModelList.get(questionCounter).getOptionTwo());
                    radioButtonOptionThree.setText(questionModelList.get(questionCounter).getOptionThree());
                    radioButtonOptionFour.setText(questionModelList.get(questionCounter).getOptionFour());
                    textQuestionCount.setText("Question: " + (questionCounter + 1) + "/" + questionModelList.size());
                    textTimer.setText("0:30");
                    countDownTimer.start();
                    textAnswer.setVisibility(View.INVISIBLE);
                    textAnswer.setTextColor(getResources().getColor(R.color.colorOnPrimary));
                    radioGroupAnswerChoices.clearCheck();
                    isNext = true;
                } else {
                    buttonConfirm.setText("Next");
                    checkAnswer();
                }
            } else {
                if (!isExit) {
                    checkAnswer();
                    isExit = !isExit;
                    isNext = true;
                }
                buttonConfirm.setText("Finish");
                if (questionCounter > questionModelList.size()) {
                    categoryName = getIntent().getStringExtra("category");
                    highScorePlayed = getIntent().getIntExtra("highScore", 0);
                    SharedPreferences sharedPreferences = getSharedPreferences("Quiz", MODE_PRIVATE);
                    if (categoryName.equals("Math")) {
                        if (highScorePlayed < score) {
                            sharedPreferences.edit().putInt("highScoreMath", score).apply();
                        }
                    } else if (categoryName.equals("Programming")) {
                        if (highScorePlayed < score) {
                            Toast.makeText(this, "TTTTTTTT", Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putInt("highScoreProgramming", score).apply();
                        }
                    }
                    startActivity(new Intent(QuizActivity.this, MainActivity.class));
                }
            }
        } else {
            Toast.makeText(this, "Please choose an option", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAnswer() {
        countDownTimer.cancel();
        if (radioButtonCheckedIndex == questionModelList.get(questionCounter - 1).getQuestionAnswer()) {
            textAnswer.setVisibility(View.VISIBLE);
            textAnswer.setTextColor(getResources().getColor(R.color.colorOnPrimary));
            textScore.setText("Score: " + ++score);
        } else {
            textAnswer.setVisibility(View.VISIBLE);
            textAnswer.setText("Your answer is incorrect");
            textAnswer.setTextColor(Color.RED);
        }
        isNext = false;
    }

    @Override
    public void onBackPressed() {
        if ((getCurrentTime + 2000) > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        getCurrentTime = System.currentTimeMillis();
    }
}