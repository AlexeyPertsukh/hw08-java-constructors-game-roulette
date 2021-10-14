/*
http://surl.li/krrz
Рулетка - азартная игра, представляющая собой вращающееся колесо с 36 секторами красного и чёрного цветов и 37-м зелёным сектором «зеро» с обозначением нуля
в американском варианте два зелёных — 0 и 00, итого 38 секторов).
Игроки, играющие в рулетку, могут сделать ставку на выпадение шарика на цвет (красное или чёрное),
чётное или нечётное число, диапазон (1—18 или 19—36) или конкретное число.

С 2016 появилась еще одна разновидность американской рулетки - с тремя нулями: 0, 00 и 000

Разметка игрового поля:
http://gambler-sapiens.ru/gambling-reviews/igrovoe-pole-ruletki#page-content

Ставки и выигрышные коэфициенты:
http://hevgen-sab.narod.ru/Pravila-stavok-na-ruletke.html
 */

//  Реализованы ставки (коеф):
//  1. Угадать одно число                     (1:35)
//  2. Угадать чет/нечет                      (1:1)
//  3. Угадать красное/черное                 (1:1)
//  4. Угадать сектор 1-18 или 19-36          (1:1)
//  5. Угадать ряд(столбец) 1-3, 4-6 и т.д.                 (1:11)
//  6. Угадать колону(линию 12 чисел) 1-34, 2-35б 3-36      (1:2)
//  7. Угадать дюжину(квадрат 12 чисел) 1-12, 13-24, 25-36  (1:2)
//  8. Угадать сплит(2 соседних): 2,5  5,8 и т.д.           (1:17)
//  9. Угадать угол (квадрат 4 числа)                       (1:8)
//  10. Угадать линию (6 чисел подряд, 2 соседних столбца)  (1:5)
//  Примечание:
//  Зеро (сектора 0, 00, 000) не являются ни четными, ни ничетными, ни красными, ни черными

// !!!!!! Переделать: все разборки строк типа 1-8 и 1,2 через субстринг (как в isLine)

public class Roulette {
    //выигрышные коефициенты
    private static final int COEF_NUM = 35;
    private static final int COEF_EVEN_ODD = 1;
    private static final int COEF_RED_BLACK = 1;
    private static final int COEF_LOW_HEIGH = 1;
    private static final int COEF_VERTICAL = 11;
    private static final int COEF_COLUMN = 2;
    private static final int COEF_DOZEN = 2;
    private static final int COEF_SPLIT = 17;
    private static final int COEF_ANGLE = 8;
    private static final int COEF_LINE = 5;

    private static final double[] BETS_CORRECT = {1, 5, 10, 25, 50, 100};  //допустимые ставки
    private static final String[] EVEN_STR = {"чет", "чёт", "четн", "чётн", "четное", "чётное", "четный", "чётный", "even"};
    private static final String[] ODD_STR = {"неч", "нечет", "нечёт", "нечетн", "нечётн", "нечетное", "нечётное", "нечетный", "нечётный", "odd"};
    private static final String[] BLACK_STR = {"черн", "чёрн", "черное", "чёрное", "черный", "чёрный", "black"};
    private static final String[] RED_STR = {"крас", "красн", "красное", "красный", "red"};
    private static final String[] SECT_1_18 = {"1-18", "низ", "низкий", "низкая", "низкое", "low"};
    private static final String[] SECT_19_36 = {"19-36", "верх", "выс", "высокий", "высокая", "высокое", "hi"};
    private static final String[] VERTICAL_STR = {"1-3", "4-6", "7-9", "10-12", "13-15", "16-18",
                                                "19-21", "22-24", "25-27", "28-30", "31-33", "34-36"};
    public static final String[] DOZEN_STR = {"1-12", "13-24", "25-36"};

    //символы-разделители в строках ставок, напр. "2#35" - ставка на горизонтальную линию на сектора от 2 до 35
    //при изменении разделителей, в Bot.java поменять команды для ботов
    private static final char SEPARATOR =        '-';
    private static final char SEPARATOR_COLUMN = ':';
    private static final char SEPARATOR_ANGLE =  '#';
    private static final char SEPARATOR_SPLIT =  ',';


