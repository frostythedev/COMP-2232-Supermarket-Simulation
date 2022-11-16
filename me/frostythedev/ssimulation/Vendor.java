package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.exceptions.InvalidItemException;
import me.frostythedev.ssimulation.utils.Utilities;

import java.util.HashMap;
import java.util.Objects;

public abstract class Vendor {

    /*
     * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simeone Douglin-Welch
     *
     * This class defines the abstract implementation of the Vendor class which contains all the information related
     * to a particular vendor, their stocks as well as functions that can be used to alter the stock o the Vendor and
     *  sell stock to the supermarket
     *
     * There are also utility functions which are used for easier access when making logs, actionLogs, printing and
     * randomNumber Generation
     * */

    // Contains the name of the vendor
    private String name;

    // represents the profit of the vendor
    private int money;

    // Represents the items store of the vendor which will contain all the item the vendor stocks
    private HashMap<String, Item> items;

    public Vendor(String name) {
        this.name = name;
        this.money = 0;
        this.items = new HashMap<>();
    }

    // abstract function which can be used to uniquely define how a vendor initializes itself
    public abstract void init();

    // abstract function which can be used to uniquely define how a vendor restocks itself
    public abstract void restock();

    // Changes the stock of a given itemName by quantity
    public void alterStock(String itemName, int quantity){
        // checks if the vendor does not sell the current item
        if(getStock(itemName) == -1){

            // ensures that the quantity altered is greater than 0 which means stock is being added
            if(quantity > 0){

                // ensures that the item exists
                if(Item.Type.getBy(itemName) != null){
                    Item item = Objects.requireNonNull(Item.Type.getBy(itemName)).item();

                    // add to the stock of the item
                    item.setQuantity(quantity);

                    // Randomly sets the spoilt value between 5 and 10
                    item.setSpoilt(Utilities.generateRndNumber(5, 11));

                    // store the item and its updated stock in the items storage
                    items.put(itemName.toLowerCase(), item);
                }else{

                    // The item requested does not exist
                    try {
                        throw new InvalidItemException(itemName);
                    } catch (InvalidItemException e) {
                        e.printStackTrace();
                        //throw new RuntimeException(e);
                    }
                }
            }


        }else{

            // The vendor currently has some stock of the item, retrieve th curren stock and alter the quantity by
            // the supplied quantity
            Item item = items.get(itemName.toLowerCase());
            int newQuantity = item.getQuantity()+quantity;

            item.setQuantity(Math.max(newQuantity, 0));
        }
    }

    // Gets the reference to the Item Object if the vendor sells that item or returns null if it doesn't
    public Item getItem(String name){
        return items.getOrDefault(name, null);
    }

    // Sell stock is used to alter the quantity of the vendors stock, add the calculated profits and if the requested
    // amount is greater than the stock that the vendor has on hand, return the remainder of the stock that the
    // vendor was NOT able to fulfil.
    public int sellStock(String name, int quantity) throws InvalidItemException {
        if(getStock(name) == -1){
            // Vendor does not stock the item that is requested
            throw new InvalidItemException(name);
        }else {

            // success

            // if the vendor has enough stock, sell accordingly and add profits, return 0 as all orders were fulfilled
            if(getStock(name)-quantity >= 0){
                //success
                alterStock(name, -quantity);
                 money+=(quantity * Objects.requireNonNull(Item.Type.getBy(name)).getItemCost());
                return 0;

            }else{
                // not enough stock therefore a positive remainder will be returned, sell the available stock and set
                // the current stock to 0

                int overflow = -1*(getStock(name)-quantity);

                alterStock(name, -overflow);

                // return remainder
                return (overflow);
            }
        }
    }

    // Searches the vendors items to see if they stock a particular item, if they do then return the amount of stock
    // that is currently had else return -1 for no stock and no item
    public int  getStock(String itemName){

        if(items.containsKey(itemName.toLowerCase())){
            return items.get(itemName.toLowerCase()).getQuantity();
        }

        return -1;
    }

    // GETTERS and MUTATORS for Vendor class fields such as name, items and money
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
