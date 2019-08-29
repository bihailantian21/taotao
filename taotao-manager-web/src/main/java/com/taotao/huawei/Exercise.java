package com.taotao.huawei;

import java.util.Scanner;

public class Exercise {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        //AsciiOrder(input);
        //FilterReapet(input);
        LastCharLength(input);

    }

    private static void AsciiOrder(String input) {
        char[] charArray = input.toCharArray();
        int[] asciiArray = new int[127];
        for (int i = 0; i < charArray.length; i++) {
            asciiArray[charArray[i]]++;
        }

        StringBuilder sb = new StringBuilder();
        int zeroCount = 0;
        while (zeroCount != 127) {
            zeroCount = 0;
            for (int j = 0; j < asciiArray.length; j++) {
                if (asciiArray[j] != 0) {
                    char ch = (char)j;
                    sb.append(ch);
                    asciiArray[j]--;
                } else {
                    zeroCount++;
                }
            }
        }
        System.out.println(sb.toString());
    }

    private static void FilterReapet(String input) {
        char[] charArray = input.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < charArray.length; i++) {
            char temp = charArray[i];
            boolean repeat = false;
            for (int j = 0; j < sb.length(); j++) {
                if (temp == sb.charAt(j)) {
                    repeat = true;
                }
            }

            if (!repeat) {
                sb.append(temp);
            }
        }
        System.out.println(sb.toString());
    }


    private static void LastCharLength(String input) {
        char[] charArray = input.toCharArray();
        int count = 0;

        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] != ' ') {
                count ++;
            } else {
                break;
            }
        }
        System.out.println(count);
    }




}
