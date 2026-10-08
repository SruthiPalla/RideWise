package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidInputException;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class RiderService {
    private final List<Rider> riders = new ArrayList<>();

    public Rider registerRider(String name, String location) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Rider name cannot be empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidInputException("Rider location cannot be empty.");
        }

        Rider rider = new Rider(IdGenerator.nextRiderId(), name.trim(), location.trim());
        riders.add(rider);
        return rider;
    }

    public Rider getRiderById(int id) {
        for (Rider rider : riders) {
            if (rider.getId() == id) {
                return rider;
            }
        }
        throw new InvalidInputException("Rider not found with ID: " + id);
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riders);
    }
}
