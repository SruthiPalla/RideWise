# 🚕 RideWise — Console-Based Ride-Sharing System

> **Final Project | Object-Oriented Programming, SOLID Principles & Low-Level Design**

RideWise is a simplified, console-based ride-sharing system inspired by platforms such as Uber and Ola. The project demonstrates how a maintainable object-oriented design can separate business responsibilities while remaining easy to extend.

The implementation focuses on **clean object modeling, high cohesion, low coupling, composition over inheritance, and the Strategy Design Pattern**.

---

## 📌 Project Overview

RideWise supports the core ride-booking lifecycle:

**Register Rider → Register Driver → Request Ride → Match Driver → Calculate Fare → Complete Ride**

The application stores data in memory and provides a simple console menu. Persistence, REST APIs, authentication, payments, maps, and external integrations are intentionally excluded because they are outside the MVP scope.

### Core capabilities

- Register riders
- Register drivers
- View available drivers
- Request a ride
- Match a driver using a configurable strategy
- Calculate fare using a configurable pricing strategy
- Track ride status, including cancellation
- Complete or cancel assigned rides and release drivers
- View all rides and fare receipts
- Validate invalid console input
- Handle the no-driver-available scenario

---

## 🎯 Design Goals

The project was designed around the following principles:

| Principle | How RideWise applies it |
|---|---|
| **SRP** | Rider, driver, ride, and fare responsibilities are separated across focused classes/services. |
| **OCP** | New matching and fare strategies can be introduced without changing `RideService`. |
| **LSP** | Every strategy implementation can be substituted through its interface. |
| **ISP** | `RideMatchingStrategy` and `FareStrategy` are small, focused interfaces. |
| **DIP** | `RideService` depends on strategy interfaces rather than concrete implementations. |
| **DRY** | Driver allocation and fare logic are centralized instead of duplicated in `Main`. |
| **KISS** | The domain model and service layer remain intentionally simple. |
| **YAGNI** | Only assignment-required MVP functionality is implemented. |
| **Law of Demeter** | Collaborators communicate through direct service/domain relationships rather than deep object chains. |

---

## 🏗️ Architecture

RideWise follows a lightweight layered design:

```text
┌──────────────────────────────────────┐
│             Main / UI                │
│        Console Menu & Input          │
└──────────────────┬───────────────────┘
                   │
                   ▼
┌──────────────────────────────────────┐
│             Service Layer            │
│ RiderService │ DriverService │       │
│              RideService              │
└───────────────┬───────────────┬──────┘
                │               │
                ▼               ▼
        ┌──────────────┐  ┌──────────────┐
        │    Model     │  │   Strategy   │
        │ Rider        │  │ Matching     │
        │ Driver       │  │ Fare         │
        │ Ride         │  └──────────────┘
        │ FareReceipt  │
        └──────────────┘
```

## 📐 UML Class Diagram

The following diagram shows the main domain entities, service layer, and Strategy Pattern extension points.

![RideWise UML Class Diagram](docs/images/ClassDiagram.png)

The diagram highlights the key design decision: `RideService` works with the `RideMatchingStrategy` and `FareStrategy` interfaces, allowing concrete strategies to be replaced without changing the ride-management workflow.

### Layer responsibilities

**Presentation**
- `Main.java`
- Displays the menu
- Reads user input
- Delegates operations to services
- Handles user-facing exceptions

**Service**
- `RiderService`
- `DriverService`
- `RideService`
- Contains application/business operations

**Domain Model**
- `Rider`
- `Driver`
- `Ride`
- `FareReceipt`
- `RideStatus`
- `VehicleType`

**Strategy**
- Driver matching strategies
- Fare calculation strategies

**Exception**
- Application-specific exceptions for invalid input and unavailable drivers

**Utility**
- `IdGenerator`

---

## 📁 Project Structure

