package com.iaroslaveremeev.model;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue {
    private int carId = 0; // starting car id
    private ConcurrentLinkedQueue<Car> queue = new ConcurrentLinkedQueue<>(); // Queue to parking
    private int queueLength = 0; // Queue length
    private int maxQueueLength;

    // Constructor - only maxQueueLength read from console is used
    public Queue(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
    }

    public ConcurrentLinkedQueue<Car> getQueue() {
        return queue;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public int getMaxQueueLength() {
        return maxQueueLength;
    }

    // Generate new car id
    public int nextCarId() {
        this.carId++;
        return this.carId;
    }

    // Method to add new car in the queue
    public void addCar(Car car) {
        this.queue.add(car);
        if (car.getType().equals(CarType.PASSENGER)){
            this.queueLength++;
        }
        else this.queueLength +=2;
    }

    // Release car from the queue before entering parking
    public void releaseCar(Car car){
        queue.poll();
        queueLength -= car.getSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queue queue1 = (Queue) o;
        return carId == queue1.carId && queueLength == queue1.queueLength && Objects.equals(queue, queue1.queue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId, queue, queueLength);
    }

    @Override
    public String toString() {
        return "Queue{" +
                "carId=" + carId +
                ", queue=" + queue +
                ", queueLength=" + queueLength +
                '}';
    }
}
