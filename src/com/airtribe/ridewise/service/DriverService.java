package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private final List<Driver> drivers = new ArrayList<>();

    public Driver registerDriver(String name, String location, VehicleType vehicleType) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Driver name cannot be empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidInputException("Driver location cannot be empty.");
        }
        if (vehicleType == null) {
            throw new InvalidInputException("Vehicle type is required.");
        }

        Driver driver = new Driver(
                IdGenerator.nextDriverId(),
                name.trim(),
                location.trim(),
                vehicleType
        );
        drivers.add(driver);
        return driver;
    }

    public void updateAvailability(int driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        driver.setAvailable(available);
    }

    public Driver getDriverById(int id) {
        for (Driver driver : drivers) {
            if (driver.getId() == id) {
                return driver;
            }
        }
        throw new InvalidInputException("Driver not found with ID: " + id);
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers);
    }
}