    //в рулетке номера секторов расположены в хитром порядке
    private static final String[] SECTORS_EURO = {"0", "32", "15", "19", "4", "21", "2",
            "25", "17", "34", "6", "27", "13", "36",
            "11", "30", "8", "23", "10", "5", "24",
            "16", "33", "1", "20", "14", "31", "9",
            "22", "18", "29", "7", "28", "12", "35",
            "3", "26"};

    private static final String[] SECTORS_USA_00 = {"0", "28", "9", "26", "30", "11", "7",
            "20", "32", "17", "5", "22", "34", "15",
            "3", "24", "36", "13", "1", "00", "27",
            "10", "25", "29", "12", "8", "19", "31",
            "18", "6", "21", "33", "16", "4", "23",
            "35", "14", "2"};

    private static final String[] SECTORS_USA_000 = {"0", "000", "00", "32", "15", "19", "4",
            "21", "2", "25", "17", "34", "6", "27",
            "13", "36", "11", "30", "8", "23", "10",
            "5", "24", "16", "33", "1", "20", "14",
            "31", "9", "22", "18", "29", "7", "28",
            "12", "35", "3", "26"};


    private static final String[][] SECTORS = {SECTORS_EURO, SECTORS_USA_00, SECTORS_USA_000};

    private static final String[] TABLE_EURO = {
            "------",
            "|     ",
            "|     ",
            "|  0  ",
            "|     ",
            "|     ",
            "------",
    };

    private static final String[] TABLE_USA_00 = {
            "------",
            "|     ",
            "|  00 ",
            "|-----",
            "|  0  ",
            "|     ",
            "------",
    };

    private static final String[] TABLE_USA_000 = {
            "------",
            "| 000 ",
            "|-----",
            "| 00  ",
            "|-----",
            "| 0   ",
            "------",
    };

    private static final String[][] TABLES = {TABLE_EURO, TABLE_USA_00, TABLE_USA_000};


    private String sectorWin;
    private int type;          //тип рулетки
    private int maxSector;
    private String sectorCheat;

    public Roulette() {
        this(0);
    }

    public Roulette(int type) {

        if (type >= SECTORS.length) {
            type = 0;
        }

        this.type = type;

        maxSector = Integer.MIN_VALUE;
        int val;

        for (String str : SECTORS[type]) {
            val = Integer.parseInt(str);
            if (val > maxSector) {
                maxSector = val;
            }
        }
    }


    public String getSectorWin() {
        return sectorWin;
    }

    //анимация
    private void goAnimation() {
        int numRepeat = (int) (Math.random() * 50) + 60;
        int numSect = (int) (Math.random() * SECTORS[type].length);
//        int step = (int) (Math.random() * 3) + 2;
        String str;

        System.out.println("Ставки сделаны, ставок больше нет!");
//        System.out.println("Шарик катится по секторам: ");
        for (int i = 0; i < numRepeat; i++) {

            Util.sleep(50);

            sectorWin = SECTORS[type][numSect];

            //если установлен чит на выигрыш
            if ((i == numRepeat - 1) && (sectorCheat != null)) {
                sectorWin = sectorCheat;
                sectorCheat = null;
            }

            str = String.format("[ %2s ]", sectorWin);
            if (isRed(Integer.parseInt(sectorWin))) {
                Color.printColor(str, Color.ANSI_BLACK, Color.ANSI_RED_BACKGROUND);
            }
            else if(Integer.parseInt(sectorWin) == 0) {
                Color.printColor(str, Color.ANSI_BLACK, Color.ANSI_BLUE_BACKGROUND);
            }
            else
            {
                System.out.print(str);
            }
            System.out.print(" "); //зазор между ячейками рулетки

            if ((i + 1) % 15 == 0) {
                System.out.println();
            }

            numSect++;
            if (numSect >= SECTORS[type].length) {
                numSect = 0;
            }
        }
        System.out.println();
    }

    public void go() {
        Util.sleep(1000);
        goAnimation();
        Util.sleep(1000);

        int val = (Integer.parseInt(sectorWin));

        //чет/нечет
        String strEvOdd = "";
        if (isEven(val)) {
            strEvOdd = "ЧЕтНОЕ";
        } else if (isOdd(val)) {
            strEvOdd = "НЕЧЕТНОЕ";
        }

        //цвет
        String strColor = "";
        boolean resRed = false;
        boolean resBlack = false;
        if (isRed(val)) {
            strColor = "КРАСНОЕ";
            resRed = true;
        } else if (isBlack(val)) {
            strColor = "ЧЕРНОЕ";
            resBlack = true;
        }

        boolean resZero = !(resRed || resBlack);

        //вывод результатов на экран
        System.out.println();
        System.out.print("ВЫИГРАЛ СЕКТОР " + sectorWin);

        if (!resZero) {
            System.out.print(", " + strEvOdd + ", ");
        }

        if (resBlack) {
            System.out.printf("%s", strColor);
        } else if (resRed) {
            Color.printColor(String.format("%s", strColor), Color.ANSI_BLACK, Color.ANSI_RED_BACKGROUND);
        }

        System.out.println();
    }

