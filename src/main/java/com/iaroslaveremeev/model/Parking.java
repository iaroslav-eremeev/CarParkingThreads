package com.iaroslaveremeev.model;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;

public class Parking {
    private int size;
    private volatile int free;
    private volatile int occupied;
    private int enteringInterval;
    private int leavingInterval;
    private SynchronousQueue queue;
    private int maxQueueLength;
    private CopyOnWriteArrayList<Car> parkedCars;
}
