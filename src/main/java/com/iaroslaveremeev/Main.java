package com.iaroslaveremeev;

import com.iaroslaveremeev.model.Parking;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter parking size:");
        int size = scanner.nextInt();
        System.out.println("Enter maximum queue length:");
        int maxQueueLength = scanner.nextInt();
        System.out.println("Enter the interval of new cars appearing in seconds (2 integer numbers):");
        int enteringFrom = scanner.nextInt();
        int enteringUntil = scanner.nextInt();
        if (enteringFrom >= enteringUntil){
            throw new InputMismatchException("First number of the interval must be smaller that the second one!");
        }
        int[] enteringInterval = new int[]{enteringFrom, enteringUntil};
        System.out.println("Enter the interval of parked cars leaving in seconds (2 integer numbers):");
        int leavingFrom = scanner.nextInt();
        int leavingUntil = scanner.nextInt();
        if (leavingFrom >= leavingUntil){
            throw new InputMismatchException("First number of the interval must be smaller that the second one!");
        }
        int[] leavingInterval = new int[]{leavingFrom, leavingUntil};

        Parking parking = new Parking(size, maxQueueLength, enteringInterval, leavingInterval);
        Thread carsAppearing = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    parking.newCar();
                    wait(parking.getNextEnteringTime() * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread carsEntering = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        })


    }
}