package com.airtribe.ridewise.util;

public class IdGenerator {
    private static int riderId = 1;
    private static int driverId = 1;
    private static int rideId = 1;

    private IdGenerator() {
    }

    public static int nextRiderId() {
        return riderId++;
    }

    public static int nextDriverId() {
        return driverId++;
    }

    public static int nextRideId() {
        return rideId++;
    }
}
