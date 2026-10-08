# RideWise — Object Relationships

## Relationship Summary

| Source | Target | Relationship | Reason |
|---|---|---|---|
| Rider | Ride | Association | A rider requests a ride. |
| Driver | Ride | Association | A driver can be assigned to a ride. |
| Ride | Rider | Association | Ride references the rider who requested it. |
| Ride | Driver | Association | Ride references the assigned driver. |
| Ride | FareReceipt | Composition | The fare receipt is created as part of the ride's business lifecycle. |
| RiderService | Rider | Aggregation | The service manages registered rider objects. |
| DriverService | Driver | Aggregation | The service manages registered driver objects. |
| RideService | Ride | Aggregation | The service manages active ride objects. |
| RideService | RideMatchingStrategy | Composition | The service is configured with a matching strategy. |
| RideService | FareStrategy | Composition | The service is configured with a fare strategy. |

---

## 1. Rider → Ride

**Type:** Association

A rider can request rides. The ride keeps a reference to the rider.

```text
Rider ─────────────── Ride
       requests
```

The rider exists independently of an individual ride.

---

## 2. Driver → Ride

**Type:** Association

A driver can be assigned to a ride.

```text
Driver ─────────────── Ride
          assigned
```

The driver exists independently and can participate in multiple rides over time.

---

## 3. Ride → FareReceipt

**Type:** Composition

The fare receipt is created as part of the ride operation.

```text
Ride ◆──────── FareReceipt
```

The receipt represents the fare generated for that ride and is not managed as an independent application entity.

---

## 4. RideService → Strategies

**Type:** Composition-style configuration / dependency through interfaces

```text
RideService ◆──── RideMatchingStrategy
RideService ◆──── FareStrategy
```

The service holds strategy objects through their interfaces and receives them through constructor injection. This provides the composition-based extension point while preserving dependency inversion.

This is the main extension mechanism in the application.

---

## 5. Service → Domain Objects

Services maintain in-memory collections of domain objects:

```text
RiderService  ─────► Rider
DriverService ─────► Driver
RideService   ─────► Ride
```

The services coordinate application operations while the domain objects hold the relevant state.

---

## Design Rationale

The relationships intentionally avoid inheritance between the major business entities.

For example:

```text
Rider
Driver
Ride
FareReceipt
```

are independent domain concepts rather than parent/child subclasses.

Behavior that is expected to vary—matching and pricing—is modeled using interfaces and composition instead.

This supports the project's goal of demonstrating **composition over inheritance and scalable extension points**.
