package com.iaroslaveremeev;

import com.iaroslaveremeev.model.Car;
import com.iaroslaveremeev.model.CarType;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static Thread threadAddQueue;
    private static Thread threadLeaveParking;
    private static int carId = 1;
    private static CopyOnWriteArrayList<Car> parking = new CopyOnWriteArrayList<>();
    private static int parkingLotsNumber;
    private static int occupiedParkingLots;
    private static ConcurrentLinkedQueue<Car> queue = new ConcurrentLinkedQueue<>();
    private static int queueLength = 0;

    public static void main(String[] args) throws InterruptedException {

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
        threadAddQueue = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Car car = new Car(carId++);
                        if (queueLength + car.getSize() > maxQueueLength) {
                            System.out.println("Car with size " + car.getSize() + " doesn't fit in the queue, which max " +
                                    "length is " + maxQueueLength);
                            threadLeaveParking.interrupt();
                            threadAddQueue.interrupt();
                            throw new InterruptedException();
                        }
                        queue.add(car);
                        if (car.getType().equals(CarType.PASSENGER)){
                            queueLength++;
                        }
                        else queueLength +=2;
                        System.out.println(car.getType() + " car with id " + car.getId() + " entered the queue." +
                                " Queue length is now " + queueLength);
                        event();
                        Thread.sleep(getNextAddingTime(enteringInterval));
                    }
                } catch (InterruptedException e) {
                    System.out.println("Max queue length is reached!");
                }
            }
        });
        threadAddQueue.start();

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
                            occupiedParkingLots -= leavingCar.getSize();
                            System.out.println(leavingCar.getType() + " car with id " + leavingCar.getId() + " left parking." +
                                    " There are now " + (parkingLotsNumber - occupiedParkingLots) + " free lots.");
                        }
                        event();
                    }
                } catch (InterruptedException ignored) {}
            }
        });
        threadLeaveParking.start();

    }

    public static synchronized void event() {
        Car parkingCar = queue.peek();
        if (parkingCar != null) {
            if (occupiedParkingLots + parkingCar.getSize() <= parkingLotsNumber) {
                System.out.println("Parking is possible!");
                parking.add(queue.poll());
                queueLength -= parkingCar.getSize();
                occupiedParkingLots += parkingCar.getSize();
                System.out.println(parkingCar.getType() + " car with id " + parkingCar.getId() + " is parked. " +
                        "There are " + (parkingLotsNumber - occupiedParkingLots) + " free lots now");
                System.out.println("Queue length is " + queueLength + " now");
            }
            else System.out.println("Parking is full!");
        }
    }

    public static long getNextAddingTime(int[] enteringInterval){
        Random random = new Random();
        return random.ints(enteringInterval[0], enteringInterval[1] + 1).findFirst().getAsInt() * 1000L;
    }

    public static long getNextLeavingTime(int[] leavingInterval){
        Random random = new Random();
        return random.ints(leavingInterval[0], leavingInterval[1] + 1).findFirst().getAsInt() * 1000L;
    }
}