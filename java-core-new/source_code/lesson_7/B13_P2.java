package com.khoinguyen.array.homework;

public class B13_P2 {

  public static void main(String[] args) {
    int[] array = {1, 2, 3, 3, 2, 1};

    //tính sum: tổng để tính avg
    int sum = 0;
    for (int i = 0; i < array.length; i++) {
      sum += array[i];
    }

    //gt trung binh
    double avg = sum / array.length;

    String result = "";
    int count = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] > avg) {
        count++;
        result += array[i] + ", ";
      }
    }
    System.out.printf("Co %s so lon hon gia tri trung binh mang la: %s", count, result);
  }
}
