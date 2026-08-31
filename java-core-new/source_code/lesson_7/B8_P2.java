package com.khoinguyen.array.homework;

import java.util.Arrays;

public class B8_P2 {
  public static boolean isPrime(int n) {
    if(n < 2) {
      return false;
    }

    if(n == 2) {
      return true;
    }

    for(int i = 2; i <= Math.sqrt(n); i++) {
      if(n % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {

    int countPrime = 0;
    int[] arr = {1, 4, 7, 9, 2, 5, 33, 44, 66, 77, 55};

    String primeStr = "";
    for(int i = 0; i < arr.length; i++) {
      if(isPrime(arr[i])) {
        countPrime++;
        primeStr += arr[i] + ", ";
      }
    }

    System.out.printf("Có %s số nguyên tố trong mảng: %s\n", countPrime, primeStr);
  }
}
