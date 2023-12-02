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
            // Create a Dialog
            Dialog<Order> dialog = new Dialog<>();
            dialog.setTitle("Order Details");
            dialog.setHeaderText("Details of Order " + selectedOrder.getOrderID());

            // Create a grid pane for the products and buttons
            GridPane grid = new GridPane();
            grid.setHgap(10); // Horizontal spacing
            grid.setVgap(10); // Vertical spacing
            grid.setPadding(new Insets(20, 20, 20, 20)); // Padding

            // Loop through the products in the order
            for (int i = 0; i < selectedOrder.getOrderedItems().size(); i++) {
                CartItem cartItem = selectedOrder.getOrderedItems().get(i);
                Product product = cartItem.getProduct();

                // Create a label for the product
                Label productLabel = new Label(product.getName() + " (Quantity: " + cartItem.getQuantity() + ")");
                productLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                // Create a "Review" button for the product
                Button reviewButton = new Button("Review");
                reviewButton.setOnAction(event -> {
                    // Create a Dialog for the review
                    Dialog<Review> reviewDialog = new Dialog<>();
                    reviewDialog.setTitle("Review Product");
                    reviewDialog.setHeaderText("Review of " + product.getName());

                    // Create a TextField for the feedback and a ComboBox for the rating
                    TextField feedbackField = new TextField();
                    feedbackField.setPromptText("Enter your feedback");
                    ComboBox<Integer> ratingComboBox = new ComboBox<>();
                    ratingComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
                    ratingComboBox.setPromptText("Select a rating");

                    // Create a grid pane for the feedback and rating
                    GridPane reviewGrid = new GridPane();
                    reviewGrid.setHgap(10); // Horizontal spacing
                    reviewGrid.setVgap(10); // Vertical spacing
                    reviewGrid.setPadding(new Insets(20, 20, 20, 20)); // Padding
                    reviewGrid.add(new Label("Feedback:"), 1, 1);
                    reviewGrid.add(feedbackField, 2, 1);
                    reviewGrid.add(new Label("Rating:"), 1, 2);
                    reviewGrid.add(ratingComboBox, 2, 2);

                    // Set the content of the review dialog pane
                    reviewDialog.getDialogPane().setContent(reviewGrid);

                    // Add a "Submit" button to the review dialog
                    ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                    reviewDialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

                    // Handle the result when the "Submit" button is clicked
                    reviewDialog.setResultConverter(buttonType -> {
                        if (buttonType == submitButtonType) {
                            // Create a new Review
                            Review review = new Review(
                                    Review.generateReviewID(),
                                    ratingComboBox.getValue(),
                                    product.getProductID(),
                                    feedbackField.getText(),
                                    java.sql.Date.valueOf(LocalDate.now())
                            );
                            // Save the review to the database
                            review.SaveReviewToDatabase();
                        }
                        return null;
                    });

                    // Show the review dialog
                    reviewDialog.showAndWait();
                });

                // Add the product label and button to the grid
                grid.add(productLabel, 1, i + 1);
                grid.add(reviewButton, 2, i + 1);
            }

            // Set the content of the dialog pane
            dialog.getDialogPane().setContent(grid);

            // Add some styling to the dialog pane
            dialog.getDialogPane().setStyle("-fx-background-color: #f0f0f0;");

            // Add a DialogEvent to handle the close request
            dialog.setOnCloseRequest(event -> dialog.close());

            // Show the dialog
            Platform.runLater(dialog::showAndWait);
        }
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
