package com.iaroslaveremeev;

import com.iaroslaveremeev.model.Parking;
import com.iaroslaveremeev.model.Queue;
import com.iaroslaveremeev.service.ParkingService;
import com.iaroslaveremeev.util.EnteringInterval;
import com.iaroslaveremeev.util.LeavingInterval;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // User may choose the parking size, maximum length of the queue before the parking
        // the interval of new cars appearing in the queue and the interval of cars leaving the queue
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of parking lots:");
        Parking parking = new Parking(scanner.nextInt());

        System.out.println("Enter maximum queue length:");
        Queue queue = new Queue(scanner.nextInt());

        System.out.println("Enter the interval of new cars appearing in seconds (2 integer numbers):");
        EnteringInterval enteringInterval = new EnteringInterval(scanner.nextInt(), scanner.nextInt());

        System.out.println("Enter the interval of parked cars leaving in seconds (2 integer numbers):");
        LeavingInterval leavingInterval = new LeavingInterval(scanner.nextInt(), scanner.nextInt());

        // Test
        ParkingService parkingService = new ParkingService(queue, parking, enteringInterval, leavingInterval);
        parkingService.allowJoiningQueue();
        parkingService.allowLeavingFromParking();
        parkingService.printStatusMessages();
    }
}
