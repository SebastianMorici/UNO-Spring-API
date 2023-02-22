package com.sebastian.unobackend;

import java.util.List;

public class JavaTest2 {
    public static void main(String[] args) {
        JavaTest javaTest = new JavaTest();

        System.out.println(javaTest.getPlayedCards());

        javaTest.getPlayedCards().add("dos-amarillo");

        System.out.println(javaTest.getPlayedCards());



    }
}
