package com.iaroslaveremeev.model;

import java.util.ArrayList;
import java.util.Objects;

public class CarRepository {

    // Создавать новые машины - через репозиторий! (для генерации уникальных айди)

    private ArrayList<Car> cars = new ArrayList<>();

    public CarRepository() {
    }

    public CarRepository(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarRepository that = (CarRepository) o;
        return Objects.equals(cars, that.cars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cars);
    }

    @Override
    public String toString() {
        return "CarRepository{" +
                "cars=" + cars +
                '}';
    }
}
