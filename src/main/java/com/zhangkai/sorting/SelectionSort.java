package com.zhangkai.sorting;

/**
 * 选择排序 由小到大
 * 平均O(n^2),最好O(n^2),最坏O(n^2);空间复杂度O(1);不稳定;简单
 * @author zeng
 *
 */
public class SelectionSort {

    public static void selectionSort(int[] a) {
        int count = 0;//相遇次数
        int change = 0;//交换次数
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int k = i;//假设一个最小值
            // 找出最小值的位置
            for (int j = i + 1; j < n; j++) {
                count++;
                if (a[j] < a[k]) {//找出最小值
                    change++;
                    k = j;//记录最小值坐标
                }
            }
            // 将最小值与最大值交换位置
            if (k > i) {
                int big = a[i];//取出大值
                a[i] = a[k];
                a[k] = big;
            }
        }
        System.out.println("count="+count);
        System.out.println("change="+change);
    }

    public static void main(String[] args) {
        int[] b = {6, 3, 8, 2, 9, 1};
        selectionSort(b);
        for (int i : b)
            System.out.print(i + " ");
    }
}