package com.airtribe.ridewise.model;

public class Driver {
    private final int id;
    private final String name;
    private String currentLocation;
    private boolean available;
    private final VehicleType vehicleType;
    private int activeRides;

    public Driver(int id, String name, String currentLocation,
                  VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicleType = vehicleType;
        this.available = true;
        this.activeRides = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getActiveRides() {
        return activeRides;
    }

    public void incrementActiveRides() {
        activeRides++;
    }

    public void decrementActiveRides() {
        if (activeRides > 0) {
            activeRides--;
        }
    }

    @Override
    public String toString() {
        return "Driver{id=" + id + ", name='" + name + "', location='" +
                currentLocation + "', vehicleType=" + vehicleType +
                ", available=" + available + ", activeRides=" + activeRides + "}";
    }
}
