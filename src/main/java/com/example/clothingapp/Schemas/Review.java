package com.example.clothingapp.Schemas;

import java.sql.*;
import java.sql.Date;

// Review class
public class Review {
    private int reviewID;
    private int rating;
    private int productID;
    private String feedback;
    private Date date;

    public Review(int reviewID, int rating, int productID, String feedback, Date date) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.productID = productID;
        this.feedback = feedback;
        this.date = date;
    }



    public static int generateReviewID() {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123"))
        {
            String query = "SELECT * FROM reviews ORDER BY id DESC LIMIT 1";
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

    public void reviewOrder(String orderID) {
        // Implementation for reviewing an order
    }

    public void reviewProduct(String productID) {
        // Implementation for reviewing a product
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerName() {
        String customerName = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "SELECT users.name FROM users INNER JOIN reviews ON users.id = reviews.user_id WHERE reviews.review_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, reviewID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        customerName = resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerName;
    }
    public void SaveReviewToDatabase() {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")){
            String query = "INSERT INTO reviews (review_id, rating, product_id, feedback, review_date) VALUES (?, ?, ?, ?, ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1, reviewID);
                preparedStatement.setInt(2, rating);
                preparedStatement.setInt(3, productID);
                preparedStatement.setString(4, feedback);
                preparedStatement.setDate(5, new java.sql.Date(date.getTime()));
                preparedStatement.executeUpdate();
                System.out.println("Review Saved in Database");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void displayReview() {
        // Implementation for displaying a review
        System.out.println("Review ID: " + reviewID);
    }
}