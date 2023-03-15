package com.iaroslaveremeev.service;

public class ParkingService {
    private static Thread threadAddQueue; // Add new car in the queue to parking thread
    private static Thread threadLeaveParking; // Release one car from the parking thread
    private static Thread threadStatusMessages; // Current number of cars in parking (passenger and trucks) and in the queue


}
