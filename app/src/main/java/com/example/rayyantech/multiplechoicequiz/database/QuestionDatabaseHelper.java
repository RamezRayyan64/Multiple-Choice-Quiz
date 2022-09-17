package com.example.rayyantech.multiplechoicequiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Parcelable;

import com.example.rayyantech.multiplechoicequiz.models.CategoriesModel;
import com.example.rayyantech.multiplechoicequiz.models.QuestionModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "QUIZ";
    public static final int DATABASEVERSION = 1;
    public static final String QUESTIONTABLENAME = "QUESTIONS";
    public static final String COLQUESTIONID = "QUESTIONID";
    public static final String COLQUESTION = "QUESTION";
    public static final String COLOPTIONONE = "OPTIONONE";
    public static final String COLOPTIONTWO = "OPTIONTWO";
    public static final String COLOPTIONTHREE = "OPTIONTHREE";
    public static final String COLOPTIONFOUR = "OPTIONFOUR";
    public static final String COLQUESTIONANSWER = "QUESTIONANSWER";
    public static final String COLQUESTIONDIFFICULTY = "COLQUESTIONDIFFICULTY";
    public static final String CATEGORIESTABLENAME = "CATEGORIESTABLENAME";
    public static final String COLCATEGORIEID = "COLCATEGORIEID";
    public static final String COLCATEGORIESNAME = "COLCATEGORIESNAME";
    public static final String COLQUESTIONCATEGORIESNAME = "COLQUESTIONCATEGORIESNAME";
    public static SQLiteDatabase sqLiteDatabase;

    public QuestionDatabaseHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sqLiteDatabase = db;
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + CATEGORIESTABLENAME + " (" +
                COLCATEGORIEID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLCATEGORIESNAME + " TEXT NOT NULL" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + QUESTIONTABLENAME + " (" +
                COLQUESTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLQUESTION + " TEXT NOT NULL UNIQUE," +
                COLOPTIONONE + " TEXT NOT NULL," +
                COLOPTIONTWO + " TEXT NOT NULL," +
                COLOPTIONTHREE + " TEXT NOT NULL," +
                COLOPTIONFOUR + " TEXT NOT NULL," +
                COLQUESTIONANSWER + " INTEGER NOT NULL," +
                COLQUESTIONDIFFICULTY + " TEXT NOT NULL," +
                COLQUESTIONCATEGORIESNAME + " TEXT NOT NULL" +
                ");");
        insertCategoriesToDatabase();
        insertQuestionsIntoDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONTABLENAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIESTABLENAME);
        onCreate(db);
    }

    public void insertQuestionsIntoDatabase() {
        List<QuestionModel> questionModelList = new ArrayList<>();
        questionModelList.add(new QuestionModel("2 + 2 = ?", "2", "1",
                "4", "6", 3, "Easy", "Math"));
        questionModelList.add(new QuestionModel("1 + 2 = ?", "5", "7",
                "2", "3", 4, "Easy", "Math"));
        questionModelList.add(new QuestionModel("2 + 4 = ?", "3", "2",
                "4", "6", 4, "Easy", "Math"));
        questionModelList.add(new QuestionModel("5 + 2 = ?", "7", "2",
                "4", "6", 1, "Easy", "Math"));
        questionModelList.add(new QuestionModel("2 + 3 = ?", "1", "2",
                "5", "6", 3, "Easy", "Math"));
        questionModelList.add(new QuestionModel("23 + 62 = ?", "82", "110",
                "85", "69", 3, "Medium", "Math"));
        questionModelList.add(new QuestionModel("71 + 20 = ?", "75", "97",
                "102", "91", 4, "Medium", "Math"));
        questionModelList.add(new QuestionModel("52 + 34 = ?", "83", "72",
                "84", "86", 4, "Medium", "Math"));
        questionModelList.add(new QuestionModel("75 + 12 = ?", "87", "82",
                "94", "76", 1, "Medium", "Math"));
        questionModelList.add(new QuestionModel("25 + 93 = ?", "110", "92",
                "118", "116", 3, "Medium", "Math"));
        questionModelList.add(new QuestionModel("Which one is not a programming language ?", "C++", "C",
                "HTML", "Java", 3, "Easy", "Programming"));
        for (int i = 0; i < questionModelList.size(); i++) {
            addQuestionsToDatabase(questionModelList.get(i));
        }
    }

    public void insertCategoriesToDatabase() {
        List<CategoriesModel> categoriesModelList = new ArrayList<>();
        categoriesModelList.add(new CategoriesModel("Math"));
        categoriesModelList.add(new CategoriesModel("Programming"));
        for (int i = 0; i < categoriesModelList.size(); i++) {
            addCategoriesToDatabase(categoriesModelList.get(i));
        }
    }

    public void addQuestionsToDatabase(QuestionModel questionModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLQUESTION, questionModel.getQuestion());
        contentValues.put(COLOPTIONONE, questionModel.getOptionOne());
        contentValues.put(COLOPTIONTWO, questionModel.getOptionTwo());
        contentValues.put(COLOPTIONTHREE, questionModel.getOptionThree());
        contentValues.put(COLOPTIONFOUR, questionModel.getOptionFour());
        contentValues.put(COLQUESTIONANSWER, questionModel.getQuestionAnswer());
        contentValues.put(COLQUESTIONDIFFICULTY, questionModel.getDifficultyLevel());
        contentValues.put(COLQUESTIONCATEGORIESNAME, questionModel.getCategoryID());
        sqLiteDatabase.insert(QUESTIONTABLENAME, null, contentValues);
    }

    public void addCategoriesToDatabase(CategoriesModel categoriesModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLCATEGORIESNAME, categoriesModel.getCategoryName());
        sqLiteDatabase.insert(CATEGORIESTABLENAME, null, contentValues);
    }

    public List<CategoriesModel> getCategoriesFromDatabase() {
        sqLiteDatabase = getReadableDatabase();
        List<CategoriesModel> categoriesModelList = new ArrayList<>();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(CATEGORIESTABLENAME);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, null, null,
                null, null, null, COLCATEGORIEID);
        int indexCategory = cursor.getColumnIndex(COLCATEGORIESNAME);
        if (cursor.moveToFirst()) {
            do {
                categoriesModelList.add(new CategoriesModel(cursor.getString(indexCategory)));
            } while (cursor.moveToNext());
        }
        return categoriesModelList;
    }

    public List<QuestionModel> getQuestionsFromDatabase(String difficulty, String category) {
        sqLiteDatabase = getReadableDatabase();
        List<QuestionModel> questionModelList = new ArrayList<>();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(QUESTIONTABLENAME);
        Cursor cursorTableQuestion = sqLiteQueryBuilder.query(sqLiteDatabase, null,
                COLQUESTIONDIFFICULTY + " = ? and " + COLQUESTIONCATEGORIESNAME + " = ?",
                new String[]{difficulty, category}, null, null, COLQUESTIONID);
        int indexQuestion = cursorTableQuestion.getColumnIndex(COLQUESTION);
        int indexOptionOne = cursorTableQuestion.getColumnIndex(COLOPTIONONE);
        int indexOptionTwo = cursorTableQuestion.getColumnIndex(COLOPTIONTWO);
        int indexOptionThree = cursorTableQuestion.getColumnIndex(COLOPTIONTHREE);
        int indexOptionFour = cursorTableQuestion.getColumnIndex(COLOPTIONFOUR);
        int indexQuestionAnswer = cursorTableQuestion.getColumnIndex(COLQUESTIONANSWER);
        int indexQuestionDifficulty = cursorTableQuestion.getColumnIndex(COLQUESTIONDIFFICULTY);
        int indexQuestionCategoryName = cursorTableQuestion.getColumnIndex(COLQUESTIONCATEGORIESNAME);
        if (cursorTableQuestion.moveToFirst()) {
            do {
                questionModelList.add(new QuestionModel(cursorTableQuestion.getString(indexQuestion), cursorTableQuestion.getString(indexOptionOne),
                        cursorTableQuestion.getString(indexOptionTwo), cursorTableQuestion.getString(indexOptionThree), cursorTableQuestion.getString(indexOptionFour),
                        cursorTableQuestion.getInt(indexQuestionAnswer), cursorTableQuestion.getString(indexQuestionDifficulty), cursorTableQuestion.getString(indexQuestionCategoryName)));
            } while (cursorTableQuestion.moveToNext());
        }
        cursorTableQuestion.close();
        return questionModelList;
    }
}
