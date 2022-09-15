package com.example.multiplechoicequiz.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionModel { //implements Parcelable
    private String question;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
    private String optionFour;
    private int questionAnswer;
    private String difficultyLevel;
    private String categoryID;

    public QuestionModel(String question, String optionOne, String optionTwo, String optionThree, String optionFour, int questionAnswer,
                         String difficultyLevel, String categoryID) {
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.questionAnswer = questionAnswer;
        this.difficultyLevel = difficultyLevel;
        this.categoryID = categoryID;
    }

    //    protected QuestionModel(Parcel in) {
//        question = in.readString();
//        optionOne = in.readString();
//        optionTwo = in.readString();
//        optionThree = in.readString();
//        optionFour = in.readString();
//        questionAnswer = in.readInt();
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(question);
//        dest.writeString(optionOne);
//        dest.writeString(optionTwo);
//        dest.writeString(optionThree);
//        dest.writeString(optionFour);
//        dest.writeInt(questionAnswer);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
//        @Override
//        public QuestionModel createFromParcel(Parcel in) {
//            return new QuestionModel(in);
//        }
//
//        @Override
//        public QuestionModel[] newArray(int size) {
//            return new QuestionModel[size];
//        }
//    };

    public String getQuestion() {
        return question;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public int getQuestionAnswer() {
        return questionAnswer;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getCategoryID() {
        return categoryID;
    }
}