    //возращает количество поддерживаемых разновидностей рулетки
    public static int getNumTypes() {
        return SECTORS.length;
    }

    //возращает название разновидности рулетки по порядковому номеру
    public static String getNameTypeNum(int num) {
        String nameType = "некорректный номер";

        switch (num) {
            case 0:
                nameType = "Европейская рулетка";
                break;
            case 1:
                nameType = "Американская рулетка с двумя нулями";
                break;
            case 2:
                nameType = "Американская рулетка с тремя нулями";
                break;
            default:
                break;
        }

        return nameType;
    }


    //справка по вводу сектора
    public void helpInputSector() {
        Color.setTextColor(Color.ANSI_BLUE);
        System.out.println("+++++");
        System.out.println("Команды для ввода сектора:");
        System.out.printf("• Ставки на ЧИСЛО (%d:1): введите число от 0 (0, 00 или 000 в американской рулетке) до %d \n", COEF_NUM, maxSector);

        System.out.printf("• Ставки на ЧЕТНОЕ (%d:1), введите одну из команд: ", COEF_EVEN_ODD);
        Util.printlnArr(EVEN_STR);

        System.out.printf("  Ставки на НЕЧЕТНОЕ (%d:1), введите одну из команд: ", COEF_EVEN_ODD);
        Util.printlnArr(ODD_STR);

        System.out.printf("• Ставки на ЧЕРНОЕ (%d:1), введите одну из команд: ", COEF_RED_BLACK);
        Util.printlnArr(BLACK_STR);

        System.out.printf("  Ставки на КРАСНОЕ (%d:1), введите одну из команд: ", COEF_RED_BLACK);
        Util.printlnArr(RED_STR);

        System.out.printf("• Ставки на СЕКТОР  1-18 (%d:1), введите одну из команд: ", COEF_LOW_HEIGH);
        Util.printlnArr(SECT_1_18);

        System.out.printf("  Ставки на СЕКТОР 19-36 (%d:1), введите одну из команд: ", COEF_LOW_HEIGH);
        Util.printlnArr(SECT_19_36);

        System.out.printf("• Ставки на РЯД (столбец) (%d:1), введите одну из команд: ", COEF_VERTICAL);
        Util.printlnArr(VERTICAL_STR);

        System.out.printf("• Ставки на КОЛОНУ (горизонтальная линия 12 чисел) (%d:1), введите одну из команд: ", COEF_COLUMN);
        System.out.printf("1%c34, 2%c35, 3%c36  \n", SEPARATOR_COLUMN, SEPARATOR_COLUMN, SEPARATOR_COLUMN);

        System.out.printf("• Ставки на ДЮЖИНУ (квадрат 12 чисел) (%d:1), введите одну из команд: ", COEF_DOZEN);
        Util.printlnArr(DOZEN_STR);

        System.out.printf("• Ставки на СПЛИТ (2 соседних числа по горизонтали) (%d:1), ввеедите номера соседних секторов, напр.: 8%c11 или 24%c27 \n", COEF_SPLIT, SEPARATOR_SPLIT, SEPARATOR_SPLIT);

        System.out.printf("• Ставки на УГОЛ (квадрат 4 числа) (%d:1), введите 2 сектора квадрата по диагонали, напр.: 8%c12 или 21%c23 \n", COEF_ANGLE, SEPARATOR_ANGLE, SEPARATOR_ANGLE);

        System.out.printf("• Ставки на ЛИНИЮ (2 соседних столбца, 6 чисел подряд) (%d:1), введите первое и последнее число линии,\n", COEF_LINE);
        System.out.println("  то есть начало первого столбца и конец второго. напр.: 7-12 или 22-27");

        Color.resetTextColor();
    }

