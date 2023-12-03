package com.example.clothingapp.Schemas;

import com.example.clothingapp.Model.User;
import javafx.geometry.Insets;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.application.Platform;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

// Order class
public class Order implements Payment {
    private int orderID;
    private String orderStatus;
    private Date orderDate;
    private Double Total;

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    private ArrayList<CartItem> orderedItems;

    public Order(ArrayList<CartItem> orderedItems) {
        this.orderID = generateOrderID();
        this.orderStatus = "Pending";
        this.orderDate = new Date();
        this.orderedItems = new ArrayList<>(orderedItems);
    }

    public static int generateOrderID() {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123"))
        {
            String query = "SELECT * FROM orders ORDER BY id DESC LIMIT 1";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if(resultSet.next())
                    {
                        System.out.println("Order ID loaded from database");

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

    public ArrayList<CartItem> getOrderedItems() {
        return orderedItems;
    }

    public void addProduct(Product product, int quantity) {
        orderedItems.add(new CartItem(product, quantity));
    }

    public int getOrderID() {
        return orderID;
    }

    public void checkStatus() {
        // Implementation for checking order status
    }

    public void trackOrder() {
        // Implementation for tracking an order
    }

    public void pay(Double Total, int userID) {
        // Implementation for making a payment
        this.Total = Total;
        generateReceipt();
        saveToDatabase(userID);
    }

    @Override
    public void discount() {

    }


    @Override
    public void generateReceipt() {
        // Implementation for generating a receipt
        Dialog dialog = new Dialog();
        dialog.setTitle("Receipt");
        dialog.setHeaderText("Order Receipt");
        dialog.setContentText("Order ID: " + orderID + "\n" + "Order Date: " + orderDate + "\n" + "Order Status: " + orderStatus + "\n" + "Total: " + Total);
        dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        dialog.showAndWait();
    }

    public void showOrderedProducts() {
        // Implementation for displaying ordered products
        ArrayList<Product> orderedProducts = new ArrayList<>();
        for (CartItem item : orderedItems) {
            orderedProducts.add(item.getProduct());
        }
        for (Product product : orderedProducts) {
            product.displayDetails();
        }
    }

    public void saveToDatabase(int userID){
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            // Insert the order into the orders table and get the generated order_id
            String query = "INSERT INTO orders (order_id, order_status, order_date, user_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, this.orderID);
                preparedStatement.setString(2, "Paid");
                preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.setInt(4, userID);
                preparedStatement.executeUpdate();

                // Get the generated order_id
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        this.orderID = resultSet.getInt(1);
                    }
                }
            }

            // Insert the order items into the order_items table
            query = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (CartItem item : orderedItems) {
                    preparedStatement.setInt(1, this.orderID);
                    preparedStatement.setInt(2, item.getProduct().getProductID());
                    preparedStatement.setInt(3, item.getQuantity());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setOrderID(int orderId) {
        this.orderID = orderId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderedItems(ArrayList<CartItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate=" + orderDate +
                ", orderedItems=" + orderedItems +
                '}';
    }
}

