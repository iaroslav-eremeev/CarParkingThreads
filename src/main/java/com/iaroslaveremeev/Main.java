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

        threadAddQueue = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (queueLength == maxQueueLength) {
                            threadLeaveParking.interrupt();
                            threadAddQueue.interrupt();
                        }
                        Thread.sleep(3000L);
                        Car car = new Car(carId++);
                        queue.add(car);
                        if (car.getType().equals(CarType.PASSENGER)){
                            queueLength++;
                        }
                        else queueLength +=2;
                        System.out.println(car.getType() + " car with id " + car.getId() + " entered the queue");
                        event();
                        System.out.println("Queue length after parking is " + queueLength);
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
                        Thread.sleep(10000L);
                        Random random = new Random();
                        if (parking.size() > 0){
                            Car leavingCar = parking.get(random.nextInt(parking.size() - 1));
                            if (leavingCar != null){
                                parking.remove(leavingCar);
                                System.out.println(leavingCar.getType() + " car with id " + leavingCar.getId() + " left parking");
                            }
                        }
                        event();
                    }
                } catch (InterruptedException e) {}
            }
        });
        threadLeaveParking.start();

    }

    public static synchronized void event() {
        System.out.println("There are " + (parkingSize - parking.size()) + " free places before parking of a new car");
        Car parkingCar = queue.peek();
        if (parkingCar != null) {
            if (parking.size() + parkingCar.getSize() <= parkingSize) {
                parking.add(queue.poll());
                queueLength -= parkingCar.getSize();
                System.out.println("There are " + (parkingSize - parking.size()) + " free places after parking a new car");
            }
            else System.out.println("Parking is full!");
        }
    }

    public static int getNextAddingTime(){
        Random random = new Random();
        return random.ints(enteringInterval[0], enteringInterval[1] + 1).findFirst().getAsInt();
    }

    public static int getNextLeavingTime(){
        Random random = new Random();
        return random.ints(leavingInterval[0], leavingInterval[1] + 1).findFirst().getAsInt();
    }
}