    //справка по вводу ставки
    public void helpInputBet() {
        Color.setTextColor(Color.ANSI_BLUE);
        System.out.println("+++++");
        System.out.println("Допустимые ставки:");
        printCorrectBets();
        Color.resetTextColor();
    }


    //проверка ставки на корректность
    public static boolean isCorrectBet(double bet) {

        for (int i = 0; i < BETS_CORRECT.length; i++) {
            if (bet == BETS_CORRECT[i]) {
                return true;
            }
        }

        return false;
    }

    //проверка на коректность сектора
    public boolean isCorrectSector(String sector) {
        String str;
        sector = sector.toLowerCase();
        sector = sector.replaceAll("\\s", "");

        //числа
        if (Util.isStrInArr(SECTORS[type], sector)) {
            return true;
        }

        //четный или нечетный
        if (isEven(sector) || isOdd(sector)) {
            return true;
        }

        //черное или красное
        if (isBlack(sector) || isRed(sector)) {
            return true;
        }

        //сектора 1-18 и 19-36
        if (isSector1to18(sector) || isSector19to36(sector)) {
            return true;
        }

        //ряд (столбец)
        if (isVertical(sector)) {
            Color.printlnColorBlue("РЯД (вертикаль)");
//            printVertical(sector);
            return true;
        }

        //колонна (линия 12 чисел)
        if (isColumn(sector)) {
            printColumn(sector);
            return true;
        }

        //дюжина (квадрат 12 чисел)
        if (isDozen(sector)) {
            Color.printlnColorBlue("ДЮЖИНА");
            return true;
        }

        //сплит (2 соседних по горизонтали)
        if(isSplit(sector)) {
            Color.printlnColorBlue("СПЛИТ");
            return true;
        }

        //угол
        if(isAngle(sector)) {
            printAngle(sector);
            return true;
        }

        //линия (2 столбца)
        if(isLine(sector)) {
//            printLine(sector);
            Color.printlnColorBlue("ЛИНИЯ (2 столбца)");
            return true;
        }


        return false;
    }


    //распечатка корректных ставок
    public static void printCorrectBets() {
        Color.setTextColor(Color.ANSI_BLUE);
        Util.printArr(BETS_CORRECT);
        System.out.println(" $");

        Color.resetTextColor();
    }

    //выигрыш/проигрыш $
    public double getWiningMoney(double bet, String sector) {
        String str;

        int valWin = Integer.parseInt(sectorWin);

        //угадали номер сектора?
        if (sectorWin.compareToIgnoreCase(sector) == 0) {
            return bet * COEF_NUM;
        }

        //угадали чет/нечет?
        if (isEven(sector, valWin)) {
            return bet * COEF_EVEN_ODD;
        }

        if (isOdd(sector, valWin)) {
            return bet * COEF_EVEN_ODD;
        }

        //угадали красное/черное?
        if (isRed(sector, valWin)) {
            return bet * COEF_RED_BLACK;
        }

        if (isBlack(sector, valWin)) {
            return bet * COEF_RED_BLACK;
        }

        //угадали сектора 1-18, 19-36
        if (isSector1to18(sector, valWin)) {
            return bet * COEF_LOW_HEIGH;
        }

        if (isSector19to36(sector, valWin)) {
            return bet * COEF_LOW_HEIGH;
        }

        //Угадали ряд (столбец)
        if (isVertical(sector, valWin)) {
            return bet * COEF_VERTICAL;
        }

        //Угадали колону (линию 12 чисел)
        if (isColumn(sector, valWin)) {
            return bet * COEF_COLUMN;
        }

        //Угадали дюжину (квадрат 12 чисел)
        if (isDozen(sector, valWin)) {
            return bet * COEF_DOZEN;
        }

        //Угадали сплит (2 соседних числа)
        if(isSplit(sector, valWin)) {
            return bet * COEF_SPLIT;
        }

        //Угадали угол (квадрат 4 числа)
        if(isAngle(sector, valWin)) {
            return bet * COEF_ANGLE;
        }

        //Угадали линию (6 чисел, два столбца)
        if(isLine(sector, valWin)) {
            return bet * COEF_LINE;
        }

        //ничего не угадали
        return bet * -1;
    }

    //распечатываем вид поля рулетки
    public void printRouletteTable() {
        printRouletteTable(type);
    }

