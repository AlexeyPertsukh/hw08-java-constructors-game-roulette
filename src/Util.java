/*
Как напечатать цвет в консоли с помощью System.out.println?
http://surl.li/ksgx

Build your own Command Line with ANSI escape codes
http://surl.li/kshu

Таблица юникод-символов
https://unicode-table.com/ru/sets/currency-symbols/
 */


import java.util.Arrays;

public class Util {

    private Util(){

    }

    //проверки
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //проверка вхождения строки в массив строк
    public static boolean isStrInArr(String[] arr, String str){
        for (String tmp : arr) {
            if (tmp.compareToIgnoreCase(str) == 0)
            {
                return true;
            }
        }
        return false;
    }

    //вытягивает команду из строки типа "cmd<команда>"
    //ключ - 3 символа
    public static String getStrCmd(String str) {
        if(str.length() > 3){
            String key = str.substring(0, 3);

            if(key.compareToIgnoreCase("cmd") == 0){
                return str.substring(3, str.length());
            }
        }

        return null;
    }

    //пауза
    public static void sleep(int n){
        try {
            Thread.sleep(n);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void printArr(String[] arr) {
        String str;
        str = Arrays.toString(arr).replace('[',' ').replace(']',' ').trim();
        System.out.print(str);
    }

    public static void printArr(double[] arr) {
        String str;
        str = Arrays.toString(arr).replace('[',' ').replace(']',' ').trim();
        System.out.print(str);
    }


    public static void printlnArr(String[] arr) {
        printArr(arr);
        System.out.println();
    }

    public static void printlnArr(double[] arr) {
        printArr(arr);
        System.out.println();
    }

    public static String getStrNearSeparator(String str, int num, char charSeparator) {
        String str1 = "";
        String str2 = "";

        if(str.length() < 1) {
            return "";
        }

        if(num != 1 && num != 2) {
            return "";
        }

        int indexSeparator =-1;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == charSeparator) {
                indexSeparator = i;
                break;
            }
        }

    if (indexSeparator < 1) {
        return "";
    }
            str1 = str.substring(0, indexSeparator);
            str2 = str.substring(indexSeparator + 1, str.length() );

        return (num == 1) ? str1 : str2;
    }

    public static String getFirstStrNearSeparator(String str, char charSeparator) {
        return getStrNearSeparator(str, 1, charSeparator);
    }

    public static String getLastStrNearSeparator(String str, char charSeparator) {
        return getStrNearSeparator(str, 2, charSeparator);
    }

}
