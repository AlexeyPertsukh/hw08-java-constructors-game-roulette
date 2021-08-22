import java.util.Scanner;

//Бот
public class Bot extends Player {

    private final String[] SECTORS = {"even", "odd", "четное", "нечетное", "чёт", "нечет",
                                        "red", "black", "красное", "черное", "red", "black",
                                        "1-18", "19-36", "1-18", "19-36", "1-18", "19-36",
                                        "1-12", "13-24", "25-36","1-12", "13-24", "25-36",          //дюжина
                                        "1:34", "2:35", "3:36",                                     //колонна
                                        "Меня зовут Кеша", "Привет, сам как?", "Всё на зеро, господа!", "Ставлю на кон вон ту дамочку!", "Где я?", "Принесите пива, плиз"
                                    };

    private final String[] SECTORS_RISK ={  "1-6", "4-9", "7-12", "10-15", "13-18", "19-24", "22-27", "25-30", "28-33", "31-36",            //линия (6 чисел, 2 столб.)
                                            "1#5", "5#9", "12#14", "10#14", "14#16", "20#24", "27#29", "29#33", "33#35", "31#35",           //угол
                                            "1-3", "4-6", "7-9", "10-12", "13-15", "16-18", "19-21", "22-24", "25-27", "28-30", "31-33", "34-36", //ряд
                                            "15,18", "23,26", "29,32", "3,6", "1,4", "25,28", "19,22",                                            //сплиты
                                            "0", "00", "000"
                                            };

    public Bot()
    {
        this("noname");
    }

    public Bot(String name)
    {
        super(name + "[БОТ]");
    }

    public Bot(String name, double money)
    {
        super(name + "[БОТ]", money);
    }

    @Override
    public String nextBet(Scanner sc) {
        double money = getMoney();
        double bet = 0;

        boolean isHighRate = false;
        if ((int) (Math.random() * 7) == 1) {
            isHighRate = true;
        }

        if(money >= 150) {
            bet = 100;
        }
        else
        if(money >= 100) {
            if(isHighRate) {
                bet = 100;
            } else {
                bet = 50;
            }
        }
        else
        if(money > 50) {
            bet = 50;
        }
        else
        if(money == 50) {
            if(isHighRate) {
                bet = 50;
            } else {
                bet = 25;
            }
        }
        else
        if(money > 25) {
            bet = 25;
        }
        else
        if(money >= 10) {
            bet = 10;
        }
        else
        if(money >= 5) {
            bet = 5;
        }
        else
        if(money >= 1) {
            bet = 1;
        }

        String strBet = String.format("%.0f", bet);
        System.out.println(strBet);
        return strBet;
    }

    @Override
    public String nextSector(Scanner sc) {

        int num = (int) (Math.random() * SECTORS.length);
        String strSector = SECTORS[num];

        boolean isRisk = false;
        if ((int) (Math.random() * 13) == 1) { //иногда рискуем
            isRisk = true;
        }

        if(getMoney() >= 200) {
            isRisk = true;  //слишком много денег- пусть рискует
        }

        //иногда- ставим на рискованные сектора
        if (isRisk) {
            num = (int) (Math.random() * SECTORS_RISK.length);
            strSector = SECTORS_RISK[num];
        }
        System.out.println(strSector);
        return strSector;
    }

    @Override
    public boolean isBot() {
        return true;
    }

}
