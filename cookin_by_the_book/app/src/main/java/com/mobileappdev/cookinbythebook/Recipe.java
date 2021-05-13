package com.mobileappdev.cookinbythebook;

import android.graphics.Picture;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipe implements Parcelable {
    public String name;
    public String owner;
    public String picture;
    public Map<String, String> ingredients;
    public String notes;
    public ArrayList<String> sharedWith;
    public ArrayList<String> steps;
    public ArrayList<String> favorited;
    public ArrayList<String> category;
    public String prepTime;
    public String cookTime;
    public String servings;
    private String uuidOhMyGod;

    public Recipe(String name, String owner, String picture, Map<String, String> ingredients, String notes, ArrayList<String> sharedWith, ArrayList<String> steps, ArrayList<String> favorited, ArrayList<String> category, String prepTime, String cookTime, String servings) {
        this.name = name;
        this.owner = owner;
        this.picture = picture;
        this.ingredients = ingredients;
        this.notes = notes;
        this.sharedWith = sharedWith;
        this.steps = steps;
        this.favorited = favorited;
        this.category = category;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
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

    public ArrayList<String> getFavorited() {
        return favorited;
    }

    public void setFavorited(ArrayList<String> favorited) {
        this.favorited = favorited;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getUuidOhMyGod() {
        return uuidOhMyGod;
    }

    public void setUuidOhMyGod(String uuidOhMyGod) {
        this.uuidOhMyGod = uuidOhMyGod;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", picture='" + picture + '\'' +
                ", ingredients=" + ingredients +
                ", notes='" + notes + '\'' +
                ", sharedWith=" + sharedWith +
                ", steps=" + steps +
                ", favorited=" + favorited +
                ", category=" + category +
                ", prepTime='" + prepTime + '\'' +
                ", cookTime='" + cookTime + '\'' +
                ", servings='" + servings + '\'' +
                '}';
    }

    /*
        this.name
        this.owner
        this.picture
        this.ingredients
        this.notes
        this.sharedWith
        this.steps
        this.favorited
        this.category
        this.prepTime
        this.cookTime
        this.servings
     */


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.owner);
        dest.writeString(this.picture);
        dest.writeInt(this.ingredients.size());
        for (Map.Entry<String, String> entry : this.ingredients.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.notes);
        dest.writeStringList(this.sharedWith);
        dest.writeStringList(this.steps);
        dest.writeStringList(this.favorited);
        dest.writeStringList(this.category);
        dest.writeString(this.prepTime);
        dest.writeString(this.cookTime);
        dest.writeString(this.servings);
        dest.writeString(this.uuidOhMyGod);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.owner = source.readString();
        this.picture = source.readString();
        int ingredientsSize = source.readInt();
        this.ingredients = new HashMap<String, String>(ingredientsSize);
        for (int i = 0; i < ingredientsSize; i++) {
            String key = source.readString();
            String value = source.readString();
            this.ingredients.put(key, value);
        }
        this.notes = source.readString();
        this.sharedWith = source.createStringArrayList();
        this.steps = source.createStringArrayList();
        this.favorited = source.createStringArrayList();
        this.category = source.createStringArrayList();
        this.prepTime = source.readString();
        this.cookTime = source.readString();
        this.servings = source.readString();
        this.uuidOhMyGod = source.readString();
    }

    protected Recipe(Parcel in) {
        this.name = in.readString();
        this.owner = in.readString();
        this.picture = in.readString();
        int ingredientsSize = in.readInt();
        this.ingredients = new HashMap<String, String>(ingredientsSize);
        for (int i = 0; i < ingredientsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.ingredients.put(key, value);
        }
        this.notes = in.readString();
        this.sharedWith = in.createStringArrayList();
        this.steps = in.createStringArrayList();
        this.favorited = in.createStringArrayList();
        this.category = in.createStringArrayList();
        this.prepTime = in.readString();
        this.cookTime = in.readString();
        this.servings = in.readString();
        this.uuidOhMyGod = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
