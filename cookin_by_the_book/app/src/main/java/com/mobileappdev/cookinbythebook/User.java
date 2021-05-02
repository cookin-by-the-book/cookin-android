package com.mobileappdev.cookinbythebook;

import java.util.ArrayList;

public class User {

    public String firstName;
    public String lastName;
    public String email;
    public ArrayList<Recipe> recipes;

    public User(String firstName, String lastName, String email, ArrayList<Recipe> recipes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.recipes = recipes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
