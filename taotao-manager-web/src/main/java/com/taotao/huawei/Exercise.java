package com.taotao.huawei;

import java.util.Scanner;

public class Exercise {

    public static void main(String[] args) {
      /* String a = Integer.toBinaryString(21);//10101
        String b = a.substring(0,3);
        System.out.println(b);
        System.out.println(b.equals("101"));

        System.out.println(a.length());
*/

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        BitCount3(input);

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
            } else { break;
            }
        }
        System.out.println(count);
    }

    private static int BitCount(int value) {
        int j = 0;
        int a = 0;
        if (value < 0) {
            a = -1;
        } else if (value == 0) {
            a = -1;
        } else {
            for (int i = 0; i < 13; i++) {
                 if((value  & (5 << i))== (5 << i)) {
                    j=j+1;
                 }
                 if(j==1&&a==0) {
                    a=i+1;
                 }
            }

        }
        return a;
    }

    private static void BitCount2(int value) {
        //次数0  a
        //首次位置 -1   j

        int a=0;
        int j=-1;

        for (int i = 0; i < Integer.toBinaryString(value).length(); i++) {
            if((value & (5)) == (5)) {
                j++;
            }
            if (j==1&&a==0) {
                a=i+1;
            }

        }
        System.out.print(a);
        System.out.println(" ");
        System.out.print(j);

    }


    /*private static void BitCount3(int value) {
        //次数0  a
        //首次位置 -1   j

        int a=0;
        int j=-1;
        String value1 = Integer.toBinaryString(value);
        char[] value2 = value1.toCharArray();
        int length = value2.length;

        for (int i = 0; i < length-2; i++) {
            if(value2[length-(i+1)] == 1 && value2[length-(i+2)] == 0 && value2[length-(i+3)] == 1) {//0-4 3 2    1-3 2 1  2-2 1 0  3-1 0 -1
                a++;
                j=i;
            }
        }
        System.out.print(a);
        System.out.println(" ");
        System.out.print(j);
    }*/

    private static void BitCount3(int value) {
        //次数0  a
        //首次位置 -1   j

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
        System.out.print(j);
    }






}