```text
RideWise/
├── src/
│   └── com/
│       └── airtribe/
│           └── ridewise/
│               ├── Main.java
│               │
│               ├── model/
│               │   ├── Rider.java
│               │   ├── Driver.java
│               │   ├── Ride.java
│               │   ├── FareReceipt.java
│               │   ├── RideStatus.java
│               │   └── VehicleType.java
│               │
│               ├── strategy/
│               │   ├── RideMatchingStrategy.java
│               │   ├── NearestDriverStrategy.java
│               │   ├── LeastActiveDriverStrategy.java
│               │   ├── FareStrategy.java
│               │   ├── DefaultFareStrategy.java
│               │   └── PeakHourFareStrategy.java
│               │
│               ├── service/
│               │   ├── RiderService.java
│               │   ├── DriverService.java
│               │   └── RideService.java
│               │
│               ├── exception/
│               │   ├── NoDriverAvailableException.java
│               │   └── InvalidInputException.java
│               │
│               └── util/
│                   └── IdGenerator.java
│
├── docs/
│   ├── Requirements.md
│   ├── Class_Model.md
│   ├── SOLID_Reflection.md
│   └── Object_Relationships.md
│
├── .gitignore
└── README.md
```

---

## 🧩 Domain Model

### Rider

Represents a customer requesting rides.

**Attributes**
- `id`
- `name`
- `location`

### Driver

Represents a driver available to accept rides.

**Attributes**
- `id`
- `name`
- `currentLocation`
- `available`
- `vehicleType`
- `activeRides`

### Ride

Represents a trip requested by a rider.

**Attributes**
- `id`
- `rider`
- `driver`
- `distance`
- `status`
- `fareReceipt`

### FareReceipt

Represents the fare generated for an assigned ride.

**Attributes**
- `rideId`
- `amount`
- `generatedAt`

### Enumerations

`RideStatus`

```text
REQUESTED
ASSIGNED
COMPLETED
CANCELLED
```

`VehicleType`

```text
BIKE
AUTO
CAR
```

---

## 🔌 Strategy Pattern

The most important extensibility point in RideWise is the Strategy Pattern.

### Driver matching

```java
public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, List<Driver> drivers);
}
```

Current implementations:

- `NearestDriverStrategy`
- `LeastActiveDriverStrategy`

### Fare calculation

```java
public interface FareStrategy {
    double calculateFare(Ride ride);
}
```

Current implementations:

- `DefaultFareStrategy`
- `PeakHourFareStrategy`

### Dependency Injection

`RideService` receives both strategies through its constructor:

```java
RideService rideService = new RideService(
        driverService,
        matchingStrategy,
        fareStrategy
);
```

Therefore, the core ride service does not need to know which concrete strategy is being used.

For example:

```java
RideMatchingStrategy matchingStrategy =
        new NearestDriverStrategy();

FareStrategy fareStrategy =
        new DefaultFareStrategy();
```

The configuration can later be changed to:

```java
RideMatchingStrategy matchingStrategy =
        new LeastActiveDriverStrategy();

FareStrategy fareStrategy =
        new PeakHourFareStrategy();
```

No changes are required inside `RideService`.

---

## 🔄 Ride Lifecycle

```text
REQUESTED
    │
    │ driver successfully matched
    ▼
ASSIGNED
    │
    │ ride completed
    ▼
COMPLETED
```

`CANCELLED` is also part of the domain model and can be supported as the cancellation extension point.

When a ride is assigned:

- Driver becomes unavailable.
- Driver's active ride count increases.
- Fare is calculated.
- A `FareReceipt` is created.

When the ride is completed or cancelled:

- Ride status becomes `COMPLETED` or `CANCELLED`.
- Driver's active ride count decreases.
- Driver becomes available again.

---

## 💰 Fare Calculation

The MVP includes two pricing strategies.

### Default Fare

```text
Base Fare = ₹50
Rate = ₹12/km

Fare = 50 + (distance × 12)
```

### Peak Hour Fare

```text
Base Fare = ₹60
Rate = ₹15/km

Fare = 60 + (distance × 15)
```

These values are intentionally simple for demonstration. The important design decision is that pricing rules are isolated behind `FareStrategy`.

---

## 🚗 Driver Matching

### NearestDriverStrategy

For this console MVP, locations are represented as strings rather than GPS coordinates. The strategy first prefers an available driver at the same location; if none exists, it falls back to the first available driver. Exact GPS-distance calculation is intentionally outside the MVP scope.

This keeps the implementation aligned with the assignment's LLD objective without introducing unnecessary map/geolocation infrastructure.

### LeastActiveDriverStrategy

Selects the available driver with the lowest active-ride count.

This demonstrates how a different business rule can be introduced independently of `RideService`.

---

## 🛡️ Exception Handling

The application handles invalid input at the console boundary and uses domain/application exceptions for business errors.

