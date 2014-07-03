/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.util.Date;

/**
 *
 * @author HP USER
 */
public class BookingRequest {
    private Date checkInDate;
    private String roomtype;
    private int numberOfDays = -1;
    private int numberOfRooms = -1;
    private double pricePerRoom = -1;
    private double totalPrice = -1;

    public BookingRequest(Date checkInDate, String roomtype, int numberOfDays, int numberOfRooms, double pricePerRoom) {
        this.checkInDate = checkInDate;
        this.roomtype = roomtype;
        this.numberOfDays = numberOfDays;
        this.numberOfRooms = numberOfRooms;
        this.pricePerRoom = pricePerRoom;
        this.totalPrice = numberOfRooms * this.pricePerRoom * this.numberOfDays;
    }

    /**
     * @return the checkInDate
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * @return the numberOfDays
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    /**
     * @return the numberOfRooms
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * @return the pricePerRoom
     */
    public double getPricePerRoom() {
        return pricePerRoom;
    }

    /**
     * @return the totalPrice
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return the roomtype
     */
    public String getRoomtype() {
        return roomtype;
    }
    
    
    
}
