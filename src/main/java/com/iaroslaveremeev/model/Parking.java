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

    // Constructor - only parkingLotsNumber entered from console is used
    public Parking(int parkingLotsNumber) {
        this.parkingLotsNumber = parkingLotsNumber;
    }

    public CopyOnWriteArrayList<Car> getParkedCars() {
        return parkedCars;
    }

    public CopyOnWriteArrayList<Car> getPassengerCarsParked() {
        return passengerCarsParked;
    }

    public CopyOnWriteArrayList<Car> getTrucksParked() {
        return trucksParked;
    }

    public int getParkingLotsNumber() {
        return parkingLotsNumber;
    }

    public int getOccupiedParkingLots() {
        return occupiedParkingLots;
    }

    // Add car to the parking
    public void addCar(Car car) {
        if (occupiedParkingLots + car.getSize() <= parkingLotsNumber) {
            parkedCars.add(car);
            if (car.getType().equals(CarType.PASSENGER)) {
                passengerCarsParked.add(car);
            } else trucksParked.add(car);
            occupiedParkingLots += car.getSize();
            System.out.println(car.getType() + " car with id " + car.getId() + " is parked. ");
        }
        else throw new RuntimeException();
    }

    // Randomly release one of the parked cars
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
