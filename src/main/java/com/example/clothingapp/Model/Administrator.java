package com.example.clothingapp.Model;
import com.example.clothingapp.System.*;

// Administrator class
public class Administrator extends User {
    private int adminID;
    private static SystemSettings systemSettings;

    public Administrator(String name, String email, String password, int adminID) {
        super(name, email, password, "Administrator");
        this.adminID = adminID;
        systemSettings = SystemSettings.getInstance();
    }

    public void setSystemSettings(double tax, String currency) {
        systemSettings.setTaxRate(tax);
        systemSettings.setCurrency(currency);
        systemSettings.saveToDatabase();
        System.out.println("System settings saved to database");
    }

    public SystemSettings getSystemSettings() {
        return systemSettings;
    }
}
