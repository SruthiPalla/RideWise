package com.airtribe.ridewise.model;

public class Ride {
    private final int id;
    private final Rider rider;
    private Driver driver;
    private final double distance;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(int id, Rider rider, double distance) {
        this.id = id;
        this.rider = rider;
        this.distance = distance;
        this.status = RideStatus.REQUESTED;
    }

    public int getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = RideStatus.ASSIGNED;
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void complete() {
        this.status = RideStatus.COMPLETED;
    }

    public void cancel() {
        this.status = RideStatus.CANCELLED;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void generateFareReceipt(double amount) {
        this.fareReceipt = new FareReceipt(id, amount);
    }

    @Override
    public String toString() {
        String driverName = driver == null ? "Not assigned" : driver.getName();
        return "Ride{id=" + id + ", rider=" + rider.getName() +
                ", driver=" + driverName + ", distance=" + distance +
                ", status=" + status +
                ", fare=" + (fareReceipt == null ? "Not generated" : fareReceipt.getAmount()) + "}";
    }
}
