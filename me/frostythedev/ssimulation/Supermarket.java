package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.fruits.Fruit;
import me.frostythedev.ssimulation.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Supermarket {
    private static final int DEFAULT_TOTAL_CYCLES = 50;
    private int totalCycles, money;

    private List<Vendor> vendors;
    private HashMap<String, Item> items;

    private List<LogRecord> logs;

    public Supermarket() {
        this(DEFAULT_TOTAL_CYCLES);
    }

    public Supermarket(int totalCycles) {
        this.totalCycles = totalCycles;
        this.vendors = new ArrayList<>();
        this.items = new HashMap<>();
        this.logs = new ArrayList<>();
    }

    public void init(){
        vendors = new ArrayList<>();

        vendors.add(new Vendor("Fruits Are Here") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0){

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.FRUITS[Utilities.generateRndNumber(Item.Type.FRUITS.length)].getItemName() , rndAdd);

                    getItem()

                    randomTotal-=rndAdd;
                }
            }

            @Override
            public void restock() {

            }
        });
    }

    public void customerPurchase(){
        Item item = (Item) items.values().toArray()[Utilities.generateRndNumber(items.values().size())];
        int rndAmt = Utilities.generateRndNumber(1, 21);

        if(rndAmt > item.getQuantity()){
            rndAmt = item.getQuantity();

            //VENDOR RESTOCK
        }

        double cost = rndAmt*item.getCost();

        money+=cost;
        item.setQuantity(item.getQuantity()-rndAmt);
    }

    public void cycle() {
        for (int i = 0; i < totalCycles; i++) {
            // perform simulation actions.

            for(Item item : items.values()){
                item.setSpoiltCycle(item.getSpoiltCycle()-1);

                if(item.getSpoiltCycle() % item.getSpoilt() == 0){

                    if(item.getQuantity() > 0){
                        item.setQuantity(item.getQuantity()-1);
                    }else{
                        items.remove(item.getName());
                    }


                }
            }

            for(int purchases = 0; purchases < 10; purchases++){
                customerPurchase();
            }

            if(i > 1 && i%10 == 0){
                runRandomEvent();
            }
        }
    }

    public void log(String log){

    }

    public void displayLogs() {
    }
}
