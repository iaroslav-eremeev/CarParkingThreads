package com.iaroslaveremeev;

import com.iaroslaveremeev.model.Car;
import com.iaroslaveremeev.model.CarType;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private static Thread threadAddQueue; // Add new car in the queue to parking thread
    private static Thread threadLeaveParking; // Release one car from the parking thread
    private static Thread threadStatusMessages; // Current number of cars in parking (passenger and trucks) and in the queue
    private static int carId = 1; // starting car id
    private static CopyOnWriteArrayList<Car> parking = new CopyOnWriteArrayList<>(); // All parked cars array
    private static CopyOnWriteArrayList<Car> passengerCarsParked = new CopyOnWriteArrayList<>(); // All passenger cars parked
    private static CopyOnWriteArrayList<Car> trucksParked = new CopyOnWriteArrayList<>(); // All trucks parked
    private static int parkingLotsNumber; // Parking size
    private static int occupiedParkingLots; // Occupied lots
    private static ConcurrentLinkedQueue<Car> queue = new ConcurrentLinkedQueue<>(); // Queue to parking
    private static int queueLength = 0; // Queue length

    public static void main(String[] args) throws InterruptedException {

        // User may choose the parking size, maximum length of the queue before the parking
        // the interval of new cars appearing in the queue and the interval of cars leaving the queue
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of parking lots:");
        parkingLotsNumber = scanner.nextInt();
        System.out.println("Enter maximum queue length:");
        int maxQueueLength = scanner.nextInt();
        System.out.println("Enter the interval of new cars appearing in seconds (2 integer numbers):");
        int enteringFrom = scanner.nextInt();
        int enteringUntil = scanner.nextInt();
        if (enteringFrom >= enteringUntil) {
            throw new InputMismatchException("First number of the interval must be smaller that the second one!");
        }
        int[] enteringInterval = new int[]{enteringFrom, enteringUntil};
        System.out.println("Enter the interval of parked cars leaving in seconds (2 integer numbers):");
        int leavingFrom = scanner.nextInt();
        int leavingUntil = scanner.nextInt();
        if (leavingFrom >= leavingUntil) {
            throw new InputMismatchException("First number of the interval must be smaller that the second one!");
        }
        int[] leavingInterval = new int[]{leavingFrom, leavingUntil};

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

    // Method to calculate random interval of new cars appearing in the queue
    public static long getNextAddingTime(int[] enteringInterval){
        Random random = new Random();
        return random.ints(enteringInterval[0], enteringInterval[1] + 1).findFirst().getAsInt() * 1000L;
    }

    // Method to calculate random interval of cars leaving the parking
    public static long getNextLeavingTime(int[] leavingInterval){
        Random random = new Random();
        return random.ints(leavingInterval[0], leavingInterval[1] + 1).findFirst().getAsInt() * 1000L;
    }
}