/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclesalesfx;

/**
 *
 * @author Ricky Thain
 */
public class Sales {
    // attributes in sales
    final private Integer QTR, Quantity, Year;
    final private String Region, Vehicle;
    
    public Sales(Integer qtr, Integer quantity, String region, String vehicle, Integer year){
        this.QTR = qtr;
        this.Quantity = quantity;
        this.Region = region;
        this.Vehicle = vehicle;
        this.Year = year;
    }
    
    @Override
    public String toString() {
        return String.format("%s%s%s%s%s", 
        ("QTRL:" + QTR + " "), ("Quantity:" + Quantity + " "), ("Region:" + Region + " "),
        ("Vehicle:" + Vehicle + " "), ("Year:" + Year + " "));
    }

    public Integer getQTR() {
        return QTR;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Integer getYear() {
        return Year;
    }

    public String getRegion() {
        return Region;
    }

    public String getVehicle() {
        return Vehicle;
    }
    
    
}
