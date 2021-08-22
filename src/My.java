/*
Как напечатать цвет в консоли с помощью System.out.println?
http://surl.li/ksgx

Build your own Command Line with ANSI escape codes
http://surl.li/kshu

Таблица юникод-символов
https://unicode-table.com/ru/sets/currency-symbols/
 */


import java.util.Arrays;

public class My {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String[] COLORS_FONT ={
                                    ANSI_RESET,
                                    ANSI_BLACK,
                                    ANSI_RED,
                                    ANSI_GREEN,
                                    ANSI_YELLOW,
                                    ANSI_BLUE,
                                    ANSI_PURPLE,
                                    ANSI_CYAN,
                                    ANSI_WHITE
                                    };
    //
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static final String[] COLORS_BACKGROUND ={

            ANSI_BLACK_BACKGROUND,
            ANSI_RED_BACKGROUND,
            ANSI_GREEN_BACKGROUND,
            ANSI_YELLOW_BACKGROUND,
            ANSI_BLUE_BACKGROUND,
            ANSI_PURPLE_BACKGROUND,
            ANSI_CYAN_BACKGROUND,
            ANSI_WHITE_BACKGROUND
    };

    private My(){

    }

    //
    public static void printColor(String strPrint, String color){
        System.out.print(color + strPrint + ANSI_RESET);
    }

    public static void printlnColor(String strPrint, String color){
        System.out.println(color + strPrint + ANSI_RESET);
    }


    //
    public static void printColor(String strPrint, String colorFont, String colorBackground){
        System.out.print(colorFont + colorBackground + strPrint + ANSI_RESET);
    }

    public static void printColorBlue(String strPrint) {
        printColor(strPrint, ANSI_BLUE);
    }

    public static void printlnColorBlue(String strPrint) {
        printlnColor(strPrint, ANSI_BLUE);
    }

    public static void printColorRed(String strPrint) {
        printColor(strPrint, ANSI_RED);
    }

    public static void printlnColorRed(String strPrint) {
        printlnColor(strPrint, ANSI_RED);
    }

    public static void printColorYellow(String strPrint) {
        printColor(strPrint, ANSI_YELLOW);
    }

    public static void printlnColorYellow(String strPrint) {
        printlnColor(strPrint, ANSI_YELLOW);
    }



    public static void setTextColor(String color){
        System.out.print(color);
    }

    public static void setTextColor(String colorFont, String colorBackgound){
        System.out.print(colorFont + colorBackgound);
    }


    public static void resetTextColor(){
        System.out.print(ANSI_RESET);
    }

    //печатает каждую букву случайным цветом
    public static void printlnRandomColor(String strPrint){
        String str = ANSI_RESET;

        String randomColor;
        char ch;

        for (int i=0; i < strPrint.length(); i++)
        {
            randomColor = COLORS_FONT[(int) (Math.random() * COLORS_FONT.length)];
            ch = strPrint.charAt(i);
            System.out.print(String.format("%s%c", randomColor, ch));
        }

        System.out.println(ANSI_RESET);
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
