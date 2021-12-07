package com.example.banquebatch.util;

public class Randomizer {
    public static Double generate(Double min,Double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }
}
