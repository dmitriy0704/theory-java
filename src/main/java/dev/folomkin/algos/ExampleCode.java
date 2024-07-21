package dev.folomkin.algos;

import java.util.HashSet;
import java.util.Set;

public class ExampleCode {
    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 6, 7, 8, 4, 5};
        int res = testCode(arr);
        System.out.println(res);
    }

    public static int testCode(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return num;
            }
            set.add(num);
        }
        return 0;
    }
}
