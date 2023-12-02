package com.example.clothingapp.Schemas;


import java.sql.*;
import java.util.ArrayList;

// Inventory class
public class Inventory {
    private static ArrayList<Product> products;

    public Inventory() {
        Inventory.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        // Implementation for adding a product
        products.add(product);

    }

    public void updateProduct(Product product) {
        // Implementation for updating a product
        for(int i = 0; i < products.size(); i++) {
            if(products.get(i).getProductID() == (product.getProductID())) {
                products.set(i, product);
                break;
            }
        }
    }

    public void removeProduct(Product product) {
        // Implementation for deleting a product
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductID() == (product.getProductID())) {
                products.remove(i);
                break;
            }
        }
    }

    public static ArrayList<Product> listProducts() {
        // Implementation for listing products
        return products;
    }

    public static Product getProduct(int id) {
        // Implementation for getting product details
        for(int i = 0; i < products.size(); i++) {
            if(products.get(i).getProductID() == id) {
                return products.get(i);
            }
        }
        return null;
    }

    public void productsDetail(int index) {
        // Implementation for getting product details
        Product product = getProduct(index);
        if (product != null) {
            product.displayDetails();
        } else {
            // Handle invalid index or product not found
        }
    }

    public static void setProducts(ArrayList<Product> products) {
        Inventory.products = products;
    }

    //This should be called when the initial inventory is created
    public static ArrayList<Product> loadFromDatabase() {
        products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "SELECT * FROM products";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Only clear the products list if it's not null and not empty
                    if (Inventory.products != null && !Inventory.products.isEmpty()) {
                        Inventory.products.clear();
                    }
                    System.out.println("All Products loaded from database");
                    while (resultSet.next()) {
                        Product product = new Product(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getDouble("price"),
                                resultSet.getInt("in_stock")
                        );
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}