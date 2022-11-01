package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.vegetables.Vegetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Vendor {

    private String name;
    private List<Item> items;

    public Vendor(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public abstract void init();
    public abstract void restock();

    public void addStock(Item item, int quantity){
        if(items.isEmpty()) {items.add(item); return;}

        if(getStock(item.getName()) == -1){
            items.add(item);
        }else{
            items.stream().forEach(item1 -> {
                if(item1.getName().equalsIgnoreCase(item.getName())){
                    item1.setQuantity(item1.getQuantity()+quantity);
                }
            });
        }

    }

    public int getStock(String itemName){
        for(Item item : items) {
            if(item.getName().equalsIgnoreCase(itemName)){
                return item.getQuantity();
            }
        }

        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
