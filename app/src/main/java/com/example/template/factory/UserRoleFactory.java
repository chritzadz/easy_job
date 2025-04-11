package com.example.template.factory;

import com.example.template.model.Employee;
import com.example.template.model.Employer;
import com.example.template.model.User;

public class UserRoleFactory {
    /**
     * Creates a user based on their role. Employer and Employee are sub-classes of User.
     * @param email The email of the user
     * @param pass The password of the user.
     * @param role The role of the user.
     * @return A User object, which is polymorph to Employer or Employee.
     */
    public static User createUser(String first, String last, String email, String pass, String role){
        if (role.equals("Employee")){
            return new Employee(first, last, email, pass);
        }
        else{
            return new Employer(first, last, email, pass);
        }
    }
}
