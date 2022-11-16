package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.exceptions.InvalidItemException;
import me.frostythedev.ssimulation.utils.Utilities;
import me.frostythedev.ssimulation.vegetables.Vegetable;

import java.util.*;

public abstract class Vendor {

    private String name;
    private HashMap<String, Item> items;

    public Vendor(String name) {
        this.name = name;
        this.items = new HashMap<>();
    }

    public abstract void init();
    public abstract void restock();
    public void alterStock(String itemName, int quantity){
        if(getStock(itemName) == -1){

            if(Item.Type.getBy(itemName) != null){
                Item item = Objects.requireNonNull(Item.Type.getBy(itemName)).item();

                item.setQuantity(quantity);

                // Randomly sets the spoilt value between 5 and 10
                item.setSpoilt(Utilities.generateRndNumber(5, 11));

                items.put(itemName.toLowerCase(), item);
            }else{

                try {
                    throw new InvalidItemException(itemName);
                } catch (InvalidItemException e) {
                    e.printStackTrace();
                    //throw new RuntimeException(e);
                }
            }
        }else{

            Item item = items.get(itemName.toLowerCase());
            int newQuantity = item.getQuantity()+quantity;

            item.setQuantity(Math.max(newQuantity, 0));
        }
    }

    public Item getItem(String name){
        return items.getOrDefault(name, null);
    }

    public int removeStock(String name, int quantity){

        int updateStock = (getStock(name) - quantity);

        if((updateStock) < 0) return -1;
        return updateStock;
    }

    public int getStock(String itemName){

        if(items.containsKey(itemName)){
            return items.get(itemName).getQuantity();
        }

        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Item> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Item> items) {
        this.items = items;
    }
}
