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

        public User(String firstName, String lastName, String email, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public void setFirstName(String firstName){ this.firstName = firstName; }
        public void setLastName(String lastName){ this.lastName = lastName; }

        public void setPassword(String password) {this.password = password;}

        public void setRole(String role){ this.role = role; }
        public String getFirstName() {return firstName;}
        public String getLastName() {return lastName;}

        public String getRole() {
            return role;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() { return password;}



}