Examples:

- Empty rider/driver name
- Empty location
- Invalid vehicle type
- Invalid distance
- Invalid rider ID
- Invalid ride ID
- Attempting to complete an invalid ride
- No driver available

Custom exceptions:

```text
InvalidInputException
NoDriverAvailableException
```

---

## 🖥️ Console Menu

```text
===== RideWise =====
1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides
7. Cancel Ride
8. Exit
```

The `Main` class is intentionally kept as a presentation layer. It collects input and delegates actual operations to the service layer.

---

## ▶️ How to Run

### Prerequisites

- Java JDK 8 or above
- IntelliJ IDEA, Eclipse, or any Java-compatible IDE

### IntelliJ IDEA

1. Open the `RideWise` folder.
2. Mark `src` as the source root if required.
3. Open:
   `src/com/airtribe/ridewise/Main.java`
4. Run `Main`.

### Command line

From the project root:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.airtribe.ridewise.Main
```

On Windows, running `Main.java` directly from IntelliJ is recommended for simplicity.

---

## 🧪 Example Usage

A typical flow is:

```text
1. Add Rider
   Name: Rahul
   Location: Hyderabad

2. Add Driver
   Name: Arjun
   Location: Hyderabad
   Vehicle: CAR

3. View Available Drivers

4. Request Ride
   Rider ID: 1
   Distance: 10

   Driver is assigned
   Fare is calculated
   Fare receipt is generated

5. Complete Ride
   Ride ID: 1

6. View Rides
   Status: COMPLETED

7. Cancel Ride
   Ride ID: 2
```

---

## 🧠 Key LLD Decisions

### Composition over inheritance

`RideService` is composed with strategy implementations through interfaces rather than inheriting from strategy classes.

### High cohesion

Each service has a focused responsibility:

```text
RiderService  → Rider operations
DriverService → Driver operations
RideService   → Ride operations
```

### Low coupling

The ride service depends on:

```text
RideMatchingStrategy
FareStrategy
```

instead of:

```text
NearestDriverStrategy
DefaultFareStrategy
```

This makes the system easier to test and extend.

### In-memory storage

Lists are used instead of a database because persistence is outside the MVP requirements.

---

## 🚀 Possible Extensions

The architecture intentionally leaves room for future functionality without requiring changes to the core design.

Potential extensions include:

- `SurgeFareStrategy`
- `VehicleSpecificFareStrategy`
- `ClosestDriverByDistanceStrategy`
- Ride cancellation
- Driver/rider location coordinates
- Persistent storage
- REST API
- Payment processing
- Authentication
- Notification service

These are **not implemented in the MVP** because of the YAGNI principle.

---

## 📚 Documentation

Detailed design documentation is available under `/docs`:

| Document | Purpose |
|---|---|
| `Requirements.md` | Functional and non-functional requirements |
| `Class_Model.md` | Domain and service model |
| `SOLID_Reflection.md` | Explanation of SOLID and design principles |
| `Object_Relationships.md` | Relationships between domain objects |

---

## ✅ Assignment Alignment

| Requirement | Implementation |
|---|---|
| Register Riders | `RiderService` |
| Register Drivers | `DriverService` |
| Show Available Drivers | `DriverService` |
| Request Ride | `RideService` |
| Driver Matching Strategy | `RideMatchingStrategy` |
| Nearest Driver | `NearestDriverStrategy` |
| Least Active Driver | `LeastActiveDriverStrategy` |
| Fare Strategy | `FareStrategy` |
| Default Fare | `DefaultFareStrategy` |
| Peak Fare | `PeakHourFareStrategy` |
| Ride Status | `RideStatus` |
| Fare Receipt | `FareReceipt` |
| Exception Handling | Custom exceptions + console validation |
| Dependency Injection | Constructor injection in `RideService` |
| Low Coupling | Interface-based strategy dependencies |
| Extensibility | Strategy Pattern |
| Console Application | `Main.java` |

---

## 🎓 Conclusion

RideWise demonstrates a practical approach to low-level design by keeping the domain model simple while creating clear extension points for business rules.

The key architectural decision is the use of **small interfaces + constructor injection + Strategy Pattern**, allowing driver matching and fare calculation to evolve independently from the ride-management workflow.

The project deliberately prioritizes **clarity, maintainability, extensibility, and appropriate scope** over unnecessary complexity.
