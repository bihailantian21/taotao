package com.taotao.huawei;

import java.util.Scanner;

public class ex {

    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            BitCount(input);
        }
    }

    private static void BitCount(int value) {
        int a=0;
        int j=-1;
        String value1 = Integer.toBinaryString(value);
        int length = value1.length();

        for (int i = 0; i < length - 2; i++) {
            if(value1.substring(i,i+3).equals("101")) {
                a=a+1;
            }
            if (a == 1 && j== -1) {
                j=i;
            }
        }
        System.out.print(a);
        System.out.print(" ");
        System.out.print(length-5-j);
    }






}
