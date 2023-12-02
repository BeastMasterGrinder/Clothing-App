package com.example.clothingapp.System;

import java.sql.*;

// SystemSettings class
public class SystemSettings {
    private static double taxRate;
    private static String currency;

    public SystemSettings(double taxRate, String currency) {
        this.taxRate = taxRate;
        this.currency = currency;
    }

    public static SystemSettings getInstance() {
        return new SystemSettings(0.0, "");
    }

    public void setTaxRate(double tax) {
        // Implementation for setting tax rate
        this.taxRate = tax;
    }

    public void setCurrency(String curr) {
        // Implementation for setting currency
        this.currency = curr;
    }

    public static String getCurrency() {
        // Implementation for getting currency
        return currency;
    }

    public static double getTaxRate() {
        return taxRate;
    }

    public static void saveToDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123")) {
            String query = "INSERT INTO system_settings (tax_rate, currency) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, getTaxRate());
                preparedStatement.setString(2, getCurrency());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SystemSettings  loadFromDatabase(){
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/ClothingDB", "Admin1", "Admin123"))
        {
            String query = "SELECT * FROM system_settings ORDER BY id DESC LIMIT 1";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                try(ResultSet resultSet = preparedStatement.executeQuery())
                {
                    if(resultSet.next())
                    {
                        System.out.println("System settings loaded from database");

                        return new SystemSettings(
                                resultSet.getDouble("tax_rate"),
                                resultSet.getString("currency")
                        );

                    }
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}