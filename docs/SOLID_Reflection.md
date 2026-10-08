# RideWise — SOLID & Design Principles Reflection

## 1. Single Responsibility Principle — SRP

Each class has one primary reason to change.

| Class | Responsibility |
|---|---|
| `RiderService` | Rider registration and lookup |
| `DriverService` | Driver registration and availability |
| `RideService` | Ride lifecycle operations |
| `DefaultFareStrategy` | Default fare calculation |
| `PeakHourFareStrategy` | Peak-hour fare calculation |
| `NearestDriverStrategy` | Driver matching rule |
| `LeastActiveDriverStrategy` | Alternative driver matching rule |
| `Main` | Console interaction |

Fare and matching logic are intentionally kept outside `RideService`.

---

## 2. Open/Closed Principle — OCP

The system is open for extension but closed for modification in its core workflow.

To add a new fare rule:

```java
public class SurgeFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        // new pricing rule
    }
}
```

`RideService` does not need to be modified.

The same approach works for new matching strategies.

---

## 3. Liskov Substitution Principle — LSP

All concrete implementations can be used wherever their interface is expected.

For example:

```java
RideMatchingStrategy strategy =
        new NearestDriverStrategy();
```

can be replaced with:

```java
RideMatchingStrategy strategy =
        new LeastActiveDriverStrategy();
```

The client code does not need to know the concrete implementation.

---

## 4. Interface Segregation Principle — ISP

The interfaces are intentionally small.

```java
RideMatchingStrategy
```

contains only driver matching behavior.

```java
FareStrategy
```

contains only fare calculation behavior.

There are no large interfaces forcing implementations to provide unrelated methods.

---

## 5. Dependency Inversion Principle — DIP

`RideService` depends on abstractions:

```java
private final RideMatchingStrategy matchingStrategy;
private final FareStrategy fareStrategy;
```

rather than:

```java
private final NearestDriverStrategy strategy;
```

The concrete objects are supplied from outside through the constructor.

This is constructor-based dependency injection.

---

# Additional Design Principles

## DRY — Don't Repeat Yourself

Common operations are centralized in service classes. The console layer does not duplicate driver allocation or fare calculation logic.

## KISS — Keep It Simple

The project uses plain Java classes and collections. It avoids unnecessary frameworks and infrastructure.

## YAGNI — You Aren't Gonna Need It

The assignment does not require:
- Database persistence
- REST APIs
- Payment gateways
- Authentication
- Maps
- Notifications

These are therefore not included in the MVP.

## Law of Demeter

The design avoids unnecessary chains such as:

```java
ride.getDriver().getSomething().getSomethingElse()
```

Services communicate with their direct collaborators, keeping knowledge of object structure limited.

---

# Composition Over Inheritance

`RideService` is composed with:

```text
RideMatchingStrategy
FareStrategy
```

It does not inherit from either strategy.

This allows the behavior of the system to be changed through object composition.

---

# Evaluator Takeaway

The core design decision can be summarized as:

```text
Business workflow
       │
       ▼
   RideService
       │
       ├──────────────► RideMatchingStrategy
       │                       │
       │                       ├── Nearest
       │                       └── Least Active
       │
       └──────────────► FareStrategy
                               │
                               ├── Default
                               └── Peak Hour
```

This separation makes the system easier to extend, reason about, and test.
