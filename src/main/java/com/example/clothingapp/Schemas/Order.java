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

    public void pay(Double Total) {
        // Implementation for making a payment
        this.Total = Total;
        generateReceipt();
        saveToDatabase();
    }

    @Override
    public void discount() {

    }


    @Override
    public void generateReceipt() {
        // Create a Dialog
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Receipt");
        dialog.setHeaderText("Order Receipt");

        // Create a grid pane for the order details
        GridPane grid = new GridPane();
        grid.setHgap(10); // Horizontal spacing
        grid.setVgap(10); // Vertical spacing
        grid.setPadding(new Insets(20, 20, 20, 20)); // Padding

        // Create labels for the order details
        Label orderIdLabel = new Label("Order ID: " + getOrderID());
        Label orderStatusLabel = new Label("Order Status: " + orderStatus);
        Label orderDateLabel = new Label("Order Date: " + orderDate.toString());
        Label totalLabel = new Label("Total: " + getTotal());

        // Add some styling to the labels
        orderIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        orderStatusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        orderDateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Add the labels to the grid
        grid.add(orderIdLabel, 1, 1);
        grid.add(orderStatusLabel, 1, 2);
        grid.add(orderDateLabel, 1, 3);
        grid.add(totalLabel, 1, 4);

        // Set the content of the dialog pane
        dialog.getDialogPane().setContent(grid);

        // Add some styling to the dialog pane
        dialog.getDialogPane().setStyle("-fx-background-color: #b12323;");

        // Add a DialogEvent to handle the close request
        dialog.setOnCloseRequest(event -> dialog.close());

        // Show the dialog on a separate thread
        Platform.runLater(dialog::showAndWait);
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

    public void saveToDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            // Save order details to the database
            String query = "INSERT INTO orders (order_id, order_status, order_date, user_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, String.valueOf(getOrderID()));
                preparedStatement.setString(2, orderStatus);
                preparedStatement.setDate(3, new java.sql.Date(orderDate.getTime()));
                preparedStatement.setString(4, String.valueOf(User.getUserID()));
                System.out.println("Order saved to database");
                preparedStatement.executeUpdate();
            }
            // Save ordered products to the database
            query = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (CartItem item : orderedItems) {
                    Product product = item.getProduct();
                    int quantity = item.getQuantity();
                    preparedStatement.setString(1, String.valueOf(getOrderID()));
                    preparedStatement.setString(2, String.valueOf(product.getProductID()));
                    preparedStatement.setString(3, String.valueOf(quantity));
                    System.out.println("Ordered products saved to database");
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

