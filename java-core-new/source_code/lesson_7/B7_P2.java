package com.khoinguyen.array.homework;

public class B7_P2 {

  public static void main(String[] args) {
    int[] array = new int[] {1, 1, 1};
    int max = Integer.MIN_VALUE;
    int secondMax = Integer.MIN_VALUE;

    for (int i = 0; i < array.length; i++) {
      if (array[i] > max) {
        secondMax = max;
        max = array[i];
      }
    }

    System.out.println("Max: " + max);
    if (secondMax == Integer.MIN_VALUE) {
      System.out.println("Không tồn tại số lớn thứ nhì");
    } else {
      System.out.println("Số lớn nhất = " + max + ", " + "Số lớn thứ nhì = " + secondMax);
    }
  }


}
