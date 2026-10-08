package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Driver selected = null;

        for (Driver driver : drivers) {
            if (driver.isAvailable() &&
                    (selected == null || driver.getActiveRides() < selected.getActiveRides())) {
                selected = driver;
            }
        }

        return selected;
    }
}
