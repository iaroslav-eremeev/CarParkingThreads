package com.iaroslaveremeev.model;

import java.util.Objects;
import java.util.Random;

public class Car {

    private int id;
    private CarType type; // ENUM - either PASSENGER CAR or TRUCK
    private int size; // number of places that this car occupies on parking

    public Car() {
    }

    public Car(int id) {
        this.id = id;
        Random random = new Random();
        int typeGenerator = random.nextInt(10);
        // Trucks are rarer than passenger cars. Let trucks represent 30% of cars that appear
        if (typeGenerator > 6) this.type = CarType.TRUCK;
        else this.type = CarType.PASSENGER;
        // Trucks' size (number of lots it takes on parking) is 2, passenger cars' - 1
        if (this.type.equals(CarType.TRUCK)) this.size = 2;
        else this.size = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && size == car.size && type == car.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, size);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
