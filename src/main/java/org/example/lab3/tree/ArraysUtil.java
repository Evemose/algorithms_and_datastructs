package org.example.lab3.tree;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArraysUtil {

    public static int homogeneousBinarySearch(int[] arr, int key) {
        var delta = arr.length/2;
        var res = arr.length/2;
        var comparsions = 0;
        while (delta > 0 && res >= 0 && res < arr.length) {
            comparsions+=2;
            if (arr[res] == key) {
                System.out.println("Comparsions: " + comparsions);
                return res;
            } else if (arr[res] > key) {
                comparsions++;
                res -= delta;
            } else {
                comparsions++;
                res += delta;
            }
            delta = (delta+1)/2;
            if (res == arr.length) {
                res = arr.length - 1;
            }
        }
        comparsions++;
        System.out.println("Comparsions: " + comparsions);
        return -1;
    }
}
