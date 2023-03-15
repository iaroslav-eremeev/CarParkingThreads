package com.iaroslaveremeev.util;

import java.util.InputMismatchException;
import java.util.Random;

public class EnteringInterval {
    private int enteringFrom;
    private int enteringUntil;

    public EnteringInterval(int enteringFrom, int enteringUntil) {
        if (enteringFrom >= enteringUntil) {
            throw new InputMismatchException("First number of the interval must be smaller that the second one!");
        }
        this.enteringFrom = enteringFrom;
        this.enteringUntil = enteringUntil;
    }

    // Method to calculate random interval of new cars appearing in the queue
    public long getNextAddingTime(int[] enteringInterval){
        Random random = new Random();
        return random.ints(enteringFrom, enteringUntil + 1).findFirst().getAsInt() * 1000L;
    }
}
