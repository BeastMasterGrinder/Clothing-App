package com.example.clothingapp.Schemas;

import java.util.ArrayList;


// ShoppingCart class
public class ShoppingCart {
    private ArrayList<CartItem> items;

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(Product product) {
        // Implementation for adding a product to the cart
        CartItem cartItem = new CartItem(product, 1);
        items.add(cartItem);
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (CartItem item : items) {
            products.add(item.getProduct());
        }
        return products;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void deleteProduct(Product product) {
        // Implementation for deleting a product from the cart
        ArrayList<Product> products = new ArrayList<>();
        for (CartItem item : items) {
            products.add(item.getProduct());
        }
        for(Product p : products){
            if(p.getProductID() == (product.getProductID())){
                products.remove(p);
                break;
            }
        }
    }

    public ArrayList<Product> listProducts() {
        // Implementation for listing products in the cart
        ArrayList<Product> products = new ArrayList<>();
        for (CartItem item : items) {
            products.add(item.getProduct());
        }
        return products;
    }


    public void clearCart() {
        items.clear();
    }
}
