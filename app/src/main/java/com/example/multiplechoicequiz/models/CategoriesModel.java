package com.example.multiplechoicequiz.models;

public class CategoriesModel {

    int categoryId;
    String categoryName;

    public CategoriesModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
