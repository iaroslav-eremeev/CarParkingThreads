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
    private static Thread threadGrowQueue; // Add new car in the queue to parking thread
    private static Thread threadLeaveParking; // Release one car from the parking thread
    private static Thread threadStatusMessages; // Current number of cars in parking (passenger and trucks) and in the queue

    // Method with thread for adding cars in the queue before parking
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

    // Method with thread for letting cars out of parking
    public void leaveParking(Parking parking, LeavingInterval leavingInterval) {
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