    public static void printRouletteTable(int type) {
        System.out.println(getNameTypeNum(type));

        for (int i = 0; i < 7; i++) {

            printRouletteTableZerro(type, i);

            for (int j = 0; j < 12; j++) {

                if ((i + 1) % 2 == 0) {
                    int val = (3 - (i / 2)) + (3 * j);
                    System.out.print("|");
                    if (isRed(val)) {
                        Color.printColor(String.format(" %2d  ", val), Color.ANSI_BLACK, Color.ANSI_RED_BACKGROUND);
                    } else {
                        System.out.printf(" %2d  ", val);
                    }
                } else if (i == 2 || i == 4) {
                    System.out.print("|-----");
                } else {
                    System.out.print("------");
                }
            }

            if ((i + 1) % 2 == 0) {
                System.out.println("|");
            } else if (i == 2 || i == 4) {
                System.out.println("|");
            } else {
                System.out.println("-");
            }
        }
    }

    //печатает сектор зеро для полей рулетки
    public static void printRouletteTableZerro(int type, int line) {
        for (int i = 0; i < TABLES[type][line].length(); i++) {
            char ch = TABLES[type][line].charAt(i);
            if (ch == '|' || ch == '-') {
                System.out.print(ch);
            } else {
                Color.printColor(String.format("%c", ch), Color.ANSI_BLACK, Color.ANSI_BLUE_BACKGROUND);
            }

        }
    }

    //установить чит - выигрышный сектор
    public boolean setSectorCheat(String sector) {

        if (Util.isStrInArr(SECTORS[type], sector)) {
            sectorCheat = sector;
            Color.printlnColorBlue(";)");
            return true;
        }

        return false;
    }

