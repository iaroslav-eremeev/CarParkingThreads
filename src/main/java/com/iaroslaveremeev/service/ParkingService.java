package com.iaroslaveremeev.service;

import com.iaroslaveremeev.model.Car;
import com.iaroslaveremeev.model.CarType;
import com.iaroslaveremeev.model.Queue;
import com.iaroslaveremeev.util.EnteringInterval;

import java.util.Random;

public class ParkingService {
    private static Thread threadGrowQueue; // Add new car in the queue to parking thread
    private static Thread threadLeaveParking; // Release one car from the parking thread
    private static Thread threadStatusMessages; // Current number of cars in parking (passenger and trucks) and in the queue

    public void growQueue(Queue queue, EnteringInterval enteringInterval) {
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
    // Define and run the thread, that deals with new cars appearing in the queue to the parking
    threadAddQueue = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    Car car = new Car(carId++);
                    System.out.println(car.getType() + " car with id " + car.getId() + " entered the queue.");
                    queue.add(car);
                    if (car.getType().equals(CarType.PASSENGER)){
                        queueLength++;
                    }
                    else queueLength +=2;
                    if (queueLength > maxQueueLength) {
                        threadLeaveParking.interrupt();
                        threadAddQueue.interrupt();
                        threadStatusMessages.interrupt();
                        throw new InterruptedException();
                    }
                    event();
                    Thread.sleep(getNextAddingTime(enteringInterval));
                }
            } catch (InterruptedException e) {
                System.out.println("Maximum queue length is reached!");
            }
        }
    });
        threadAddQueue.start();

    // Define and run the thread that deals with cars leaving the parking
    threadLeaveParking = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(getNextLeavingTime(leavingInterval));
                    Random random = new Random();
                    if (parking.size() > 0){
                        Car leavingCar = parking.get(random.nextInt(parking.size()));
                        parking.remove(leavingCar);
                        if (leavingCar.getType().equals(CarType.PASSENGER)){
                            passengerCarsParked.remove(leavingCar);
                        }
                        else trucksParked.remove(leavingCar);
                        occupiedParkingLots -= leavingCar.getSize();
                        System.out.println(leavingCar.getType() + " car with id " + leavingCar.getId() + " left parking.");
                    }
                    event();
                }
            } catch (InterruptedException ignored) {}
        }
    });
        threadLeaveParking.start();

    // Define and run the thread posting status messages each 5 seconds
    threadStatusMessages = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (true){
                    Thread.sleep(5000L);
                    System.out.println("Free parking lots: " + (parkingLotsNumber - occupiedParkingLots));
                    System.out.println("Occupied lots: " + occupiedParkingLots);
                    System.out.println("Passenger cars: " + passengerCarsParked.size());
                    System.out.println("Trucks: " + trucksParked.size());
                    System.out.println("Cars in the queue: " + queueLength);
                }
            } catch (InterruptedException ignored) {}
        }
    });
        threadStatusMessages.start();
}

    // Synchronized method letting cars leave the queue and enter the parking
    public static synchronized void event() {
        Car parkingCar = queue.peek();
        if (parkingCar != null) {
            if (occupiedParkingLots + parkingCar.getSize() <= parkingLotsNumber) {
                parking.add(queue.poll());
                if (parkingCar.getType().equals(CarType.PASSENGER)){
                    passengerCarsParked.add(parkingCar);
                }
                else trucksParked.add(parkingCar);
                queueLength -= parkingCar.getSize();
                occupiedParkingLots += parkingCar.getSize();
                System.out.println(parkingCar.getType() + " car with id " + parkingCar.getId() + " is parked. ");
            }
        }
    }

}
