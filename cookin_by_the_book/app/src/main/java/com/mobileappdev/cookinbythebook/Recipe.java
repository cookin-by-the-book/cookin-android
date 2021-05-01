package com.mobileappdev.cookinbythebook;

import android.graphics.Picture;

import java.util.ArrayList;
import java.util.Map;

public class Recipe {
    public String name;
    public String owner;
    public String picture;
    public Map<String, String> ingredients;
    public String notes;
    public ArrayList<String> sharedWith;
    public ArrayList<String> steps;

    public Recipe(String name, String owner, String picture, Map<String, String> ingredients, String notes, ArrayList<String> sharedWith, ArrayList<String> steps) {
        this.name = name;
        this.owner = owner;
        this.picture = picture;
        this.ingredients = ingredients;
        this.notes = notes;
        this.sharedWith = sharedWith;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Map<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(ArrayList<String> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }
}
