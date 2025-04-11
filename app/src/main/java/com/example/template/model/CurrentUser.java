package com.example.template.model;

import com.example.template.factory.UserRoleFactory;

/**
 * @file Application.java
 * @author: -
 * @description: Singleton class that represents the current user in the application.
 * The CurrentUser class provides a global class to represent the current user's information.
 */

public class CurrentUser {
    private static CurrentUser instance;
    private User user = null;

    /**
     * Private constructor to prevent instantiation of another instance.
     */
    private CurrentUser() {}

    /**
     * Retrieves the singleton instance of the CurrentUser class.
     * @return The singleton instance of the CurrentUser.
     */
    public static synchronized CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    /**
     * Sets the current user with User object.
     * @param currentUser The User object representing the current user.
     */
    public void setUser(User currentUser) {
        this.user = currentUser;
    }

    /**
     * Retrieves the current user.
     * @return The User object representing the current user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets new role for current user.
     * @param newRole The User new role
     */
    public void setRole(String newRole){
        setUser(UserRoleFactory.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), newRole));
    }
}