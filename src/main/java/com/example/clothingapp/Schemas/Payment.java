package com.example.clothingapp.Schemas;


interface Payment {
    int paymentID = Integer.parseInt(null);
    String paymentMethod = "";
    double Total = 0;

    public void discount();

    public void generateReceipt();


}
