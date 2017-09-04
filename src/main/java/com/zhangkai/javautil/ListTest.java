package com.zhangkai.javautil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by zhangkai on 2017/9/4.
 */
public class ListTest {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("");arrayList.add("");
        String str = arrayList.set(1, "1");
        //set返回以前的值
        System.out.println(arrayList.set(1,"2"));

        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("");
        linkedList.set(0,"1");
    }
}
