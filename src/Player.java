import java.util.Scanner;

//Игрок
public class Player {
    private String      name;
    private double      money;       //наличные деньги игрока
    private int         cntWin;     //счетчик выиграшей
    private int         cntLoss;    //счетчик проиграшей
    private  double     bet;            //ставка
    private  double     lastAddMoney;
    private  String     sector;        //сектор ставки

    public Player()
    {
        this("noname");
    }

    public Player(String name)
    {
        this(name, 50.0);
    }

    public Player(String name, double money)
    {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public double getLastAddMoney() {
        return lastAddMoney;
    }

    public void addMoney(double money){   //дать/взять деньги у игрока
        lastAddMoney = money;
        this.money += money;
        if (this.money < 0){
            this.money = 0;
        }
    }

    //игрок проигрался в пух и прах?
    public boolean isLost(){
        return (1 > money);  //из-за дробных коеф, у игрока может остаться 0.5$
    }

    public boolean setBet(double bet){     //сделать ставку

        //если есть деньги - ставим ставку
        if (money >= bet){
            this.bet = bet;
            return true;
        }

        return false;
    }

    //очистка статистики прошлой игры
    public void clearLastStat(){
        sector = "";
        lastAddMoney = 0;
        bet = 0;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setMoney(double money) {
        if(money < 0) {
            money = 0;
        }
        this.money = money;
    }

    public double getBet() {
        return bet;
    }

    public String getSector() {
        return sector;
    }

    public void info() {
        System.out.printf("%s. Ставка: сектор %d, %.1f $  \n", name, sector, bet);
    }

    public void setName(String name){
        this.name = name;
    }

    //распечатывает результаты последней игры
    public void printResultLastGame() {
        if(bet > 0) {
            System.out.printf("%s. Ставка  %.1f$    Сектор %s    Выигрыш  %+.1f$    Баланс  %.1f$  \n", name, bet, sector.toUpperCase(), lastAddMoney, money);
        }
        else{
            My.printlnColor(name +" не имеет денег и пропустил раунд \uD83D\uDC80\uD83D\uDC80\uD83D\uDC80\uD83D\uDC80\uD83D\uDC80\uD83D\uDC80 ", "yellow");
        }
    }

    //распечатывает количество денег
    public void printMoney(){
        My.setTextColor(My.ANSI_BLUE);
        System.out.printf("У вас %.1f $  \n", money);
        My.resetTextColor();
    }

    public String nextBet(Scanner sc) {
        return sc.next();
    }

    public String nextSector(Scanner sc) {
        return sc.next();
    }

    public boolean isBot() {
        return false;
    }

}
