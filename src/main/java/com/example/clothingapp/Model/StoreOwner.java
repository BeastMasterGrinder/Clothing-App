package com.example.clothingapp.Model;

import com.example.clothingapp.Schemas.Inventory;
import com.example.clothingapp.Schemas.Product;

// StoreOwner class
public class StoreOwner extends User {
    private int storeOwnerID;
    private Inventory inventory;

    public StoreOwner(String name, String email, String password, int storeOwnerID) {
        super(name, email, password, "Store Owner");
        this.storeOwnerID = storeOwnerID;
        this.inventory = new Inventory();
    }

    //CRUD operations
    public void addProduct(Product product) {
        inventory.addProduct(product);
    }
    public void removeProduct(Product product) {
        inventory.removeProduct(product);
    }
    public void updateProduct(Product product) {
        inventory.updateProduct(product);
    }
    public Product getProduct(int productID) {
        return inventory.getProduct(productID);
    }
    public Inventory getInventory() {
        return inventory;
    }


    public void generateReport() {
        // Implementation for generating sales report
    }
}