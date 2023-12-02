package com.example.clothingapp;

import com.example.clothingapp.Controller.*;
import com.example.clothingapp.Model.Customer;
import com.example.clothingapp.Schemas.Inventory;
import com.example.clothingapp.Schemas.Product;
import com.example.clothingapp.System.SystemSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;


import java.io.IOException;

public class ClothingStoreApp extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            loadAllData();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 900);

            // Set the SignInController on the FXMLLoader
            LogInController LogIn = loader.getController();

            primaryStage.setTitle("Clothing Store App");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> {
//                saveAllData(); // Save all data when the application is closed
            });
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllData() {
        // Load all data from database
        SystemSettings.loadFromDatabase();
        ArrayList<Product>products = Inventory.loadFromDatabase();
        Inventory.setProducts(products);
    }

//    private void saveAllData() {
//        // Save all data to database
//        SystemSettings.saveToDatabase();
//        ArrayList<Product>products = Inventory.listProducts();
//        for (Product product : products) {
//            product.saveToDatabase();
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }

}
