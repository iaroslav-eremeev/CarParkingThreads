package com.iaroslaveremeev.model;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Parking {
    private CopyOnWriteArrayList<Car> parkedCars = new CopyOnWriteArrayList<>(); // All parked cars array
    private CopyOnWriteArrayList<Car> passengerCarsParked = new CopyOnWriteArrayList<>(); // All passenger cars parked
    private CopyOnWriteArrayList<Car> trucksParked = new CopyOnWriteArrayList<>(); // All trucks parked
    private int parkingLotsNumber; // Parking size
    private int occupiedParkingLots = 0; // Occupied lots

    public Parking() {
    }

    public Parking(int parkingLotsNumber) {
        this.parkingLotsNumber = parkingLotsNumber;
    }

    public CopyOnWriteArrayList<Car> getParkedCars() {
        return parkedCars;
    }

    public void setParkedCars(CopyOnWriteArrayList<Car> parkedCars) {
        this.parkedCars = parkedCars;
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

    public void addCar(Car car) {
        if (occupiedParkingLots + car.getSize() <= parkingLotsNumber) {
            parkedCars.add(car);
            if (car.getType().equals(CarType.PASSENGER)) {
                passengerCarsParked.add(car);
            } else trucksParked.add(car);
            occupiedParkingLots += car.getSize();
        }
    }

    public Car releaseRandomCar(){
        Random random = new Random();
        Car leavingCar = parkedCars.get(random.nextInt(parkedCars.size()));
        parkedCars.remove(leavingCar);
        if (leavingCar.getType().equals(CarType.PASSENGER)){
            passengerCarsParked.remove(leavingCar);
        }
        else trucksParked.remove(leavingCar);
        occupiedParkingLots -= leavingCar.getSize();
        return leavingCar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parking parking1 = (Parking) o;
        return parkingLotsNumber == parking1.parkingLotsNumber && occupiedParkingLots == parking1.occupiedParkingLots && Objects.equals(parkedCars, parking1.parkedCars) && Objects.equals(passengerCarsParked, parking1.passengerCarsParked) && Objects.equals(trucksParked, parking1.trucksParked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkedCars, passengerCarsParked, trucksParked, parkingLotsNumber, occupiedParkingLots);
    }

    @Override
    public String toString() {
        return "Parking{" +
                "parkedCars=" + parkedCars +
                ", passengerCarsParked=" + passengerCarsParked +
                ", trucksParked=" + trucksParked +
                ", parkingLotsNumber=" + parkingLotsNumber +
                ", occupiedParkingLots=" + occupiedParkingLots +
                '}';
    }
}
