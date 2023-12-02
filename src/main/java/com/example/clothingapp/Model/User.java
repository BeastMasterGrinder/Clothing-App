package com.example.clothingapp.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import com.example.clothingapp.Model.*;

// User class
public class User {
    private static int userID;
    private String name;
    private String email;
    private String password;
    private String role;
    public User(String name, String email, String password, String role){
        userID = generateOrderID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public static int getUserID() {
        return userID;
    }

    public static int generateOrderID() {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123"))
        {
            String query = "SELECT * FROM users ORDER BY id DESC LIMIT 1";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if(resultSet.next())
                    {
                        System.out.println("User ID loaded from database");

                        return resultSet.getInt("id");

                    }
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    // New method for user login
    public static User login(String username, String password, String role) {
        return switch (role) {
            case "Customer" -> authenticate(username, password, role);
            case "Administrator" -> authenticate(username, password, role);
            case "Shop Owner" -> authenticate(username, password, role);
            default -> null;
        };
    }

    public String getRole() {
        return role;
    }
    // New method for user sign-in
    public static User signIn(String name, String email, String password, String role) {
        User newUser = new User(name, email, password, role);
        newUser.saveToDatabase();
        return newUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    // New method to save user to the database
    public void saveToDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, getName());
                preparedStatement.setString(2, getEmail());
                preparedStatement.setString(3, getPassword());
                preparedStatement.setString(4, getRole());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // New method to authenticate user from the database
    public static User authenticate(String name, String password, String role) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "SELECT * FROM users WHERE name = ? AND password = ? AND role = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        if(resultSet.getString("role").equals("Administrator")){
                            return new Administrator(
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password"),
                                    resultSet.getInt("id")
                            );
                        }
                        else if (resultSet.getString("role").equals("Customer")){
                            return new Customer(
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password"),
                                    resultSet.getInt("id")
                            );
                        }
                        else if (resultSet.getString("role").equals("Shop Owner")){
                            return new StoreOwner(
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password"),
                                    resultSet.getInt("id")
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}