    //=========================================================
    //сектор красный?
    public static boolean isRed(int num) {
        switch (num) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 9:
            case 12:
            case 14:
            case 16:
            case 18:
            case 19:
            case 21:
            case 23:
            case 25:
            case 27:
            case 30:
            case 32:
            case 34:
            case 36:
                return true;

            default:
                break;
        }
        return false;
    }

    public boolean isRed(String str) {
        return Util.isStrInArr(RED_STR, str);
    }

    public boolean isRed(String str, int num)
    {
        return (isRed(str) && isRed(num));
    }


    //сектор черный?
    public boolean isBlack(int num) {
        //Zero не черный и не красный
        if (num == 0) {
            return false;
        }
        return !isRed(num);
    }

    public static boolean isBlack(String str) {
        return Util.isStrInArr(BLACK_STR, str);
    }

    public boolean isBlack(String str, int num)
    {
        return (isBlack(str) && isBlack(num));
    }

    //сектор чеТный?
    public boolean isEven(int num) {
        if (num == 0) {
            return false;
        }
        return ((num % 2) == 0);
    }

    public boolean isEven(String str) {
        return Util.isStrInArr(EVEN_STR, str);
    }

    public boolean isEven(String str, int num) {
        return (isEven(str) && isEven(num));
    }

    //сектор нечетный?
    public boolean isOdd(int num) {
        if (num == 0) {
            return false;
        }
        return !isEven(num);
    }

    public boolean isOdd(String str) {
        return Util.isStrInArr(ODD_STR, str);
    }

    public boolean isOdd(String str, int num) {
        return (isOdd(str) && isOdd(num));
    }

    //сектор от... до...?
    public boolean isSector(int num, int from, int to) {
        return ((num >= from) && (num <= to));
    }


    //сектор 1-18?
    public boolean isSector1to18(int num) {
        return isSector(num, 1, 18);
    }

    public boolean isSector1to18(String str) {
        return Util.isStrInArr(SECT_1_18, str);
    }

    public boolean isSector1to18(String str, int num) {
        return (isSector1to18(str) && isSector1to18(num));
    }

    //сектор 19-36?
    public boolean isSector19to36(int num) {
        return isSector(num, 19, 36);
    }

    public boolean isSector19to36(String str) {
        return Util.isStrInArr(SECT_19_36, str);
    }

    public boolean isSector19to36(String str, int num) {
        return (isSector19to36(str) && isSector19to36(num));
    }

    //возвращает int > 0 из строки, где два числа разделены разделителем (напр. "4-6" - вернет 4 или 6 при num = 1 или 2)
    //если что-то пошло не так, возращает 0
    private int getIntInStrNearSeparator(String str, int num, char charSeparator ) {
        str = Util.getStrNearSeparator(str, num, charSeparator);
        if(! Util.isInteger(str)) {
            return 0;
        }
        return  Integer.parseInt(str);
    }

    private int getFirstIntInStrNearSeparator(String str, char charSeparator) {
        str = Util.getStrNearSeparator(str,1, charSeparator);
        if(! Util.isInteger(str)) {
            return 0;
        }
        return  Integer.parseInt(str);
    }

    private int getLastIntInStrNearSeparator(String str, char charSeparator) {
        str = Util.getStrNearSeparator(str,2, charSeparator);
        if(! Util.isInteger(str)) {
            return 0;
        }
        return  Integer.parseInt(str);
    }


    //дюжина (квадрат 12 чисел)
    public boolean isDozen(String str) {
        return Util.isStrInArr(DOZEN_STR, str);
    }

    public boolean isDozen(String str, int num) {

        if(! isDozen(str)) {
            return false;
        }

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR);
        if ((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        return isSector(num, val1, val2);
    }

    //ряд (вертикальный столбец)
    public boolean isVertical(String str) {
        return Util.isStrInArr(VERTICAL_STR, str);
    }

    public boolean isVertical(String str, int num) {

        if(! isVertical(str)) {
            return false;
        }

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR);
        if ((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        return isSector(num, val1, val2);
    }

    public void printVertical(String str) { //распечатка чисел выбранной вертикали

        if(! isVertical(str)) {
            return;
        }

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR);
        if ((val1 == 0 ) || (val2 == 0)) {
            return;
        }

        Color.printColorBlue("РЯД (вертикаль) с числами: ");

        for (int i = val1; i <= val2; i++) {
            Color.printColorBlue(Integer.toString(i) );
            if(i < val2) {
                Color.printColorBlue(", ");
            }
        }
        System.out.println();
    }

    //колонна (горизонтальная линия 12 чисел)
    public boolean isColumn(String str) {
        String str1 = String.format("1%c34", SEPARATOR_COLUMN);
        String str2 = String.format("2%c35", SEPARATOR_COLUMN);
        String str3 = String.format("3%c36", SEPARATOR_COLUMN);

        return ((str.compareToIgnoreCase(str1) == 0) ||
                (str.compareToIgnoreCase(str2) == 0) ||
                (str.compareToIgnoreCase(str3) == 0));
    }

    public boolean isColumn(String str, int num) {

        if (num < 1) {
            return false;
        }

        num %= 3;
        String[] arr = {String.format("3%c36", SEPARATOR_COLUMN),
                        String.format("1%c34", SEPARATOR_COLUMN),
                        String.format("2%c35", SEPARATOR_COLUMN)};

        return (str.compareToIgnoreCase(arr[num]) == 0);
    }

    public void printColumn(String str) {

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR_COLUMN);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR_COLUMN);

        if((val1 == 0) || (val2 == 0)) {
            return;
        }

        if(val2 - val1 != 33) {
            return;
        }

        Color.printColorBlue("КОЛОННА (горизонталь) с числами: ");
        for (int i = val1; i < 37; i+= 3) {
            Color.printColorBlue(Integer.toString(i));
            if(i + 3 < 37) {
                Color.printColorBlue(", ");
            }
        }
        System.out.println();
    }

    //сплит (2 соседних числа)
    public boolean isSplit(String str) {
        String split = "";
        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR_SPLIT);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR_SPLIT);

        if((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 11; j++) {
                if((val1 == (j * 3) + i) && (val2 == val1 + 3)) {
                    return true;
                }
            }
        }
    return false;
    }

    public boolean isSplit(String str, int num) {

        if(num < 1) {
            return false;
        }

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR_SPLIT);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR_SPLIT);
        if((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        return ((val1 == num) || (val2 == num));
    }

    //распечатывает спислк сплитов
    public static void printSplit() {
        String split = "";
        int val1;
        int val2;

        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 11; j++) {
                val1 = (j * 3) + i;
                val2 = val1 + 3;
                split = String.format("%d%c%d", val1, SEPARATOR_SPLIT, val2);
                System.out.print(split + "  ");
            }
            System.out.println();
        }
    }

    // угол (квадрат 4 числа)
    public boolean isAngle(String str) {

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR_ANGLE);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR_ANGLE);
        if ((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        if(val1 > val2) {
            int tmp = val1;
            val1 = val2;
            val2 = tmp;
        }

        if((val1 % 3 == 1) && (val2 % 3 == 0)) {    //что бы не путали с вертикалью
            return false;
        }

        return ( ((val2 - val1 == 2) || (val2 - val1 == 4)) && val1 > 0);
    }

    public boolean isAngle(String str, int num) {

        if(num < 1) {
            return false;
        }

        if(!isAngle(str)) {
            return false;
        }

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR_ANGLE);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR_ANGLE);
        if ((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        if(val1 > val2) {
            int tmp = val1;
            val1 = val2;
            val2 = tmp;
        }

        if(val2 - val1 == 2) {
            val1--;
            val2++;
        }

        return (num == val1 || num == val2 || num == val1 + 1 || num == val1 + 3);
    }

    public void  printAngle(String str) {

        if(! isAngle(str)) {
            return;
        }

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR_ANGLE);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR_ANGLE);
        if ((val1 == 0 ) || (val2 == 0)) {
            return;
        }

        if(val1 > val2) {
            int tmp = val1;
            val1 = val2;
            val2 = tmp;
        }

        if(val2 - val1 == 2) {
            val1--;
            val2++;
        }

        String angle = String.format("УГОЛ (квадрат 4 числа): %d, %d, %d, %d",  val1, val1 + 1, val2, val2 - 1);
        Color.printlnColorBlue(angle);
    }

    // линия (6 чисел, 2 соседних столбца)
    public boolean isLine(String str) {

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR);


        if ((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        if(val1 > val2){
            return false;
        }

        return ((val1 > 0) && (val2 <37) && (val1 %3 == 1) && (val2 %3 == 0) && (val2 - val1 == 5)) ;
    }

    public boolean isLine(String str, int num) {

        int val1 = getFirstIntInStrNearSeparator(str, SEPARATOR);
        int val2 = getLastIntInStrNearSeparator(str, SEPARATOR);
        if ((val1 == 0 ) || (val2 == 0)) {
            return false;
        }

        if(val1 > val2){
            return false;
        }

        return ((val1 > 0) && (val1 %3 == 1) && (val2 %3 == 0) && (val2 - val1 == 5) && isSector(num, val1, val2)) ;
    }

    public void printLine(String str){
        if(! isLine(str)) {
            return;
        }

        int val = getFirstIntInStrNearSeparator(str, SEPARATOR);
        if (val == 0 ) {
            return;
        }

        Color.printColorBlue("ЛИНИЯ (2 столбца, шесть чисел): ");

        for (int i = val; i < val + 6; i++) {
            Color.printColorBlue(Integer.toString(i));
            if(i < val + 5) {
                Color.printColorBlue(", ");
            }
        }
        System.out.println();
    }

    //распечатывает подсказку- маленькое игровое поле
    public static void printSmallTab(int type) {
        int val;

        Color.setTextColor(Color.ANSI_BLUE);

        for (int i = 3; i > 0; i--) {
            String str = "|      ";
            if(i == 3) {
                if(type == 2) {
                    str = "| 000  ";
                }
                else if (type == 1) {
                    str = "|  00  ";
                }
            }

            if (i == 2) {
                if (type == 0) {
                   str = "|   0  ";
                }
                else if (type == 2) {
                    str = "|  00  ";
                }
            }

            if( (i == 1) && ((type == 1) || (type == 2)) ) {
                    str = "|   0  ";
            }

            System.out.print(str);

            for (int j = 0; j < 12; j++) {
                val = i + j * 3;
                System.out.print("|");
                if(isRed(val)) {
                    System.out.printf("  %dкр. ", val);
                }
                else {
                    System.out.printf("  %d    ", val);
                }
            }
            System.out.println();
        }

        Color.resetTextColor();
    }

    public void printSmallTab() {
        printSmallTab(type);
    }

    //
    public String getStrTypeSector(String sector) {

        //ряд (столбец)
        if (isVertical(sector)) {
            return "ряд";
        }

        //колонна (линия 12 чисел)
        if (isColumn(sector)) {
            return "колонна";
        }

        //дюжина (квадрат 12 чисел)
        if (isDozen(sector)) {
            return "дюжина";
        }

        //угол
        if(isAngle(sector)) {
            return "угол";
        }

        //линия (2 столбца)
        if(isLine(sector)) {
            return "линия";
        }

        //сплит
        if(isSplit(sector)) {
            return "сплит";
        }

        return "";
    }

}
