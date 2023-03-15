package com.iaroslaveremeev.util;

import java.util.InputMismatchException;
import java.util.Random;

public class LeavingInterval {
    private int leavingFrom;
    private int leavingUntil;

    public LeavingInterval(int leavingFrom, int leavingUntil) {
        if (leavingFrom >= leavingUntil) {
            throw new InputMismatchException("First number of the interval must be smaller that the second one!");
        }
        this.leavingFrom = leavingFrom;
        this.leavingUntil = leavingUntil;
    }

    // Method to calculate random interval of cars leaving the parking
    public long getNextLeavingTime(int[] leavingInterval){
        Random random = new Random();
        return random.ints(leavingFrom, leavingUntil + 1).findFirst().getAsInt() * 1000L;
    }
}
