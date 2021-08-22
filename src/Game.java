//Реализованный функционал рулетки см. Roulette.java

import java.util.Scanner;

public class Game {

    private Player[]    players;
    private Roulette    roulette;
    private double      moneyBalance;    //денежный баланс- доход казино от игры

    private final int   MAX_PLAYERS = 10;
    private int         numPlayers;
    private int         numBots;
    private int         typeRoulette;

    Scanner sc;

    public Game(){
        sc = new Scanner(System.in);
    }

    //дополнительные команды при вводе
    private boolean inputCmd(String str, Player player){
        boolean res = false;

        //официальные команды
        switch (str){
            case "?":
                help();
                res = true;
                break;
            case "$":
                player.printMoney();
                res = true;
                break;
            case "tab":
                roulette.printSmallTab();
                res = true;
            default:
                break;
        }

        if (res){
            return true;
        }

        //читы
        String cmd = My.getStrCmd(str);
        if(cmd != null) {
            switch (cmd) {
                case "+":
                    player.setMoney(100);
                    My.printlnColorBlue(";)");
                    res = true;
                    break;
                case "-":
                    player.setMoney(1);
                    My.printlnColorBlue(";)");
                    res = true;
                    break;
                default:
                    res = roulette.setSectorCheat(cmd);
                    break;
            }
        }

        return res;
    }

    public void help(){
        helpInputCmd();
        roulette.helpInputSector();
        roulette.helpInputBet();
        helpGit();

        System.out.println();
    }

    private void helpGit() {
        My.setTextColor(My.ANSI_BLUE);
        System.out.println();
        System.out.println("https://github.com/AlexeyPertsukh/hw08-java-constructors-game-roulette");
        My.resetTextColor();
    }

    //справка по командам
    public void helpInputCmd(){
        My.setTextColor(My.ANSI_BLUE);
        System.out.println("+++++");
        System.out.println("Команды:");
        System.out.println("? - справка");
        System.out.println("$ - показать баланс игрока");
        System.out.println("tab - показать игровое поле");
        My.resetTextColor();
    }

    //ввод сектора ставки
    private boolean inputSector(Player player){
        System.out.print("Сектор: ");
        String str = player.nextSector(sc);

        if(inputCmd(str, player)){  //если команда - выходим
            return false;
        }

        if (str.compareToIgnoreCase("?") == 0)
        {
            roulette.helpInputSector();
            roulette.helpInputBet();
            System.out.println();
            return false;
        }

        if (!roulette.isCorrectSector(str))
        {
            My.printlnColorBlue("Недопустимый сектор, попробуйте еще раз");
            return false;
        }

        player.setSector(str);

        return true;
    }

    //ввод ставки
    private boolean inputBet(Player player){
        System.out.print("Ставка $: ");
        String str = player.nextBet(sc);


        if(inputCmd(str, player)){  //если команда - выходим
            return false;
        }

        if(!My.isDouble(str)){
            My.printlnColorBlue("Недопустимый ввод, попробуйте еще раз");
            return false;
        }

        double bet = Double.parseDouble(str);


        //корректная ставка?
        if(!Roulette.isCorrectBet(bet)){
            My.printlnColorBlue("Вы не можете сделать такую ставку.");
            My.printlnColorBlue("Допустимые ставки: ");
            Roulette.printCorrectBets();
            return false;
        }

        //деньги есть?
        if (!player.setBet(bet)){
            My.setTextColor(My.ANSI_BLUE);
            System.out.printf("Не хватает денег для ставки. У вас всего %.1f $  \n", player.getMoney());
            My.resetTextColor();
            return false;
        }

        return true;
    }

    //все игроки делают ставки
    public void setBet(){
        boolean b;

        System.out.println("Делайте ставки, господа! \n");
        for (Player player : players) {

            player.clearLastStat();
            //у игрока нет денег? пропускаем его
            if(player.isLost()){
                continue;
            }

            System.out.printf("%s (%.1f $)    \n", player.getName(), player.getMoney() );
            System.out.println("-----------------");
            System.out.println("? - вызов справки");

            //ставки
            do {
                b = inputBet(player);
            }while (!b);

            if(player.isBot()) {
                My.sleep(1000);
            }
            //сектор
            do {
                b = inputSector(player);
            }while (!b);

            if(player.isBot()) {
                My.sleep(2000);
            }

            System.out.println();
        }
    }

