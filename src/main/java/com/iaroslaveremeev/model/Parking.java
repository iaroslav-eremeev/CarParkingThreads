package com.iaroslaveremeev.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;

public class Parking {
    private int[] enteringInterval = new int[2];
    private int[] leavingInterval = new int[2];
    private LinkedList<Car> queue = new LinkedList<>();
    private int queueLength;
    private int maxQueueLength;
    private CopyOnWriteArrayList<Car> parkedCars = new CopyOnWriteArrayList<>();
    private volatile int freePlaces;
    private CarRepository carRepository = new CarRepository();

    public Parking() {
    }

    public Parking(int freePlaces, int maxQueueLength, int[] enteringInterval, int[] leavingInterval) {
        this.freePlaces = freePlaces;
        this.maxQueueLength = maxQueueLength;
        this.enteringInterval = enteringInterval;
        this.leavingInterval = leavingInterval;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    public Queue<Car> getQueue() {
        return queue;
    }

    public int getMaxQueueLength() {
        return maxQueueLength;
    }

    public void newCar() throws InterruptedException {
        Car car = new Car(carRepository);
        carRepository.addCar(car);
        if (this.queueLength + car.getSize() > this.maxQueueLength){
            throw new InterruptedException("The maximum length of car queue is reached!");
        }
        else {
            this.queue.offer(car);
            this.queueLength += car.getSize();
            System.out.println(car.getType() + " car with id " + car.getId() + " entered the queue to parking");
            System.out.println("Queue length is " + this.queueLength);
        }
    }

    public void parkCar() throws InterruptedException {
        if (this.queue.peek() != null){
            if (freePlaces >= this.queue.peek().getSize()){
                SynchronousQueue<Car> syncQueue = new SynchronousQueue<>();
                Car car = this.queue.poll();
                assert car != null;
                this.queueLength -= car.getSize();
                syncQueue.put(car);
                parkedCars.add(syncQueue.take());
                setFreePlaces(freePlaces - car.getSize());
                System.out.println(car.getType() + " car with id " + car.getId() + " is parked");
            }
        }
    }

    public void releaseCar(Car car){

    }

    public int getNextEnteringTime(){
        Random random = new Random();
        return random.ints(enteringInterval[0], enteringInterval[1] + 1).findFirst().getAsInt();
    }

    public int getNextLeavingTime(){
        Random random = new Random();
        return random.ints(leavingInterval[0], leavingInterval[1] + 1).findFirst().getAsInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parking parking = (Parking) o;
        return enteringInterval == parking.enteringInterval && leavingInterval == parking.leavingInterval && maxQueueLength == parking.maxQueueLength && freePlaces == parking.freePlaces && Objects.equals(queue, parking.queue) && Objects.equals(parkedCars, parking.parkedCars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enteringInterval, leavingInterval, queue, maxQueueLength, parkedCars, freePlaces);
    }

    @Override
    public String toString() {
        return "Parking{" +
                "enteringInterval=" + enteringInterval +
                ", leavingInterval=" + leavingInterval +
                ", queue=" + queue +
                ", maxQueueLength=" + maxQueueLength +
                ", parkedCars=" + parkedCars +
                ", freePlaces=" + freePlaces +
                '}';
    }
}
