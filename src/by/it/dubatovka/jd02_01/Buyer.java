package by.it.dubatovka.jd02_01;

import java.util.ArrayList;
import java.util.List;

class Buyer extends Thread implements IBuyer, IUseBascket {
    private int number;
    private List<Good> basket;
    private boolean pensioneer;

//    void setPensioneer(boolean pensioneer) {
//        this.pensioneer = pensioneer;
//    }

     Buyer(int number, boolean pensioneer) {
        this.number = number;
        this.pensioneer = pensioneer;
        this.setName(pensioneer ? "Пенсионер " + number + " " : "Покупатель " + number + " ");
        start();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void run() {
        goToMarket();
        takeBasket();
        chooseGoods();
        goToOut();
    }

    @Override
    public void goToMarket() {
        System.out.println(this + "вошел в магазин");
    }

    @Override
    public void chooseGoods() {
        int quantity = Rnd.fromTo(1, 5);
        for (int i = 0; i < quantity; i++) {
            pause();
            putGoodsToBasket();
        }
        System.out.println(this + " закончил выбирать товар");
    }

    @Override
    public void goToOut() {
        System.out.println(this + "вышел из магазина");
    }

    @Override
    public void takeBasket() {
        pause();
        basket = new ArrayList<>();
        System.out.println(this + "взял корзину");

    }

    @Override
    public void putGoodsToBasket() {
        Good item = Good.random();
        basket.add(item);
        System.out.println(this + "положил " + item.name() + " в корзину");
    }

    private void pause() {
        double timeOut = Rnd.fromTo(100, 200);
        timeOut = pensioneer ? timeOut * 1.5 : timeOut;

        try {
            sleep((long) timeOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Некорректное завершение ожидания при выборе товара");
        }
    }

}