    //играть
    public void go() {

        My.printlnColorYellow  ("***************************************************");
        My.printlnColorYellow  ("SUPER CRAZY UNIVERSAL JAVA CONSOLE ROULETTE v1.6.21 ");
        My.printlnColorYellow  ("***************************************************");

        //Ввод типа рулетки

        for (int i = 0; i < Roulette.getNumTypes(); i++) {
            System.out.println((i + 1) + " - " + Roulette.getNameTypeNum(i));
        }

        do {
            System.out.print("Введите тип рулетки: ");

             String str = sc.next();
             if(My.isInteger(str)){
                 typeRoulette = Integer.parseInt(str);
             }
        }while(typeRoulette < 1 || typeRoulette > Roulette.getNumTypes());

        //
        typeRoulette--;
        roulette = new Roulette(typeRoulette);

        do {
            System.out.printf("Введите количество игроков (1-%d): ", MAX_PLAYERS);

            String str = sc.next();
            if(My.isInteger(str)){
                numPlayers = Integer.parseInt(str);
            }
        }while(numPlayers < 1 || numPlayers > MAX_PLAYERS);

        do {
            numBots = -1;
            System.out.printf("Введите количество ботов (0-%d): ", MAX_PLAYERS);

            String str = sc.next();
            if(My.isInteger(str)){
                numBots = Integer.parseInt(str);
            }
        }while(numBots < 0 || numBots > 10);

        players = new Player[numPlayers + numBots];

        for (int i = 0; i < numPlayers + numBots; i++) {
            players[i] = new Player("Игрок" + (i + 1));
            if(i >= numPlayers) {
                players[i] = new Bot("Игрок" + (i + 1));
            }
        }


        //
        boolean exit = false;
        do {
            //рисуем поле
            System.out.println();
            roulette.printRouletteTable();
            System.out.println();

            //делаем ставки
            setBet();
            //запускаем раунд
            goRound();

            if (playersLost()){    //все проиграли?
                System.out.println();
                My.printlnColorYellow("Игра окончена: все игроки проигрались в пух и прах 💀💀💀💀💀💀");
                break;
            }
            System.out.println();
            System.out.println("*******************************");
            System.out.print("1 - продолжить игру, 2 - выход: ");
            String strCmd = sc.next();
            if (strCmd.compareToIgnoreCase("2") == 0) {
                exit = true;
            }
        } while(!exit);
        My.printlnColorYellow("Приходите в наше казино еще.");
    }


    // Сыграть один раз и по результатам раздать/забрать деньги
    private void goRound(){

     roulette.go();

     //проверка выигрышей
        for (Player player : players) {

            if (player.getBet() == 0) {
                continue;
            }

            double money = roulette.getWiningMoney(player.getBet(), player.getSector());
            player.addMoney(money);
            moneyBalance += (money * -1);   //баланс игры (казино)
        }
        printResultTable(); //таблица результатов раунда
        System.out.printf("Баланс казино: %6.1f $   \n", moneyBalance);

        //выводим игроков, которые слились в этом раунде
        for (Player player : players) {
            if ((player.getBet()) != 0 && (player.isLost())) {
                My.printlnColorYellow(player.getName() + " всё проиграл и выбывает из игры 💀💀💀💀💀💀");
            }
        }
    }

    //проверяем- проиграли все игроки или нет?
    public boolean  playersLost(){
        boolean lost = true;
        for (Player player : players) {
//             if (!players[i].isLost() && !players[i].getBotStatus()){
            if (!player.isLost()) {
                lost = false;
                break;
            }
        }
        return lost;
    }

    public void printResultTable(){

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("   Имя                  Ставка         Сектор                     Выигрыш     Баланс  ");
        System.out.println("-------------------------------------------------------------------------------------");

        String nameSect = "";
        String sector = "";
        String strBet = "";
        String strWin = "";

        for (Player player : players) {
            if (player.getBet() > 0) {

                sector = String.format("%-10s", player.getSector().toUpperCase());
                nameSect = String.format("%-10s", roulette.getStrTypeSector(player.getSector()));
                strBet = String.format("%4.1f", player.getBet());
                strWin = String.format("%+4.1f", player.getLastAddMoney());
                strWin = String.format("%6s", strWin);


                System.out.printf("   %-19s %6s %20s %-12s  %8s  %8.1f   \n", player.getName(), strBet, sector, nameSect, strWin, player.getMoney());
            } else {
                String str = String.format("   %-19s %5s %14s %28s %10s  ", player.getName(), "💀", "💀", "💀", "💀");
                My.printlnColorYellow(str);
            }
            System.out.println("-------------------------------------------------------------------------------------");
        }

    }



}
