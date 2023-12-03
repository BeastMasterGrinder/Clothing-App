package com.example.clothingapp.Schemas;

import com.example.clothingapp.Model.Customer;

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

    public void deleteProduct(Product product, Customer customer) {
        // Implementation for deleting a product from the cart

        for (CartItem item : items) {
            Product Newproduct = (item.getProduct());
            if(Newproduct.getProductID() == (product.getProductID())){
                items.remove(item);
                break;
            }
        }
        // Update the cart items in the Customer class
        customer.updateCartItems(items);
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
