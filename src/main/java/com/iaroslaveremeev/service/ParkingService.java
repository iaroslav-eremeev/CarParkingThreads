package com.iaroslaveremeev.service;

import com.iaroslaveremeev.model.Car;
import com.iaroslaveremeev.model.CarType;
import com.iaroslaveremeev.model.Parking;
import com.iaroslaveremeev.model.Queue;
import com.iaroslaveremeev.util.EnteringInterval;
import com.iaroslaveremeev.util.LeavingInterval;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParkingService {
    private Thread threadGrowQueue;
    private Thread threadLeaveParking;
    private Thread threadStatusMessages;
    private final Queue queue;
    private final Parking parking;
    private final EnteringInterval enteringInterval;
    private final LeavingInterval leavingInterval;

    public ParkingService(Queue queue, Parking parking, EnteringInterval enteringInterval, LeavingInterval leavingInterval) {
        this.queue = queue;
        this.parking = parking;
        this.enteringInterval = enteringInterval;
        this.leavingInterval = leavingInterval;
    }

    // Method with thread for adding cars in the queue before parking
    public void growQueue() {
        threadGrowQueue = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Car car = new Car(queue.nextCarId());
                        System.out.println(car.getType() + " car with id " + car.getId() + " entered the queue.");
                        queue.addCar(car);
                        if (queue.getQueueLength() > queue.getMaxQueueLength()) {
                            threadLeaveParking.interrupt();
                            threadGrowQueue.interrupt();
                            threadStatusMessages.interrupt();
                            throw new InterruptedException();
                        }
                        event();
                        Thread.sleep(enteringInterval.nextEntry());
                    }
                } catch (InterruptedException e) {
                    System.out.println("Maximum queue length is reached!");
                }
            }
        });
        threadGrowQueue.start();
    }

    // Method with thread for letting cars out of parking
    public void leaveParking() {
        threadLeaveParking = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(leavingInterval.nextLeave());
                        if (parking.getParkedCars().size() > 0){
                            Car leavingCar = parking.releaseRandomCar();
                            System.out.println(leavingCar.getType() + " car with id " + leavingCar.getId() + " left parking.");
                        }
                        event();
                    }
                } catch (InterruptedException ignored) {}
            }
        });
        threadLeaveParking.start();
    }

    // Method with thread posting status messages each 5 seconds
    public void printStatusMessages() {
        threadStatusMessages = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(5000L);
                        System.out.println("Free parking lots: " +
                                (parking.getParkingLotsNumber() - parking.getOccupiedParkingLots()));
                        System.out.println("Occupied lots: " + parking.getOccupiedParkingLots());
                        System.out.println("Passenger cars: " + parking.getPassengerCarsParked().size());
                        System.out.println("Trucks: " + parking.getTrucksParked().size());
                        System.out.println("Cars in the queue: " + queue.getQueue());
                    }
                } catch (InterruptedException ignored) {}
            }
        });
        threadStatusMessages.start();
    }

    // Synchronized method letting cars leave the queue and enter the parking
    public synchronized void event() {
        Car car = queue.getQueue().peek();
        if (car != null) {
            parking.addCar(car);
            queue.releaseCar(car);
            System.out.println(car.getType() + " car with id " + car.getId() + " is parked. ");
            }
        }

}
