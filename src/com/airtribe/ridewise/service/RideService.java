package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class RideService {
    private final DriverService driverService;
    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;
    private final List<Ride> rides = new ArrayList<>();

    public RideService(DriverService driverService,
                       RideMatchingStrategy matchingStrategy,
                       FareStrategy fareStrategy) {
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(Rider rider, double distance) {
        if (rider == null) {
            throw new InvalidInputException("Rider is required.");
        }
        if (distance <= 0) {
            throw new InvalidInputException("Distance must be greater than zero.");
        }

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        Driver driver = matchingStrategy.findDriver(rider, availableDrivers);

        if (driver == null) {
            throw new NoDriverAvailableException("No driver is currently available.");
        }

        Ride ride = new Ride(IdGenerator.nextRideId(), rider, distance);
        ride.assignDriver(driver);

        driver.setAvailable(false);
        driver.incrementActiveRides();

        double fare = fareStrategy.calculateFare(ride);
        ride.generateFareReceipt(fare);

        rides.add(ride);
        return ride;
    }

    public Ride completeRide(int rideId) {
        Ride ride = getRideById(rideId);

        if (ride.getDriver() == null) {
            throw new InvalidInputException("Ride has no assigned driver.");
        }

        if (ride.getStatus() != com.airtribe.ridewise.model.RideStatus.ASSIGNED) {
            throw new InvalidInputException("Only an assigned ride can be completed.");
        }

        ride.complete();

        Driver driver = ride.getDriver();
        driver.decrementActiveRides();
        driver.setAvailable(true);

        return ride;
    }

    public Ride cancelRide(int rideId) {
        Ride ride = getRideById(rideId);

        if (ride.getStatus() != com.airtribe.ridewise.model.RideStatus.ASSIGNED) {
            throw new InvalidInputException("Only an assigned ride can be cancelled.");
        }

        ride.cancel();

        Driver driver = ride.getDriver();
        driver.decrementActiveRides();
        driver.setAvailable(true);

        return ride;
    }

    public Ride getRideById(int rideId) {
        for (Ride ride : rides) {
            if (ride.getId() == rideId) {
                return ride;
            }
        }
        throw new InvalidInputException("Ride not found with ID: " + rideId);
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides);
    }
}
