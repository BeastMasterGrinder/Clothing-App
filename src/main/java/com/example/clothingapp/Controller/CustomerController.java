package com.example.clothingapp.Controller;



import com.example.clothingapp.Model.Customer;
import com.example.clothingapp.Model.User;
import com.example.clothingapp.Schemas.*;
import com.example.clothingapp.System.SystemSettings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CustomerController{

    @FXML
    private BorderPane customerPane;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private TextField  searchTextField;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private Button ViewShopButton;

    @FXML
    private Button backButton;

    List<Product> inventory = Inventory.listProducts();
    Customer customer ;

    @FXML
    public void initialize(User user) {
        // Initialize your UI components or any other setup
        customer = (Customer) user;
        customer.loadOrdersFromDatabase();
        refreshProductList();
    }

    @FXML
    private void viewShoppingCart() {
        // Get the CartItems from the ShoppingCart
        List<CartItem> cartItems = customer.getCart().getItems();

        if (!cartItems.isEmpty()) {
            // Create a Dialog
            Dialog<CartItem> dialog = new Dialog<>();
            dialog.setTitle("Shopping Cart");
            dialog.setHeaderText("Products in the Shopping Cart");

            // Create a grid pane for the products and buttons
            GridPane grid = new GridPane();
            grid.setHgap(10); // Horizontal spacing
            grid.setVgap(10); // Vertical spacing
            grid.setPadding(new Insets(20, 20, 20, 20)); // Padding

            // Loop through the CartItems
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem cartItem = cartItems.get(i);

                // Create a label for the product and quantity
                Label productLabel = new Label(cartItem.getProduct().getName() + " (Quantity: " + cartItem.getQuantity() + ")");

                // Create a TextField for the new quantity
                TextField quantityField = new TextField();
                quantityField.setPromptText("Enter new quantity");

                // Create a "Remove" button for the product
                Button removeButton = new Button("Remove");
                removeButton.setOnAction(event -> {
                    // Remove the CartItem from the cart

                    ShoppingCart temp = customer.getCart();
                    temp.deleteProduct(cartItem.getProduct(), customer);
                    customer.setCart(temp);

                    // Remove the product label, TextField, and button from the dialog
                    grid.getChildren().removeAll(productLabel, quantityField, removeButton);
                });

                // Add the product label, TextField, and button to the grid
                grid.add(productLabel, 1, i + 1);
                grid.add(quantityField, 2, i + 1);
                grid.add(removeButton, 3, i + 1);
            }

            // Create an "Order" button
            ButtonType orderButtonType = new ButtonType("Order", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(orderButtonType, ButtonType.CANCEL);

            // Handle the result when the "Order" button is clicked
            dialog.setResultConverter(buttonType -> {
                if (buttonType == orderButtonType) {
                    // Handle order action
                    OrderFromCart(customer);

                }
                return null;
            });

            // Set the content of the dialog pane
            dialog.getDialogPane().setContent(grid);

            // Show the dialog
            dialog.showAndWait();
        } else {
            // Show a message if the cart is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Shopping Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your shopping cart is empty.");

            alert.showAndWait();
        }
    }

    @FXML
    private void showProductDetails() {
        // Get the selected product
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            // Fetch the reviews of the selected product
            List<Review> reviews = selectedProduct.getReviews();

            // Create a Dialog
            Dialog<Product> dialog = new Dialog<>();
            dialog.setTitle("Product Details");
            dialog.setHeaderText("Details of " + selectedProduct.getName());

            // Create labels and fields for product details
            Label nameLabel = new Label("Name: " + selectedProduct.getName());
            Label priceLabel = new Label("Price: " + selectedProduct.getPrice());
            Label stockLabel = new Label("Stock: " + selectedProduct.getInStock());

            // Create a TextField for the quantity
            TextField quantityField = new TextField();
            quantityField.setPromptText("Enter quantity");

            // Create buttons for actions
            ButtonType orderButtonType = new ButtonType("Order", ButtonBar.ButtonData.OK_DONE);
            ButtonType addToCartButtonType = new ButtonType("Add to ShoppingCart", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(orderButtonType, addToCartButtonType, ButtonType.CANCEL);

            // Create a grid pane and add the labels, fields, and buttons
            GridPane grid = new GridPane();
            grid.add(nameLabel, 1, 1);
            grid.add(priceLabel, 1, 2);
            grid.add(stockLabel, 1, 3);
            grid.add(quantityField, 1, 4);

            // Add the reviews to the grid
            for (int i = 0; i < reviews.size(); i++) {
                Review review = reviews.get(i);
                Label reviewLabel = new Label("Review by " + review.getCustomerName() + ": " + review.getFeedback() + " (Rating: " + review.getRating() + ")");
                grid.add(reviewLabel, 1, i + 5);
            }

            dialog.getDialogPane().setContent(grid);

            // Handle the result when the "Order" button is clicked
            dialog.setResultConverter(buttonType -> {
                if (buttonType == orderButtonType || buttonType == addToCartButtonType) {
                    // Check if the quantity field is empty
                    if (quantityField.getText().isEmpty()) {
                        // Show an alert
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a quantity.");

                        alert.showAndWait();
                        return null;
                    }

                    // Handle order action
                    int quantity = Integer.parseInt(quantityField.getText());
                    if (buttonType == orderButtonType) {

                        if(OrderFromView(customer, selectedProduct, quantity))
                            customer.addtoOrder(selectedProduct, quantity);
                    } else if (buttonType == addToCartButtonType) {
                        // Handle add to cart action
                        customer.addToCart(selectedProduct, quantity);
                    }
                }
                return null;
            });

            // Show the dialog
            dialog.showAndWait();
        }
    }

    @FXML
    private void searchProducts() {
        String searchTerm = searchTextField.getText().toLowerCase();

        List<Product> filteredProducts = inventory.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        updateProductListView(filteredProducts);
    }

    @FXML
    private void filterProducts() {
        String filterOption = filterChoiceBox.getValue();

        List<Product> sortedProducts;

        switch (filterOption) {
            case "Price: Low to High":
                sortedProducts = inventory.stream().sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice())).collect(Collectors.toList());
                break;
            case "Price: High to Low":
                sortedProducts = inventory.stream().sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice())).collect(Collectors.toList());
                break;
            default:
                sortedProducts = inventory;
        }

        updateProductListView(sortedProducts);
    }

    private void refreshProductList() {
        productListView.getItems().clear();
        ObservableList<Product> observableInventory = FXCollections.observableArrayList(inventory);
        productListView.setItems(observableInventory);
    }
    private void updateProductListView(List<Product> updatedList) {
        ObservableList<Product> observableList = FXCollections.observableArrayList(updatedList);
        productListView.setItems(observableList);
    }

    @FXML
    public void showOrders(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/Order.fxml"));

            Stage stage = (Stage) ViewShopButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);

            stage.setScene(scene);


            // Assuming your signup page controller has a method to set up initial data.
            OrderController orderController = fxmlLoader.getController();
            orderController.initialize(customer);  // Adjust this line accordingly.

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message.
        }
    }

    private boolean OrderFromCart(Customer customer){
        // Get the CartItems from the ShoppingCart
        List<CartItem> cartItems = customer.getCart().getItems();

        if (!cartItems.isEmpty()) {
            // Calculate the total amount
            double totalAmount = 0.0;
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                int quantity = cartItem.getQuantity();
                if (quantity > product.getInStock()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("The quantity ordered for " + product.getName() + " is greater than the stock available.");
                    alert.showAndWait();
                    return false;
                }
                totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
                product.setInStock(product.getInStock() - quantity);
                if(product.getInStock() != 0)
                    product.updateProductInDatabase(product);
                else if(product.getInStock() == 0)
                    product.deleteProductFromDatabase();
            }
            totalAmount += totalAmount * (SystemSettings.getTaxRate()/100);

            // Create a Dialog
            Dialog<CartItem> dialog = new Dialog<>();
            dialog.setTitle("Order Confirmation");
            dialog.setHeaderText("Confirm your order");

            // Create a label for the total amount
            Label totalLabel = new Label("Total Amount: " + totalAmount);

            // Create a "Confirm" button
            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            // Set the content of the dialog pane
            dialog.getDialogPane().setContent(totalLabel);

            // Handle the result when the "Confirm" button is clicked
            double finalTotalAmount = totalAmount;
            dialog.setResultConverter(buttonType -> {
                if (buttonType == confirmButtonType) {
                    // Handle confirm action
                    customer.checkOut(finalTotalAmount);
                }
                return null;
            });

            // Show the dialog
            dialog.showAndWait();
        } else {
            // Show a message if the cart is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Shopping Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your shopping cart is empty.");

            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean OrderFromView(Customer customer, Product product, int quantity){
        CartItem OrderedcartItem = new CartItem(product, quantity);
        // Get the CartItems from the ShoppingCart
        customer.getCart().clearCart();
        ArrayList<CartItem> cartItems = customer.getCart().getItems();
        cartItems.add(OrderedcartItem);
        customer.setCartItems(cartItems);

        if (!cartItems.isEmpty()) {
            // Calculate the total amount
            double totalAmount = 0.0;
            for (CartItem item : cartItems) {
                if (quantity > product.getInStock()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("The quantity ordered for " + product.getName() + " is greater than the stock available.");
                    alert.showAndWait();
                    return false;
                }
                totalAmount += item.getProduct().getPrice() * item.getQuantity();
                product.setInStock(product.getInStock() - quantity);
                Product.updateProductInDatabase(product);
            }
            totalAmount += totalAmount * (SystemSettings.getTaxRate()/100);

            // Create a Dialog
            Dialog<CartItem> dialog = new Dialog<>();
            dialog.setTitle("Order Confirmation");
            dialog.setHeaderText("Confirm your order");

            // Create a label for the total amount
            Label totalLabel = new Label("Total Amount: " + totalAmount);

            // Create a "Confirm" button
            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            // Set the content of the dialog pane
            dialog.getDialogPane().setContent(totalLabel);

            // Handle the result when the "Confirm" button is clicked
            double finalTotalAmount = totalAmount;
            dialog.setResultConverter(buttonType -> {
                if (buttonType == confirmButtonType) {
                    // Handle confirm action
                    customer.checkOut(finalTotalAmount);
                }
                return null;
            });

            // Show the dialog
            dialog.showAndWait();
        } else {
            // Show a message if the cart is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Shopping Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your shopping cart is empty.");

            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    private void goBack(){
        logOut();
    }
    @FXML
    private void logOut(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/LogIn.fxml"));

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            stage.setScene(scene);


            // Assuming your signup page controller has a method to set up initial data.
            LogInController logInController = fxmlLoader.getController();
            logInController.initialize();  // Adjust this line accordingly.

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message.
        }
    }
}
