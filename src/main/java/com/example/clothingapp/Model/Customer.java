package com.example.clothingapp.Model;
import  com.example.clothingapp.Schemas.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Customer class
public class Customer extends User {
    private int customerID;
    private ShoppingCart cart;
    private ArrayList<Order> orders;

    public Customer(String name, String email, String password, int customerID) {
        super(name, email, password, "Customer");
        this.customerID = customerID + 1;
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }

    public void orderProducts() {
        // Implementation for ordering products

    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void reviewProduct(Review choice) {
        // Implementation for reviewing a product
    }

    public void searchAndFilterInventory(String filter) {
        // Implementation for searching and filtering inventory
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addToCart(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.addProduct(product);
        }
    }

    public void addtoOrder(Product product, int quantity) {
        CartItem cartItem = new CartItem(product, quantity);
        for (int i = 0; i < quantity; i++) {
            ArrayList<CartItem> itemList = new ArrayList<>();
            itemList.add(cartItem);
            orders.add(new Order(itemList));
        }
    }

    public void checkOut(Double total){
        // Implementation for checking out
        ArrayList<CartItem> items = cart.getItems();
        Order order = new Order(items);
        orders.add(order);
        order.pay(total);
        cart.clearCart();
    }

    public void trackOrder(int orderID) {
        // Implementation for tracking an order
        Order order = findOrder(orderID);
        if (order != null) {
            order.trackOrder();
        } else {
            // Handle order not found
        }
    }

    private Order findOrder(int orderID) {
        for (Order order : orders) {
            if (order.getOrderID() == (orderID)) {
                return order;
            }
        }
        return null;
    }

    // In Customer.java
    public void loadOrdersFromDatabase() {
        orders.clear();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            // Fetch the orders from the database
            String query = "SELECT * FROM orders WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, customerID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Create a new Order
                        Order order = new Order(new ArrayList<>());
                        order.setOrderID(resultSet.getInt("order_id"));
                        order.setOrderStatus(resultSet.getString("order_status"));
                        order.setOrderDate(resultSet.getDate("order_date"));

                        // Fetch the order items from the database
                        String query2 = "SELECT * FROM order_items WHERE order_id = ?";
                        try (PreparedStatement preparedStatement2 = connection.prepareStatement(query2)) {
                            preparedStatement2.setInt(1, order.getOrderID());
                            try (ResultSet resultSet2 = preparedStatement2.executeQuery()) {
                                while (resultSet2.next()) {
                                    // Create a new CartItem
                                    Product product = Inventory.getProduct(resultSet2.getInt("product_id"));
                                    int quantity = resultSet2.getInt("quantity");
                                    CartItem cartItem = new CartItem(product, quantity);

                                    // Add the CartItem to the Order
                                    order.addProduct(product, quantity);
                                }
                            }
                        }

                        // Add the Order to the orders ArrayList
                        orders.add(order);
                    }
                    System.out.println("Orders loaded from database");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCart(ArrayList<CartItem> cart){
        this.cart.setItems(cart);
    }
}
