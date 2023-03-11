package com.iaroslaveremeev.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;

public class Parking {
    private int[] enteringInterval = new int[2];
    private int[] leavingInterval = new int[2];
    private Queue<Car> queue = new Queue<Car>() {
        @Override
        public boolean add(Car car) {
            return false;
        }

        @Override
        public boolean offer(Car car) {
            return false;
        }

        @Override
        public Car remove() {
            return null;
        }

        @Override
        public Car poll() {
            return null;
        }

        @Override
        public Car element() {
            return null;
        }

        @Override
        public Car peek() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Car> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Car> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    };
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
        if (this.queue.size() <= this.maxQueueLength){
            this.queue.add(car);
            System.out.println(car.getType() + " car with id " + car.getId() + " entered the queue to parking");
        }
        else throw new InterruptedException("The maximum length of car queue is reached!");
    }

    public void parkCar() throws InterruptedException {
        if (this.queue.peek() != null){
            if (freePlaces >= this.queue.peek().getSize()){
                SynchronousQueue<Car> syncQueue = new SynchronousQueue<>();
                Car car = this.queue.poll();
                assert car != null;
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
