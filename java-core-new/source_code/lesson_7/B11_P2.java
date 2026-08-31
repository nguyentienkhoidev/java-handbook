package com.khoinguyen.array.homework;

public class B11_P2 {
  public static boolean isPalindrome(int[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      if (array[i] != array[array.length - i - 1]) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    int[] array = {1, 2, 3, 3, 2, 1};
    //1 -1
    //2 - 2
    if(isPalindrome(array)) {
      System.out.println("Mảng đối xứng");
    }
    else {
      System.out.println("Mảng KHÔNG đối xứng");
    }
  }
}
