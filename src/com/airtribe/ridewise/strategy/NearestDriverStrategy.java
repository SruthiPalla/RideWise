package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        // The MVP stores locations as strings rather than GPS coordinates.
        // Prefer an available driver at the same location; otherwise use
        // the first available driver as the nearest candidate.
        for (Driver driver : drivers) {
            if (driver.isAvailable() &&
                    driver.getCurrentLocation().equalsIgnoreCase(rider.getLocation())) {
                return driver;
            }
        }

        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                return driver;
            }
        }
        return null;
    }
}
