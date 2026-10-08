package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();

        RideMatchingStrategy matchingStrategy = new NearestDriverStrategy();
        FareStrategy fareStrategy = new DefaultFareStrategy();

        RideService rideService = new RideService(
                driverService,
                matchingStrategy,
                fareStrategy
        );

        boolean running = true;

        while (running) {
            printMenu();

            try {
                int choice = readInt("Enter your choice: ");

                switch (choice) {
                    case 1:
                        addRider(riderService);
                        break;
                    case 2:
                        addDriver(driverService);
                        break;
                    case 3:
                        viewAvailableDrivers(driverService);
                        break;
                    case 4:
                        requestRide(riderService, rideService);
                        break;
                    case 5:
                        completeRide(rideService);
                        break;
                    case 6:
                        viewRides(rideService);
                        break;
                    case 7:
                        cancelRide(rideService);
                        break;
                    case 8:
                        running = false;
                        System.out.println("Thank you for using RideWise.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1-8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a numeric value.");
            } catch (InvalidInputException | NoDriverAvailableException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== RideWise =====");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Cancel Ride");
        System.out.println("8. Exit");
    }

    private static void addRider(RiderService riderService) {
        String name = readText("Enter rider name: ");
        String location = readText("Enter rider location: ");

        Rider rider = riderService.registerRider(name, location);
        System.out.println("Rider registered successfully: " + rider);
    }

    private static void addDriver(DriverService driverService) {
        String name = readText("Enter driver name: ");
        String location = readText("Enter driver location: ");
        VehicleType vehicleType = readVehicleType();

        Driver driver = driverService.registerDriver(name, location, vehicleType);
        System.out.println("Driver registered successfully: " + driver);
    }

    private static void viewAvailableDrivers(DriverService driverService) {
        List<Driver> drivers = driverService.getAvailableDrivers();

        if (drivers.isEmpty()) {
            System.out.println("No available drivers.");
            return;
        }

        System.out.println("\nAvailable Drivers:");
        for (Driver driver : drivers) {
            System.out.println(driver);
        }
    }

    private static void requestRide(RiderService riderService, RideService rideService) {
        int riderId = readInt("Enter rider ID: ");
        double distance = readDouble("Enter distance in km: ");

        Rider rider = riderService.getRiderById(riderId);
        Ride ride = rideService.requestRide(rider, distance);

        System.out.println("Ride created successfully.");
        System.out.println(ride);
        System.out.println(ride.getFareReceipt());
    }

    private static void completeRide(RideService rideService) {
        int rideId = readInt("Enter ride ID: ");
        Ride ride = rideService.completeRide(rideId);

        System.out.println("Ride completed successfully.");
        System.out.println(ride);
    }

    private static void cancelRide(RideService rideService) {
        int rideId = readInt("Enter ride ID: ");
        Ride ride = rideService.cancelRide(rideId);

        System.out.println("Ride cancelled successfully.");
        System.out.println(ride);
    }

    private static void viewRides(RideService rideService) {
        List<Ride> rides = rideService.getAllRides();

        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }

        System.out.println("\nAll Rides:");
        for (Ride ride : rides) {
            System.out.println(ride);
        }
    }

    private static VehicleType readVehicleType() {
        System.out.println("Vehicle types: BIKE, AUTO, CAR");
        String value = readText("Enter vehicle type: ").toUpperCase();

        try {
            return VehicleType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid vehicle type.");
        }
    }

    private static String readText(String message) {
        System.out.print(message);
        String value = scanner.nextLine().trim();

        if (value.isEmpty()) {
            throw new InvalidInputException("Input cannot be empty.");
        }

        return value;
    }

    private static int readInt(String message) {
        System.out.print(message);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double readDouble(String message) {
        System.out.print(message);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}
