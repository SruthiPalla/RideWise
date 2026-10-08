# RideWise — Requirements

## 1. Project Objective

RideWise is a simplified console-based ride-sharing system designed to demonstrate:

- Object-Oriented Programming
- SOLID principles
- Strategy Pattern
- Composition over inheritance
- Low coupling and high cohesion
- Layered separation of concerns
- Extensible business rules

The project is intentionally scoped as an MVP.

---

## 2. Functional Requirements

### FR-01 — Register Rider
The system shall allow a rider to be registered with:
- ID
- Name
- Location

### FR-02 — Register Driver
The system shall allow a driver to be registered with:
- ID
- Name
- Current location
- Vehicle type
- Availability

### FR-03 — View Available Drivers
The system shall display drivers currently marked as available.

### FR-04 — Request Ride
A registered rider shall be able to request a ride by providing the travel distance.

### FR-05 — Match Driver
The requested ride shall be assigned to an available driver using a `RideMatchingStrategy`.

### FR-06 — Calculate Fare
The ride fare shall be calculated using a `FareStrategy`.

### FR-07 — Track Ride Status
A ride shall support the following states:

```text
REQUESTED
ASSIGNED
COMPLETED
CANCELLED
```

### FR-08 — Complete Ride
An assigned ride shall be completable. Once completed:
- Ride status becomes `COMPLETED`
- Driver becomes available
- Driver's active ride count is reduced

### FR-09 — View Rides
The console application shall display registered rides and their current status.

---

## 3. Non-Functional Requirements

### NFR-01 — Extensibility
New fare and driver-matching algorithms should be addable without modifying the core ride workflow.

### NFR-02 — Low Coupling
Services should depend on abstractions rather than concrete strategy implementations.

### NFR-03 — Maintainability
Classes should have focused responsibilities and readable code.

### NFR-04 — Simplicity
The implementation should avoid infrastructure that is not required by the assignment.

### NFR-05 — Testability
Business rules should be separated from console input so that service and strategy classes can be tested independently.

---

## 4. MVP Scope

### Included
- In-memory data
- Console interface
- Rider and driver registration
- Driver availability
- Ride creation
- Driver matching
- Fare calculation
- Ride completion
- Fare receipts
- Input validation

### Intentionally excluded
- Database
- REST APIs
- Authentication
- Payments
- Maps/GPS
- Notifications
- Distributed services
- External integrations

These can be future extensions but are not necessary for the final MVP.
