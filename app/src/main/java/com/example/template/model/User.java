package com.example.template.model;

/**
 * @file User.java
 * @author: -
 * @description: Represent a user in the system.
 * The User class holds credentials of user.
 */

import java.io.Serializable;

public class User implements Serializable{
        private String email;
        private String password;
        protected String role;
        private String firstName;
        private String lastName;

        public User() {}

        /**
         * Constructs a new User
         * @param email The email of the user account
         * @param password The password of the user account
         */
        public User(String firstName, String lastName, String email, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

        /**
         * @return The user role
         */
        public String getRole() {
            return role;
        }

        /**
         * @return The user email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email The user email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * @return The user email
         */
        public String getFirstName() {
            return firstName;
        }


        /**
         * @return The user email
         */
        public String getLastName() {
            return lastName;
        }


    /**
         * @return The user password
         */
        public String getPassword() { return password;}

        /**
         * @param password The user password
         */
        public void setPassword(String password) {this.password = password;}

        /**
         * @param role The user role
         */
        public void setRole(String role){ this.role = role; }

        /**
         * @param firstName The user role
         */
        public void setFirstName(String firstName){ this.firstName = firstName; }

        /**
         * @param lastName The user role
         */
        public void setLastName(String lastName){ this.lastName = lastName; }
}
