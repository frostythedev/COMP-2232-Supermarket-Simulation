package me.frostythedev.ssimulation;

import me.frostythedev.ssimulation.exceptions.FruitNotAvailableException;
import me.frostythedev.ssimulation.exceptions.InvalidItemException;
import me.frostythedev.ssimulation.exceptions.NotEnoughtFunds;
import me.frostythedev.ssimulation.exceptions.VegetableNotAvailableException;
import me.frostythedev.ssimulation.records.ActionLog;
import me.frostythedev.ssimulation.records.LogRecord;
import me.frostythedev.ssimulation.utils.Utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Supermarket {

    /*
     * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
     *
     * This class defines the entire implementation supermarket simulation which contains the occurance of every
     * specification of the design document, storage cotainers for all relevant fields as well as references to
     * created vendors and their unqiue implementation of init and restock
     *
     * There are also utility functions which are used for easier access when making logs, actionLogs, printing and
     * randomNumber Generation
     * */

    // Defines the default cycles for the simulation if none is provided
    private static final int DEFAULT_TOTAL_CYCLES = 50;

    // Defines the amount of cycles supplied to the simulation, default will assign to DEFAULT_TOTAL_CYCLES
    private int totalCycles;

    // Stores the money that the supermarket makes in profit and stores the amount of money stored in the bank
    private int profit, bank;

    // Defines if the simulation prints detailed messages in its system messages
    private boolean verbose;

    // Stores the location of the logFile that would be outputed too
    private String logFile;

    // Stores a list (container) of all the vendors of the supermarket
    private List<Vendor> vendors;

    // Stores the name and Item object of each item that the supermarket owns
    private HashMap<String, Item> items;

    // Stores the number of spoilt goods that the supermarket has
    private int spoiltGoods;

    // Stores all the records made by the simulation when it runs
    private List<LogRecord> logs;

    // Stores all actionLogs such as spoiltFood, vendorPurchase etc omade by the simulation
    private List<ActionLog> actionLogs;

    // Stores the current random event that is to occur
    private RandomEventType randomEventType;

    // Defines if the afforementioned randomEvent should occur in that particular cycle of the simulation
    private boolean runEvent;

    // Default constructor, sets the number of cycles to the default amount of cycles defined above
    public Supermarket() {
        this(DEFAULT_TOTAL_CYCLES);
    }

    // Overloaded constructor, sets the total cycles to the inputted parameter totalCycles
    public Supermarket(int totalCycles) {
        this.totalCycles = totalCycles;
        /*this.vendors = new ArrayList<>();
        this.items = new HashMap<>();
        this.logs = new ArrayList<>();
        this.actionLogs = new ArrayList<>();

        this.spoiltGoods = 0;
        this.money = 1000000;
        this.bank = 1000;*/
    }

    // Initializes the simulation by clearing out all of its dataStores and resettting fields to their default
    // value, initializes all the items that the supermarket would carry with an initial quantity of 0, initializes
    // all the vendors of the supermarket and defines their init and restock function which is different depending on
    // what that vendor supplies
    public void init() {

        this.vendors = new ArrayList<>();
        this.items = new HashMap<>();
        this.logs = new ArrayList<>();
        this.actionLogs = new ArrayList<>();

        this.spoiltGoods = 0;
        this.profit = 10000;
        this.bank = 1000;

        // Inits all items to an 'empty' item of quantity 0, then adds it to the supermarkets store 'items'
        // Also generates the random spoilt value which would determine how many cycles it takes for a single item to
        // go bad
        for (Item.Type type : Item.Type.values()) {
            Item item = type.item();
            item.setSpoilt(Utilities.generateRndNumber(5, 11));
            items.put(item.getName().toLowerCase(), item);
        }

        // Initializes a vendor and their stock, their initial stock is randomly generated and stored to each
        // respective item
        // A vendors restock involves adding a random amount between 10 and 100 to the current stock
        vendors.add(new Vendor("Fruits Are Here") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0) {

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.FRUITS[Utilities.generateRndNumber(Item.Type.FRUITS.length)].getItemName(), rndAdd);

                    //getItem()

                    randomTotal -= rndAdd;
                }

            }

            @Override
            public void restock() {

                for (Item.Type fruit : Item.Type.FRUITS) {
                    alterStock(fruit.getItemName(), Utilities.generateRndNumber(10, 101));
                }
            }
        });

        vendors.add(new Vendor("Tasty Vegetables") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0) {

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.VEGETABLES[Utilities.generateRndNumber(Item.Type.VEGETABLES.length)].getItemName(), rndAdd);

                    //getItem()

                    randomTotal -= rndAdd;
                }
            }

            @Override
            public void restock() {

                for (Item.Type vege : Item.Type.VEGETABLES) {
                    alterStock(vege.getItemName(), Utilities.generateRndNumber(10, 101));
                }
            }
        });

        vendors.add(new Vendor("All You Can Eat") {
            @Override
            public void init() {

                int randomTotal = Utilities.generateRndNumber(10, 101);

                while (randomTotal != 0) {

                    int rndAdd = Utilities.generateRndNumber(randomTotal);

                    alterStock(Item.Type.values()[Utilities.generateRndNumber(Item.Type.values().length)].getItemName(), rndAdd);

                    //getItem()

                    randomTotal -= rndAdd;
                }
            }

            @Override
            public void restock() {

                for (Item.Type item : Item.Type.values()) {
                    alterStock(item.getItemName(), Utilities.generateRndNumber(10, 101));
                }
            }
        });
    }

    // Defines a customer purchase which chooses a random item and random amount which is bought from the
    // supermarket, it logs the respective transaction and decreases the stock by the amount bought
    // Takes in 1 parameter which indiciates which cycle the purchase is taking place in for logging purposes
    public void customerPurchase(int cycle) {

        List<String> keys = new ArrayList<>(items.keySet());
        String randomKey = keys.get(Utilities.generateRndNumber(keys.size()));

        Item item = items.get(randomKey);
        int rndAmt = Utilities.generateRndNumber(1, 21);

        if (rndAmt > item.getQuantity()) {
            rndAmt = item.getQuantity();

        }

        double cost = rndAmt * item.getCost();

        this.actionLogs.add(new ActionLog(cycle, "Total customer purchases: %s (%d)".formatted((item.isFruit() ?
                "Fruit" : "Vegetable"), rndAmt)));


        profit += cost;
        item.setQuantity(item.getQuantity() - rndAmt);
    }

    // randomly chooses a random event and sets it to run over the curren cycle
    public void chooseRandomEvent() {
        randomEventType = RandomEventType.values()[Utilities.generateRndNumber(RandomEventType.values().length)];
        runEvent = true;
    }

    // Returns a formatted string that shows the respective commands, and the value that was supplied at runtime
    public String getCommandsPassed() {
        return ("""
                Verbose: %s
                Iter: %s
                logFile:%s""")
                .formatted(verbose ? true : "false (default)", (totalCycles == 50 ? "50 (default)" :
                                totalCycles),
                        (logFile == null ? "N/A, default printing to commandline" : logFile));
    }

    // Begins the simulation
    public void run() {

        if(!logs.isEmpty()){
            log(-1, "========== NEW SIMULATION =========");
        }
        //logs.clear();

        // Start running cycles of the simulation
        for (int i = 0; i < totalCycles; i++) {
            // perform simulation actions.

            if (i == 0) {
                print("Simulation Started");
            }

            // random event may occur each day with a 25% probably
            if (i != 1 && Utilities.generateRndNumber(1, 101) < 25) {
                // random even occurs
                chooseRandomEvent();
                log(i, "RANDOM EVENT> " + randomEventType.name());
            }

            // Every 10 cycles a random event WILL 100% occur, this chooses that random event and logs it
            if (i > 1 && i % 10 == 0) {
                chooseRandomEvent();
                log(i, "RANDOM EVENT> " + randomEventType.name());
            }

            // Random events which may occur at the start of each day and their implementation
            if (runEvent) {
                switch (randomEventType) {
                    case FRIDGE_DOWN -> {
                        int fridgeCost = Utilities.generateRndNumber(0, 101);
                        profit -= fridgeCost;
                    }
                    // If the electricity off event is choosen then a random percentage of items will spoilt based on
                    // the hours the electricty stays off, this may be between 1 and 5 hours
                    case ELECTRICITY_OFF -> {
                        int rndHours = Utilities.generateRndNumber(1, 6);

                        for (Item item : items.values()) {
                            int instaSpoilt = item.getQuantity() * (rndHours / 100);

                            //this.actionLogs.add(new ActionLog(i, "Spoilt %s (%s): %d".formatted((item.isFruit() ? "Fruit" : "Vegetable"), item.getName(), instaSpoilt)))

                            item.setQuantity(Math.max(item.getQuantity() - instaSpoilt, 0));

                        }
                    }
                    // if spoiltItems randomEvent is choosen then a random percentage of the items (between 1-10) in
                    // stock will instantly spoil according to the randomly generated number, a log is made when this
                    // happens
                    case SPOILT_ITEMS -> {

                        int rndHours = Utilities.generateRndNumber(1, 11);

                        for (Item item : items.values()) {
                            int instaSpoilt = (item.getQuantity() * (rndHours / 100))+1;

                            this.actionLogs.add(new ActionLog(i, "Spoilt %s (%s): %d".formatted((item.isFruit() ?
                                    "Fruit" : "Vegetable"), item.getName(), instaSpoilt)));

                            item.setQuantity(Math.max(item.getQuantity() - instaSpoilt, 0));

                        }
                    }
                }
            }

            // Every cycle all item's spoiltCycle value will decrement by 1, if the spoiltCycle mod the spoilt value
            // is equal to 0 then it means that the item has spoilt as the adequate amount of cycles has passed, when
            // spoilt the item is removed from being able to be sold
            for (Item item : items.values()) {
                if (item.getQuantity() > 0) {
                    item.setSpoiltCycle(item.getSpoiltCycle() - 1);

                    if (item.getSpoiltCycle() % item.getSpoilt() == 0) {
                        spoiltGoods++;
                        this.actionLogs.add(new ActionLog(i, "Spoilt %s (%s): %d".formatted((item.isFruit() ?
                                "Fruit" : "Vegetable"), item.getName(), 1)));
                        item.setQuantity(item.getQuantity() - 1);
                    }
                }
            }

            // Checks if a randomEventType is not null and that it is not equal to no purchases and that the event
            // must run
            // if all the conditions are met, customers will make 10 purchases, if not then no purchases will be made
            if (randomEventType != null && !randomEventType.equals(RandomEventType.NO_PURCHASES) && runEvent) {
                for (int purchases = 0; purchases < 10; purchases++) {
                    customerPurchase(i);
                }
            }

            // End of the Day

            // All vendors restock at the end of the day a number between 10 and 100 of each item
            for (Vendor vendor : vendors) {
                vendor.restock();
            }

            // RESTOCK

            // Randomly chooses a vendor which will noshow based on the randomEventType choosen
            int noShowIndex = -1;
            if (randomEventType != null && randomEventType.equals(RandomEventType.VENDOR_NO_SHOW)) {
                noShowIndex = Utilities.generateRndNumber(0, 3);
                this.actionLogs.add(new ActionLog(i,
                        "Vendor does not deliver goods: %s".formatted(vendors.get(noShowIndex).getName())));

            }

            // Supermarket is restocking all items with their maximum amount by making orders to each vendor
            // respective of what they sell
            for (Item item : items.values()) {
                int orderQty;
                if (item.isVegetable()) {
                    orderQty = 100 - item.getQuantity();

                    //System.out.printf("Vendor %d will no shown\n", noShowIndex);

                    if(orderQty != 0){

                        // Ensures that the supermarket is not bankrupt ordering more items. If it is then the
                        // simulation ends
                        if ((bank + profit) < (orderQty * item.getCost())) {
                            //Game over
                            try {
                                throw new NotEnoughtFunds();
                            } catch (NotEnoughtFunds e) {
                                log(i, "EXCEPTION:>" + e + " - " + e.getMessage());
                                print("Simulation has ended because the supermarket ran out of money");
                                break;
                            }
                        }

                        Vendor veg = vendors.get(1);

                        if (noShowIndex == 2) {

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(100);

                            // remainder refers to a value which is returned if a vendor cannot completely
                            // fulfill an order of a particular amount
                            // gotOrders refers to the number of fulfilled orders of a product by that vendor
                            int remainder = 0;
                            int gotOrders = 0;
                            try {

                                remainder = veg.sellStock(item.getName(), orderQty);
                                gotOrders = orderQty-remainder;

                                orderQty -= gotOrders;

                            } catch (InvalidItemException e) {
                                log(i, e + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                            if (orderQty > 0) {
                                // No stock from vendor cause of no show
                                profit += (orderQty * item.getCost());
                                item.setQuantity(item.getQuantity() - orderQty);
                            }

                            // Logs the fulfilled orders into the simulation logBook and a corresponding
                            // actionLog
                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                            veg.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));


                        } else if (noShowIndex == 1) {

                            veg = vendors.get(2);

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(100);

                            // remainder refers to a value which is returned if a vendor cannot completely
                            // fulfill an order of a particular amount
                            // gotOrders refers to the number of fulfilled orders of a product by that vendor
                            int remainder = 0;
                            int gotOrders = 0;
                            try {

                                remainder = veg.sellStock(item.getName(), orderQty);
                                gotOrders = orderQty-remainder;

                                orderQty -= gotOrders;

                            } catch (InvalidItemException e) {
                                log(i, e + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                            if (orderQty > 0) {
                                // No stock from vendor cause of no show
                                profit += (orderQty * item.getCost());
                                item.setQuantity(item.getQuantity() - orderQty);
                            }

                            // Logs the fulfilled orders into the simulation logBook and a corresponding
                            // actionLog
                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                            veg.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));
                        } else {

                            try {

                                profit -= (orderQty * item.getCost());
                                item.setQuantity(100);

                                // remainder refers to a value which is returned if a vendor cannot completely
                                // fulfill an order of a particular amount
                                // gotOrders refers to the number of fulfilled orders of a product by that vendor
                                int remainder = veg.sellStock(item.getName(), orderQty);

                                int gotOrders = orderQty-remainder;

                                orderQty -= gotOrders;


                                if (orderQty > 0) {


                                    // Logs the fulfilled orders into the simulation logBook and a corresponding
                                    // actionLog
                                    log(i, ("ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total " +
                                            "Purchased: %s")
                                            .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                                    veg.getName(), (gotOrders)));

                                    this.actionLogs.add(new ActionLog(i,
                                            "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : 
                                                    "Vegetable"), gotOrders)));

                                    // Both veg and fruit - reassigns the veg vendor to another vendor
                                    veg = vendors.get(2);

                                    // remainder refers to a value which is returned if a vendor cannot completely
                                    // fulfill an order of a particular amount
                                    // gotOrders refers to the number of fulfilled orders of a product by that vendor
                                    int remainder2 = veg.sellStock(item.getName(), orderQty);

                                    int gotOrders2 = orderQty-remainder2;
                                    orderQty -= gotOrders2;

                                    if (orderQty > 0) {
                                        // not enough stock to supply

                                        profit += (orderQty * item.getCost());
                                        item.setQuantity(item.getQuantity() - remainder2);

                                        try {
                                            throw new VegetableNotAvailableException(item.getName());
                                        } catch (VegetableNotAvailableException e) {
                                            log(i, "EXCEPTION> " + e.getMessage());
                                            print("EXCEPTION> " + e);
                                            //e.printStackTrace();
                                        }
                                    }

                                    // Logs the fulfilled orders into the simulation logBook and a corresponding
                                    // actionLog
                                    log(i, ("ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total " +
                                            "Purchased: %s")
                                            .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                                    veg.getName(), (gotOrders2)));

                                    this.actionLogs.add(new ActionLog(i,
                                            "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : 
                                                    "Vegetable"), gotOrders2)));
                                }

                            } catch (InvalidItemException e) {
                                log(i, "EXCEPTION> " + e.getMessage());
                            }
                        }
                    }

                } else {
                    orderQty = 200 - item.getQuantity();

                    if(orderQty != 0){

                        if ((bank + profit) < (orderQty * item.getCost())) {
                            //Game over
                            try {
                                throw new NotEnoughtFunds();
                            } catch (NotEnoughtFunds e) {
                                log(i, "EXCEPTION:>" + e + " - " + e.getMessage());
                                print("Simulation has ended because the supermarket ran out of money");
                                break;
                            }
                        }

                        Vendor fruits = vendors.get(0);

                        if (noShowIndex == 0) {
                            fruits = vendors.get(2);

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(200);

                            // remainder refers to a value which is returned if a vendor cannot completely
                            // fulfill an order of a particular amount
                            // gotOrders refers to the number of fulfilled orders of a product by that vendor
                            int remainder = 0;
                            int gotOrders = 0;
                            try {

                                remainder = fruits.sellStock(item.getName(), orderQty);
                                gotOrders = orderQty-remainder;

                                orderQty -= gotOrders;

                            } catch (InvalidItemException e) {
                                log(i, "EXCEPTION> " + e.getMessage());
                                e.printStackTrace();
                            }

                            if (orderQty > 0) {
                                // first vendor didnt show up, second didnt have enough stock
                                profit += (orderQty * item.getCost());
                                item.setQuantity(item.getQuantity() - orderQty);
                            }

                            // Logs the fulfilled orders into the simulation logBook and a corresponding
                            // actionLog
                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"),
                                            item.getCost(),
                                            fruits.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));

                        } else if (noShowIndex == 2) {

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(200);

                            // remainder refers to a value which is returned if a vendor cannot completely
                            // fulfill an order of a particular amount
                            // gotOrders refers to the number of fulfilled orders of a product by that vendor
                            int remainder = 0;
                            int gotOrders = 0;
                            try {

                                remainder = fruits.sellStock(item.getName(), orderQty);
                                gotOrders = orderQty - remainder;

                                orderQty -= gotOrders;

                            } catch (InvalidItemException e) {
                                log(i, e + ": " + e.getMessage());
                            }

                            if (remainder > 0) {
                                // second vendor didnt show up, first didnt have enough stock, return the remaining
                                // qty that aas not fulfilled

                                profit += (orderQty * item.getCost());
                                item.setQuantity(item.getQuantity() - orderQty);
                            }

                            // Logs the fulfilled orders into the simulation logBook and a corresponding
                            // actionLog
                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"),
                                            item.getCost(),
                                            fruits.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));
                        } else {

                            // This case refers to when all vendors will show for this type of item

                            try {

                                profit -= (orderQty * item.getCost());
                                item.setQuantity(200);

                                // remainder refers to a value which is returned if a vendor cannot completely fulfil
                                // an order of a particular amount
                                int remainder = fruits.sellStock(item.getName(), orderQty);

                                // fulfilled orders that was delivered from the supplier to the supermarket
                                int gotOrders = orderQty-remainder;

                                // sets the newOrderQty to less than the orders that were delivered
                                orderQty -= gotOrders;

                                // if the supermarket has to go to another vendor for more supply, orderQty will be
                                // greater than 0 as the remainder not supplied was a number greater than 0
                                if (orderQty > 0) {

                                    // Logs the fulfilled orders into the simulation logBook and a corresponding
                                    // actionLog
                                    log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                            .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"),
                                                    item.getCost(),
                                                    fruits.getName(), (gotOrders)));

                                    this.actionLogs.add(new ActionLog(i,
                                            "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));

                                    // Both veg and fruit

                                    //sets the new vendor to the other that can supply
                                    fruits = vendors.get(2);

                                    // remainder refers to a value which is returned if a vendor cannot completely
                                    // fulfill an order of a particular amount
                                    // gotOrders refers to the number of fulfilled orders of a product by that vendor
                                    int remainder2 = fruits.sellStock(item.getName(), orderQty);

                                    int gotOrders2 = orderQty-remainder2;
                                    orderQty -= gotOrders2;

                                    if (orderQty > 0) {
                                        // not enough stock to supply

                                        profit += (orderQty * item.getCost());
                                        item.setQuantity(item.getQuantity() - orderQty);

                                        try {

                                            //Throws an exception if there is no more of that fruit that can be
                                            // stocked by the supermarket as no vendor has available stock
                                            throw new FruitNotAvailableException(item.getName());
                                        } catch (FruitNotAvailableException e) {
                                            log(i, "EXCEPTION> " + e.getMessage());
                                            print("EXCEPTION> " + e);
                                            //e.printStackTrace();
                                        }
                                    }


                                    // Logs the fulfilled orders into the simulation logBook and a corresponding
                                    // actionLog
                                    log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                            .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                                    fruits.getName(), (gotOrders2)));

                                    this.actionLogs.add(new ActionLog(i,
                                            "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));
                                }

                            } catch (InvalidItemException e) {
                                log(i, e + ": " + e.getMessage());
                            }

                        }
                    }
                }
            }

            // Sets randomEventRun to false so that it does not run again in the next cycle
            runEvent = false;

            // Prints information related to the cycle that recently ran including profits, total cycles up to that
            // point and vendor profits
            print("Commands Passed: ");
            print(getCommandsPassed());

            // Prints detailed messages if required by the commandLineArgs
            if(verbose){
                print("Cycle #%d ".formatted((i+1)));
                print(getActionsForCycle(i));
                print("End of cycle #%d ".formatted((i+1)));
            }

            print("Total cycles performed are: %d".formatted((i+1)));
            print("Total supermarket profit: $%d".formatted(profit));
            print("Total vendor profit: $%d".formatted((vendors.get(0).getMoney() + vendors.get(1).getMoney() + vendors.get(2).getMoney())));
            print(" ");


            // Stores the market's profits in the bank and resets the profits for another cycle
            bank += profit;
            profit = 0;
        }
    }

    // Utility function used to quickly add a log to the logList, takes an index which represents the cycle and the
    // message containing what to log
    public void log(int index, String log) {
        this.logs.add(new LogRecord(index, log));
    }

    // Returns a string which contains all the logged records for a particular cycle
    public String getLogsForCycle(int cycle) {
        String logCompile = "";

        // Go through the logs list and check if the dateTime is equal to the cycle asked for before appending
        // the appropriate record to the compile and returning the completeComple of logs for that cycle
        for (LogRecord lr : logs) {
            if (lr.getCycleIndex() == cycle) {
                logCompile = lr.getLogMessage() + "\n";
            }
        }
        return logCompile;
    }

    // Returns a string which contains all the actions logged for a particular cycle
    public String getActionsForCycle(int cycle) {
        StringBuilder actionCompile = new StringBuilder("\tActions performed:\n");

        // Go through the actionLogs list and check if the dateTime is equal to the cycle asked for before appending
        // the appropriate actionLog to the end of the compile and returning the completeCompile of actions for that
        // cycle

        for (ActionLog al : actionLogs) {
            //print("Action size: %d".formatted(actionLogs.size()));
            if (al.getCycleIndex() == cycle) {
                actionCompile.append("\t\t")
                        .append(al.getLogMessage())
                        .append("\n");
            }
        }
        return actionCompile.toString();
    }

    // displays the logs contained in the logsList, can take in a logFile which the logs would be written to
    public void displayLogs(String logFile) {

        if(logFile != null){
            //if logFile is not null (this means it contains a path), then the function writes the contents of the logsList
            // to the specified field and prints an message stating such

            // File writer

            try {

                //Creates a fileWritter, writes every logRecord to the file and then saves the file
                FileWriter writer = new FileWriter(logFile);
                writer.write("log information: \n");
                for (LogRecord logRecord : logs) {
                    writer.write(logRecord.getLogMessage() + "\n");
                }
                writer.close();
                print("Logs from the previous cycle has been saved to: %s".formatted(logFile));
                print(" ");
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not create log file.");
            }
        }

        //Prints all logs to the commandline
        for (LogRecord logRecord : logs) {
            print(logRecord.getLogMessage());
        }

    }

    // General print function for printing to the command line
    private void print(String message) {
        System.out.printf(message + "\n");
    }

    // Mutator for verbose field
    public void setDetailedLogs(boolean verbose) {
        this.verbose = verbose;
    }

    // Mutator for setting the logFile
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
}
