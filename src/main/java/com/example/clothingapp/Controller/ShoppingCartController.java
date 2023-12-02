package com.example.clothingapp.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ShoppingCartController implements Initializable {

    @FXML
    private BorderPane shoppingCartPane;

    @FXML
    private JFXListView<String> cartListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize your UI components or any other setup
    }

    @FXML
    private void backToProducts() {
        // Handle back to products action
    }

    @FXML
    private void orderShoppingCart() {
        // Handle order shopping cart action
    }

    @FXML
    private void removeFromCart() {
        // Handle remove from cart action
    }
}
