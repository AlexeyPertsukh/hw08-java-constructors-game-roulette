/*
Создать игру рулетка.
Обязательный режим:чет/нечет.
Классы:Игрок(деньги), Рулетка, Игра(алгоритм), Main(старт игры).
Использовать приватные методы.

Перцух Алексей(c), Запорожье 2021, учебная группа "Java A01" Академия "ШАГ"
 */

public class Main {

    public static void main(String[] args) {
    //ЧИТ КОДЫ
    // cmd+       установить игроку баланс 100 $
    // cmd-       установить игроку баланс 1$
    // cmd<11>    установить выигрышный сектор следующего раунда, напр: cmd000  - выиграет сектор "000"

        Game game = new Game();
        game.go();
    }

}
