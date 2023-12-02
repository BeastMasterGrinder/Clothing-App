package com.example.clothingapp.Schemas;


import java.sql.*;
import java.util.ArrayList;

// Product class
public class Product {
    private int productID;
    private String name;
    private double price;
    private int inStock;
    private ArrayList<Review> reviews;

    public Product(int productID,String name, double price, int inStock) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.reviews = new ArrayList<>();
//        saveToDatabase();
        loadReviews();
    }

    public static int getIDFromDatabase() {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123"))
        {
            String query = "SELECT * FROM products ORDER BY id DESC LIMIT 1";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if(resultSet.next())
                    {
                        System.out.println("Product ID loaded from database");

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

    public int getProductID() {
        return productID;
    }

    protected void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean checkAvailability() {
        // Implementation for checking product availability
        return inStock > 0;
    }

    public void checkReviews() {
        // Implementation for checking product reviews
        for (Review review : reviews) {
            review.displayReview();
        }
    }

    public void addReview(Review review) {
        // Implementation for adding a review
        reviews.add(review);
    }

    public void saveToDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "INSERT INTO products (product_id, name, price, in_stock) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, String.valueOf(getProductID()));
                preparedStatement.setString(2, getName());
                preparedStatement.setDouble(3, getPrice());
                preparedStatement.setInt(4, getInStock());
                System.out.println("Product saved to database");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //This should run when the product is called
    private ArrayList<Review> loadReviews() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "SELECT * FROM reviews WHERE product_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, String.valueOf(getProductID()));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ArrayList<Review> reviews = new ArrayList<>();
                    while (resultSet.next()) {
                        int reviewID = resultSet.getInt("review_id");
                        int rating = resultSet.getInt("rating");
                        int productID = resultSet.getInt("product_id");
                        String feedback = resultSet.getString("feedback");
                        Date date = resultSet.getDate("review_date");
                        Review review = new Review(reviewID, rating, productID, feedback, date);
                        reviews.add(review);
                    }
                    this.reviews = reviews;
                    return reviews;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void updateProductInDatabase(Product product) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "UPDATE products SET name=?, price=?, in_stock=? WHERE product_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setInt(3, product.getInStock());
                preparedStatement.setString(4, String.valueOf(product.getProductID()));
                System.out.println("Product updated in database");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProductFromDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            // Check if there are any order items that reference this product
            String query = "SELECT COUNT(*) FROM order_items WHERE product_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        // There are order items that reference this product, so don't delete it
                        System.out.println("Cannot delete product because it is referenced by one or more order items.");
                        return;
                    }
                }
            }

            // Delete the product
            query = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayDetails() {
        System.out.println("Product ID: " + getProductID());
        System.out.println("Name: " + getName());
        System.out.println("Price: " + getPrice());
        System.out.println("In Stock: " + getInStock());
        System.out.println("Reviews: ");
        checkReviews();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                ", reviews=" + reviews +
                '}';
    }

}