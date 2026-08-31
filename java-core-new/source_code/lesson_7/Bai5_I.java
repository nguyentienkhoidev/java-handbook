package com.khoinguyen.array.homework;

public class Bai5_I {

  public static void main(String[] args) {
    int[] array = new int[] {1, 9, 3, 7, 2, 8};// chua sap xep

//    int[] array = new int[] {1, 2, 4, 6}; // da sap xe
    boolean isSorted = true;

    for (int i = 0; i < array.length; i++) {
      if (array[i] > array[i + 1]) {
        isSorted = false;
        break;
      }
    }

    if (isSorted) {
      System.out.println("Mảng ĐÃ được sắp xếp tăng dần.");
    } else {
      System.out.println("Mảng CHƯA được sắp xếp tăng dần.");
    }
  }
}
