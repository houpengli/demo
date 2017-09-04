package com.zhangkai.sorting;

/**
 * 升序冒泡
 * Created by zhangkai on 2017/9/4.
 */
public class BubbleSort {
    public static void main(String[] args) {
        int count = 0;//相遇次数
        int change = 0;//交换次数
        int[] arr = {6, 3, 8, 2, 9, 1};
        for (int i = 0; i < arr.length ; i++) {//外层循环控制排序趟数
            for (int j = i+1; j < arr.length ; j++) {//内层循环控制每一趟排序多少次
                count++;
                if (arr[i] > arr[j]) {//从小到大
                    change++;
                    int big = arr[i];
                    arr[i] = arr[j];
                    arr[j] = big;
                }
            }
        }
        System.out.println("count="+count);
        System.out.println("change="+change);
        System.out.println("排序后的数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}
