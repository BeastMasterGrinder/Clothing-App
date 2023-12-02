package com.example.clothingapp.Controller;



import com.example.clothingapp.Model.StoreOwner;
import com.example.clothingapp.Model.User;
import com.example.clothingapp.Schemas.Inventory;
import com.example.clothingapp.Schemas.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class StoreOwnerController {

    @FXML
    private Button addProductButton;

    @FXML
    private Button updateProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button listProductsButton;

    @FXML
    private Button generateSalesReportButton;

    @FXML
    private Button backButton;

    StoreOwner storeOwner;

    // Add more FXML elements as needed

    @FXML
    public void initialize(User user) {
        // Add any initialization code here
        storeOwner = (StoreOwner) user;
        storeOwner.getInventory().loadFromDatabase();
    }


    @FXML
    private void handleAddProduct() {
        // Create a Dialog
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Enter product details");

        // Create labels and fields
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label stockLabel = new Label("Stock:");
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();

        // Create a grid pane and add the labels and fields
        GridPane grid = new GridPane();
        grid.add(nameLabel, 1, 2);
        grid.add(nameField, 2, 2);
        grid.add(priceLabel, 1, 3);
        grid.add(priceField, 2, 3);
        grid.add(stockLabel, 1, 4);
        grid.add(stockField, 2, 4);
        dialog.getDialogPane().setContent(grid);

        // Create a button type for the "Add" button
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Add an event filter to handle the "Add" button click
        dialog.getDialogPane().lookupButton(addButtonType).addEventFilter(ActionEvent.ACTION, event -> {
            String name = nameField.getText();
            String priceText = priceField.getText();
            String stockText = stockField.getText();

            if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                showAlert("All fields must be filled");
                event.consume(); // Stop the event from propagating
                return;
            }

            try {
                Double.parseDouble(priceText);
                Integer.parseInt(stockText);
            } catch (NumberFormatException e) {
                showAlert("Price must be a number and stock must be an integer");
                event.consume(); // Stop the event from propagating
                return;
            }
        });

        // Handle the result when the "Add" button is clicked
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                Product product = new Product(Product.getIDFromDatabase() + 1,name, price, stock);
                product.saveToDatabase();
                return product;
            }
            return null;
        });

        // Show the dialog and get the result
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            storeOwner.addProduct(product);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Added");
            alert.setHeaderText(null);
            alert.setContentText("Product added: \n" +
                    "Name: " + product.getName() + "\n" +
                    "Price: " + product.getPrice() + "\n" +
                    "In Stock: " + product.getInStock());
            alert.showAndWait();
        });
    }



    @FXML
    private void handleUpdateProduct() {
        // Create a Dialog
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Update Product");
        dialog.setHeaderText("Enter new product details");

        // Create labels and fields
        Label productLabel = new Label("Product:");
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label stockLabel = new Label("Stock:");
        ChoiceBox<Product> productChoiceBox = new ChoiceBox<>();
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();

        // Populate the ChoiceBox with the names of all the products
        productChoiceBox.getItems().addAll(storeOwner.getInventory().listProducts());

        // Add a listener to the ChoiceBox to update the TextFields when a product is selected
        productChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                priceField.setText(String.valueOf(newValue.getPrice()));
                stockField.setText(String.valueOf(newValue.getInStock()));
            }
        });

        // Create a grid pane and add the labels, fields, and ChoiceBox
        GridPane grid = new GridPane();
        grid.add(productLabel, 1, 1);
        grid.add(productChoiceBox, 2, 1);
        grid.add(nameLabel, 1, 2);
        grid.add(nameField, 2, 2);
        grid.add(priceLabel, 1, 3);
        grid.add(priceField, 2, 3);
        grid.add(stockLabel, 1, 4);
        grid.add(stockField, 2, 4);
        dialog.getDialogPane().setContent(grid);

        // Create a button type for the "Update" button
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Add an event filter to handle the "Update" button click
        dialog.getDialogPane().lookupButton(updateButtonType).addEventFilter(ActionEvent.ACTION, event -> {
            String name = nameField.getText();
            String priceText = priceField.getText();
            String stockText = stockField.getText();

            if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                showAlert("All fields must be filled");
                event.consume(); // Stop the event from propagating
                return;
            }

            try {
                Double.parseDouble(priceText);
                Integer.parseInt(stockText);
            } catch (NumberFormatException e) {
                showAlert("Price must be a number and stock must be an integer");
                event.consume(); // Stop the event from propagating
                return;
            }
        });

        // Handle the result when the "Update" button is clicked
        dialog.setResultConverter(buttonType -> {
            if (buttonType == updateButtonType) {
                Product product = productChoiceBox.getSelectionModel().getSelectedItem();
                if (product != null) {
                    product.setName(nameField.getText());
                    product.setPrice(Double.parseDouble(priceField.getText()));
                    product.setInStock(Integer.parseInt(stockField.getText()));
                    Product.updateProductInDatabase(product);
                    return product;
                }
            }
            return null;
        });

        // Show the dialog and get the result
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            storeOwner.updateProduct(product);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Updated");
            alert.setHeaderText(null);
            alert.setContentText("Product updated: \n" +
                    "Name: " + product.getName() + "\n" +
                    "Price: " + product.getPrice() + "\n" +
                    "In Stock: " + product.getInStock());
            alert.showAndWait();
        });
    }

    @FXML
    private void handleDeleteProduct() {
        // Create a Dialog
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Delete Product");
        dialog.setHeaderText("Select a product to delete");

        // Create labels and ChoiceBox
        Label productLabel = new Label("Product:");
        ChoiceBox<Product> productChoiceBox = new ChoiceBox<>();

        // Populate the ChoiceBox with the names of all the products
        productChoiceBox.getItems().addAll(storeOwner.getInventory().listProducts());

        // Create a grid pane and add the labels and ChoiceBox
        GridPane grid = new GridPane();
        grid.add(productLabel, 1, 1);
        grid.add(productChoiceBox, 2, 1);
        dialog.getDialogPane().setContent(grid);

        // Create a button type for the "Delete" button
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        // Handle the result when the "Delete" button is clicked
        dialog.setResultConverter(buttonType -> {
            if (buttonType == deleteButtonType) {
                Product product = productChoiceBox.getSelectionModel().getSelectedItem();
                if (product != null) {
                    product.deleteProductFromDatabase();
                    return product;
                }
            }
            return null;
        });

        // Show the dialog and get the result
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> {
            storeOwner.removeProduct(product);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Deleted");
            alert.setHeaderText(null);
            alert.setContentText("Product deleted: \n" +
                    "Name: " + product.getName() + "\n" +
                    "Price: " + product.getPrice() + "\n" +
                    "In Stock: " + product.getInStock());
            alert.showAndWait();
        });
    }

    @FXML
    private void handleListProducts() {
        // Create a Dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("List Products");

        // Create a ListView and populate it with the names of all the products
        ListView<Product> listView = new ListView<>();
        listView.getItems().addAll(Inventory.listProducts());

        // Add the ListView to the Dialog
        dialog.getDialogPane().setContent(listView);

        // Add a "Close" button to the Dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Show the dialog
        dialog.showAndWait();
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Shop Owner Page");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
