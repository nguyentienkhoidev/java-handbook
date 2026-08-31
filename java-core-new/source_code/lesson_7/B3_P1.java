package com.khoinguyen.array.homework;

import java.util.Arrays;

public class B3_P1 {

  public static void main(String[] args) {
    int [] array = new int[] {1, 1, 1, 1};
    int max = array[0];
    int min = array[0];
    int indexMax = 0;
    int indexMin = 0;

    for (int i = 0; i < array.length; i++) {
      if (array[i] > max) {
        max = array[i];
        indexMax = i;
      }

      if (array[i] < min) {
        min = array[i];
        indexMin = i;
      }
    }
    System.out.printf("Value Max: %s has index %s \n", max, indexMax);
    System.out.printf("Value Min: %s has index %s", min, indexMin);
    if (max == min) {
      System.out.println("ko co min");
    }
    else {

    }
  }
}
