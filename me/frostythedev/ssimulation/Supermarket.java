package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.exceptions.InvalidItemException;
import me.frostythedev.ssimulation.fruits.Fruit;
import me.frostythedev.ssimulation.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Supermarket {
    private static final int DEFAULT_TOTAL_CYCLES = 50;
    private int totalCycles;

    private int money, bank;

    private List<Vendor> vendors;
    private HashMap<String, Item> items;
    private int spoiltGoods;

    private List<LogRecord> logs;

    private RandomEventType randomEventType;
    private boolean runEvent;

    public Supermarket() {
        this(DEFAULT_TOTAL_CYCLES);
    }

    public Supermarket(int totalCycles) {
        this.totalCycles = totalCycles;
        this.vendors = new ArrayList<>();
        this.items = new HashMap<>();
        this.logs = new ArrayList<>();

        this.spoiltGoods = 0;
        this.money = 1000000;
        this.bank = 1000;
    }

    public void init(){

        for (Item.Type type : Item.Type.values()){
            Item item = type.item();
            item.setSpoilt(Utilities.generateRndNumber(5, 11));
            items.put(item.getName().toLowerCase(), item);
        }

        vendors.add(new Vendor("Fruits Are Here") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0){

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.FRUITS[Utilities.generateRndNumber(Item.Type.FRUITS.length)].getItemName() , rndAdd);

                    //getItem()

                    randomTotal-=rndAdd;
                }
            }

            @Override
            public void restock() {

                for(Item.Type fruit : Item.Type.FRUITS){
                    alterStock(fruit.getItemName(), Utilities.generateRndNumber(10, 101));
                }
            }
        });

        vendors.add(new Vendor("Tasty Vegetables") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0){

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.VEGETABLES[Utilities.generateRndNumber(Item.Type.VEGETABLES.length)].getItemName() , rndAdd);

                    //getItem()

                    randomTotal-=rndAdd;
                }
            }

            @Override
            public void restock() {

                for(Item.Type vege : Item.Type.VEGETABLES){
                    alterStock(vege.getItemName(), Utilities.generateRndNumber(10, 101));
                }
            }
        });

        vendors.add(new Vendor("All You Can Eat") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0){

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.values()[Utilities.generateRndNumber(Item.Type.values().length)].getItemName() , rndAdd);

                    //getItem()

                    randomTotal-=rndAdd;
                }
            }

            @Override
            public void restock() {

                for(Item.Type item : Item.Type.values()){
                    alterStock(item.getItemName(), Utilities.generateRndNumber(10, 101));
                }
            }
        });
    }

    public void customerPurchase(){

        List<String> keys = new ArrayList<>(items.keySet());
        //print("KEY SIZE: " + keys.size());
        //print(keys.toString());
        String randomKey = keys.get( Utilities.generateRndNumber(keys.size()) );

        Item item = (Item) items.get(randomKey);
        int rndAmt = Utilities.generateRndNumber(1, 21);

        if(rndAmt > item.getQuantity()){
            rndAmt = item.getQuantity();

            //VENDOR RESTOCK
        }

        double cost = rndAmt*item.getCost();

        money+=cost;
        item.setQuantity(item.getQuantity()-rndAmt);
    }

    public void chooseRandomEvent(){
        randomEventType = RandomEventType.values()[Utilities.generateRndNumber(RandomEventType.values().length)];
        runEvent = true;
    }

    public void run() {

        // cycle
        for (int i = 0; i < totalCycles; i++) {
            // perform simulation actions.

            if(i == 0){
                print("Simulation Started");
            }
            print("Total cycles performed are: %d".formatted(i));
            print("Total supermarket profit: %d".formatted(money));
            print("Total vendor profit: %d".formatted((vendors.get(0).getMoney() + vendors.get(1).getMoney() + vendors.get(2).getMoney())));


            // random even may occur each day
            if(i != 1 && Utilities.generateRndNumber(1, 101) < 25){
                // random even occurs
                chooseRandomEvent();
            }
            //print("DEBUG 2");

            if(runEvent){
                switch (randomEventType){
                    case FRIDGE_DOWN -> {
                        int fridgeCost = Utilities.generateRndNumber(0, 101);
                        money-=fridgeCost;
                    }
                    case ELECTRICITY_OFF -> {
                        int rndHours = Utilities.generateRndNumber(1,5);

                        for(Item item : items.values()){
                            int instaSpoilt = (int) (item.getQuantity()* (rndHours/100));

                            item.setQuantity(Math.max(item.getQuantity() - instaSpoilt, 0));

                        }
                    }
                    case SPOILT_ITEMS -> {

                        int rndHours = Utilities.generateRndNumber(1,11);

                        for(Item item : items.values()){
                            int instaSpoilt = (int) (item.getQuantity()* (rndHours/100));

                            item.setQuantity(Math.max(item.getQuantity() - instaSpoilt, 0));

                        }
                    }
                }
            }

            //print("DEBUG 3");

            for(Item item : items.values()){
                if(item.getQuantity() > 0){
                    item.setSpoiltCycle(item.getSpoiltCycle()-1);

                    if(item.getSpoiltCycle() % item.getSpoilt() == 0){
                        spoiltGoods++;
                        item.setQuantity(item.getQuantity()-1);
                    }
                }
            }



            if(i > 1 && i%10 == 0){
                chooseRandomEvent();
            }

            if(randomEventType != null && !randomEventType.equals(RandomEventType.NO_PURCHASES) && runEvent){
                for(int purchases = 0; purchases < 10; purchases++){
                    customerPurchase();
                }
            }

            // End of the Day

            for(Vendor vendor : vendors){
                vendor.restock();
            }

            // RESTOCK

            int noShowIndex = -1;
            if(randomEventType != null && randomEventType.equals(RandomEventType.VENDOR_NO_SHOW)){
                noShowIndex = Utilities.generateRndNumber(0, 3);
            }

            for(Item item : items.values()){
                int orderQty;
                if(item.isVegetable()){
                     orderQty = 100-item.getQuantity();

                     if((bank+money) < (orderQty*item.getCost())){
                         //Game over
                         System.out.println("Simulation has been terminated because the supermarket ran out of money");
                         return;
                     }

                     //System.out.printf("Vendor %d will no shown\n", noShowIndex);

                    Vendor veg = vendors.get(1);

                    if(noShowIndex == 1){

                        veg = vendors.get(2);

                        money-=(orderQty*item.getCost());

                        int remainder = 0;
                        try {
                            remainder = veg.sellStock(item.getName(), orderQty);
                        } catch (InvalidItemException e) {
                            throw new RuntimeException(e);
                        }
                        if(remainder > 0){
                            // No stock from vendor cause of no show
                            money+=(remainder*item.getCost());
                        }

                    }else{

                        //print("RESTOCKING: " + item.getName());
                        try {

                            money-=(orderQty*item.getCost());

                            int remainder = veg.sellStock(item.getName(), orderQty);
                            if(remainder > 0){

                                // Both veg and fruit

                                if(noShowIndex != 2){
                                    int remainder2 = vendors.get(2).sellStock(item.getName(), remainder);

                                    if(remainder2 > 0) {
                                        // not enough stock to supply

                                        money+=(remainder2*item.getCost());
                                    }
                                }else{
                                    // second vendor no show, refund remainder
                                    money+=(remainder*item.getCost());
                                }
                            }

                        } catch (InvalidItemException e) {
                            e.printStackTrace();
                        }
                    }




                }else{
                    orderQty = 200- item.getQuantity();

                    if((bank+money) < (orderQty*item.getCost())){
                        //Game over
                        System.out.println("Simulation has been terminated because the supermarket ran out of money");
                        return;
                    }

                    if(noShowIndex == 0){

                    }

                    Vendor fruits = vendors.get(0);
                    try {
                        money-=(orderQty*item.getCost());

                        int remainder = fruits.sellStock(item.getName(), orderQty);
                        if(remainder > 0){

                            // Both veg and fruit
                            int remainder2 = vendors.get(2).sellStock(item.getName(), remainder);

                            if(remainder2 > 0) {
                                // not enough stock to supply

                                money+=(remainder2*item.getCost());
                            }
                        }
                    } catch (InvalidItemException e) {
                        e.printStackTrace();
                       // throw new RuntimeException(e);
                    }

                }
            }

            runEvent = false;

            bank+=money;
            money = 0;
        }
    }

    public void log(String log){

    }

    public void displayLogs() {
    }

    private void print(String message){
        System.out.printf(message + "\n");
    }
}
