package com.iaroslaveremeev.model;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Parking {
    private CopyOnWriteArrayList<Car> parking = new CopyOnWriteArrayList<>(); // All parked cars array
    private CopyOnWriteArrayList<Car> passengerCarsParked = new CopyOnWriteArrayList<>(); // All passenger cars parked
    private CopyOnWriteArrayList<Car> trucksParked = new CopyOnWriteArrayList<>(); // All trucks parked
    private int parkingLotsNumber; // Parking size
    private int occupiedParkingLots = 0; // Occupied lots

    public Parking() {
    }

    public Parking(int parkingLotsNumber) {
        this.parkingLotsNumber = parkingLotsNumber;
    }

    public CopyOnWriteArrayList<Car> getParking() {
        return parking;
    }

    public void setParking(CopyOnWriteArrayList<Car> parking) {
        this.parking = parking;
    }

    public CopyOnWriteArrayList<Car> getPassengerCarsParked() {
        return passengerCarsParked;
    }

    public void setPassengerCarsParked(CopyOnWriteArrayList<Car> passengerCarsParked) {
        this.passengerCarsParked = passengerCarsParked;
    }

    public CopyOnWriteArrayList<Car> getTrucksParked() {
        return trucksParked;
    }

    public void setTrucksParked(CopyOnWriteArrayList<Car> trucksParked) {
        this.trucksParked = trucksParked;
    }

    public int getParkingLotsNumber() {
        return parkingLotsNumber;
    }

    public void setParkingLotsNumber(int parkingLotsNumber) {
        this.parkingLotsNumber = parkingLotsNumber;
    }

    public int getOccupiedParkingLots() {
        return occupiedParkingLots;
    }

    public void setOccupiedParkingLots(int occupiedParkingLots) {
        this.occupiedParkingLots = occupiedParkingLots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parking parking1 = (Parking) o;
        return parkingLotsNumber == parking1.parkingLotsNumber && occupiedParkingLots == parking1.occupiedParkingLots && Objects.equals(parking, parking1.parking) && Objects.equals(passengerCarsParked, parking1.passengerCarsParked) && Objects.equals(trucksParked, parking1.trucksParked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parking, passengerCarsParked, trucksParked, parkingLotsNumber, occupiedParkingLots);
    }

    @Override
    public String toString() {
        return "Parking{" +
                "parking=" + parking +
                ", passengerCarsParked=" + passengerCarsParked +
                ", trucksParked=" + trucksParked +
                ", parkingLotsNumber=" + parkingLotsNumber +
                ", occupiedParkingLots=" + occupiedParkingLots +
                '}';
    }
}
