package com.example.clothingapp.Controller;

import com.example.clothingapp.Model.Customer;
import com.example.clothingapp.Schemas.CartItem;
import com.example.clothingapp.Schemas.Order;
import com.example.clothingapp.Schemas.Product;
import com.example.clothingapp.Schemas.Review;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController{

    @FXML
    private BorderPane orderPane;

    @FXML
    private Label orderSummaryLabel;

    @FXML
    private ListView<Order> orderListView;

    @FXML
    private Button backButton;

    Customer customer;

    @FXML
    public void initialize(Customer customer) {
        // Initialize your UI components or any other setup
        this.customer = customer;
        fillOrderListView();
    }

    @FXML
    private void backToProducts() {
        // Handle back to products action
    }
    @FXML
    private void showOrderDetail() {
        // Get the selected order
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            // Fetch the CartItems from the Order
            List<CartItem> orderedItems = selectedOrder.getOrderedItems();

            // Create a Dialog
            Dialog<CartItem> dialog = new Dialog<>();
            dialog.setTitle("Order Details");
            dialog.setHeaderText("Details of Order " + selectedOrder.getOrderID());

            // Create a grid pane for the products and buttons
            GridPane grid = new GridPane();
            grid.setHgap(10); // Horizontal spacing
            grid.setVgap(10); // Vertical spacing
            grid.setPadding(new Insets(20, 20, 20, 20)); // Padding

            // Loop through the CartItems
            for (int i = 0; i < orderedItems.size(); i++) {
                CartItem cartItem = orderedItems.get(i);

                // Create a label for the product and quantity
                Label productLabel = new Label(cartItem.getProduct().getName() + " (Quantity: " + cartItem.getQuantity() + ")");

                // Create a "Review" button for the product
                Button reviewButton = new Button("Review");
                reviewButton.setOnAction(event -> {
                    // Open a new dialog for reviewing the product
                    ReviewProduct(cartItem.getProduct());
                });

                // Add the product label and button to the grid
                grid.add(productLabel, 1, i + 1);
                grid.add(reviewButton, 2, i + 1);
            }

            // Add a "Cancel" button to the dialog
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

            // Set the content of the dialog pane
            dialog.getDialogPane().setContent(grid);

            // Show the dialog
            dialog.showAndWait();
        }
    }
    private void ReviewProduct(Product product){
        // Create a Dialog
        Dialog<Review> dialog = new Dialog<>();
        dialog.setTitle("Review Product");
        dialog.setHeaderText("Review " + product.getName());

        // Create labels and fields for the review
        Label ratingLabel = new Label("Rating (1-5):");
        TextField ratingField = new TextField();
        Label feedbackLabel = new Label("Feedback:");
        TextField feedbackField = new TextField();

        // Create a "Submit" button
        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        // Create a grid pane and add the labels, fields, and button
        GridPane grid = new GridPane();
        grid.add(ratingLabel, 1, 1);
        grid.add(ratingField, 2, 1);
        grid.add(feedbackLabel, 1, 2);
        grid.add(feedbackField, 2, 2);

        dialog.getDialogPane().setContent(grid);

        // Handle the result when the "Submit" button is clicked
        dialog.setResultConverter(buttonType -> {
            if (buttonType == submitButtonType) {
                // Handle submit action
                int reviewID = Review.generateReviewID();
                int rating = Integer.parseInt(ratingField.getText());
                String feedback = feedbackField.getText();
                Date date = new Date(System.currentTimeMillis());
                Review review = new Review(reviewID, rating, product.getProductID(), feedback, date);
                review.SaveReviewToDatabase();
            }
            return null;
        });

        // Show the dialog
        dialog.showAndWait();
    }

    @FXML
    private void placeOrder() {
        // Handle place order action
    }
    public void fillOrderListView() {
        // Fetch the orders from the Customer
        List<Order> orders = customer.getOrders();

        // Clear the ListView
        orderListView.getItems().clear();

        // Create an ObservableList from the orders
        ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);

        // Add the ObservableList to the ListView
        orderListView.setItems(observableOrders);
    }
    @FXML
    private void goBack(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/clothingapp/Customer.fxml"));

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            stage.setScene(scene);


            // Assuming your signup page controller has a method to set up initial data.
            CustomerController customerController = fxmlLoader.getController();
            customerController.initialize(customer);  // Adjust this line accordingly.

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., show an error message.
        }
    }
}
