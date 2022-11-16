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
    private static final int DEFAULT_TOTAL_CYCLES = 50;
    private int totalCycles;

    private int profit, bank;

    private boolean verbose;
    private String logFile;

    private List<Vendor> vendors;
    private HashMap<String, Item> items;
    private int spoiltGoods;

    private List<LogRecord> logs;
    private List<ActionLog> actionLogs;

    private RandomEventType randomEventType;
    private boolean runEvent;

    public Supermarket() {
        this(DEFAULT_TOTAL_CYCLES);
    }

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

    public void init() {

        this.vendors = new ArrayList<>();
        this.items = new HashMap<>();
        this.logs = new ArrayList<>();
        this.actionLogs = new ArrayList<>();

        this.spoiltGoods = 0;
        this.profit = 1000000;
        this.bank = 1000;

        for (Item.Type type : Item.Type.values()) {
            Item item = type.item();
            item.setSpoilt(Utilities.generateRndNumber(5, 11));
            items.put(item.getName().toLowerCase(), item);
        }

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

    public void customerPurchase(int cycle) {

        List<String> keys = new ArrayList<>(items.keySet());
        String randomKey = keys.get(Utilities.generateRndNumber(keys.size()));

        Item item = items.get(randomKey);
        int rndAmt = Utilities.generateRndNumber(1, 21);

        if (rndAmt > item.getQuantity()) {
            rndAmt = item.getQuantity();

            //VENDOR RESTOCK
        }

        double cost = rndAmt * item.getCost();

        this.actionLogs.add(new ActionLog(cycle, "Total customer purchases: %s (%d)".formatted((item.isFruit() ?
                "Fruit" : "Vegetable"), rndAmt)));


        profit += cost;
        item.setQuantity(item.getQuantity() - rndAmt);
    }

    public void chooseRandomEvent() {
        randomEventType = RandomEventType.values()[Utilities.generateRndNumber(RandomEventType.values().length)];
        runEvent = true;
    }

    public String getCommandsPassed() {
        return ("""
                Verbose: %s
                Iter: %s
                logFile:%s""")
                .formatted(verbose ? true : "false (default)", (totalCycles == 50 ? "50 (default)" :
                                totalCycles),
                        (logFile == null ? "N/A, default printing to commandline" : logFile));
    }

    public void run() {

        if(!logs.isEmpty()){
            log(-1, "========== NEW SIMULATION =========");
        }
        //logs.clear();

        // cycle
        for (int i = 0; i < totalCycles; i++) {
            // perform simulation actions.

            if (i == 0) {
                print("Simulation Started");
            }

            // random even may occur each day
            if (i != 1 && Utilities.generateRndNumber(1, 101) < 25) {
                // random even occurs
                chooseRandomEvent();
                log(i, randomEventType.name());
            }
            //print("DEBUG 2");

            if (runEvent) {
                switch (randomEventType) {
                    case FRIDGE_DOWN -> {
                        int fridgeCost = Utilities.generateRndNumber(0, 101);
                        profit -= fridgeCost;
                    }
                    case ELECTRICITY_OFF -> {
                        int rndHours = Utilities.generateRndNumber(1, 5);

                        for (Item item : items.values()) {
                            int instaSpoilt = item.getQuantity() * (rndHours / 100);

                            //this.actionLogs.add(new ActionLog(i, "Spoilt %s (%s): %d".formatted((item.isFruit() ? "Fruit" : "Vegetable"), item.getName(), instaSpoilt)))

                            item.setQuantity(Math.max(item.getQuantity() - instaSpoilt, 0));

                        }
                    }
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

            //print("DEBUG 3");

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


            if (i > 1 && i % 10 == 0) {
                chooseRandomEvent();
                log(i, "RANDOM EVENT>" + randomEventType.name());
            }

            if (randomEventType != null && !randomEventType.equals(RandomEventType.NO_PURCHASES) && runEvent) {
                for (int purchases = 0; purchases < 10; purchases++) {
                    customerPurchase(i);
                }
            }

            // End of the Day

            for (Vendor vendor : vendors) {
                vendor.restock();
            }

            // RESTOCK

            int noShowIndex = -1;
            if (randomEventType != null && randomEventType.equals(RandomEventType.VENDOR_NO_SHOW)) {
                noShowIndex = Utilities.generateRndNumber(0, 3);
                this.actionLogs.add(new ActionLog(i,
                        "Vendor does not deliver goods: %s".formatted(vendors.get(noShowIndex).getName())));

            }

            for (Item item : items.values()) {
                int orderQty;
                if (item.isVegetable()) {
                    orderQty = 100 - item.getQuantity();

                    //System.out.printf("Vendor %d will no shown\n", noShowIndex);

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

                        Vendor veg = vendors.get(1);

                        if (noShowIndex == 2) {

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(100);

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

                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                            veg.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));


                        } else if (noShowIndex == 1) {

                            veg = vendors.get(2);

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(100);

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

                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                            veg.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));
                        } else {

                            try {

                                profit -= (orderQty * item.getCost());
                                item.setQuantity(100);

                                int remainder = veg.sellStock(item.getName(), orderQty);

                                int gotOrders = orderQty-remainder;
                                orderQty -= gotOrders;


                                if (orderQty > 0) {

                                    log(i, ("ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total " +
                                            "Purchased: %s")
                                            .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), item.getCost(),
                                                    veg.getName(), (gotOrders)));

                                    this.actionLogs.add(new ActionLog(i,
                                            "%s: %s (%d)".formatted(veg.getName(), (item.isFruit() ? "Fruit" : 
                                                    "Vegetable"), gotOrders)));

                                    // Both veg and fruit
                                    veg = vendors.get(2);
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

                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"),
                                            item.getCost(),
                                            fruits.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));

                        } else if (noShowIndex == 2) {

                            profit -= (orderQty * item.getCost());
                            item.setQuantity(200);

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
                                // second vendor didnt show up, first didnt have enough stock
                                profit += (orderQty * item.getCost());
                                item.setQuantity(item.getQuantity() - orderQty);
                            }

                            log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                    .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"),
                                            item.getCost(),
                                            fruits.getName(), (gotOrders)));

                            this.actionLogs.add(new ActionLog(i,
                                    "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));
                        } else {

                            try {

                                profit -= (orderQty * item.getCost());
                                item.setQuantity(200);

                                int remainder = fruits.sellStock(item.getName(), orderQty);

                                int gotOrders = orderQty-remainder;
                                orderQty -= gotOrders;


                                if (orderQty > 0) {

                                    log(i, "ITEM PURCHASE> Name: %s, Type: %s, Cost: %s, Vendor: %s, Total Purchased: %s"
                                            .formatted(item.getName(), (item.isFruit() ? "Fruit" : "Vegetable"),
                                                    item.getCost(),
                                                    fruits.getName(), (gotOrders)));

                                    this.actionLogs.add(new ActionLog(i,
                                            "%s: %s (%d)".formatted(fruits.getName(), (item.isFruit() ? "Fruit" : "Vegetable"), gotOrders)));

                                    // Both veg and fruit
                                    fruits = vendors.get(2);
                                    int remainder2 = fruits.sellStock(item.getName(), orderQty);

                                    int gotOrders2 = orderQty-remainder2;
                                    orderQty -= gotOrders2;

                                    if (orderQty > 0) {
                                        // not enough stock to supply

                                        profit += (orderQty * item.getCost());
                                        item.setQuantity(item.getQuantity() - orderQty);

                                        try {
                                            throw new FruitNotAvailableException(item.getName());
                                        } catch (FruitNotAvailableException e) {
                                            log(i, "EXCEPTION> " + e.getMessage());
                                            print("EXCEPTION> " + e);
                                            //e.printStackTrace();
                                        }
                                    }

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

            runEvent = false;

            print("Commands Passed: ");
            print(getCommandsPassed());

            if(verbose){
                print("Cycle #%d ".formatted((i+1)));
                print(getActionsForCycle(i));
                print("End of cycle #%d ".formatted((i+1)));
            }

            print("Total cycles performed are: %d".formatted((i+1)));
            print("Total supermarket profit: $%d".formatted(profit));
            print("Total vendor profit: $%d".formatted((vendors.get(0).getMoney() + vendors.get(1).getMoney() + vendors.get(2).getMoney())));
            print(" ");


            bank += profit;
            profit = 0;
        }
    }

    public void log(int index, String log) {
        this.logs.add(new LogRecord(index, log));
    }

    public String getLogsForCycle(int cycle) {
        String logCompile = "";
        for (LogRecord lr : logs) {
            if (lr.getDateTime() == cycle) {
                logCompile = lr.getLogMessage() + "\n";
            }
        }
        return logCompile;
    }

    public String getActionsForCycle(int cycle) {
        StringBuilder actionCompile = new StringBuilder("\tActions performed:\n");

        for (ActionLog al : actionLogs) {
            //print("Action size: %d".formatted(actionLogs.size()));
            if (al.getDateTime() == cycle) {
                actionCompile.append("\t\t")
                        .append(al.getLogMessage())
                        .append("\n");
            }
        }
        return actionCompile.toString();
    }

    public void displayLogs(String logFile) {
        if (logFile == null || logFile.equals("")) {
            for (LogRecord logRecord : logs) {
                print(logRecord.getLogMessage());
            }
        } else {

            // File writer

            try {
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
    }

    private void print(String message) {
        System.out.printf(message + "\n");
    }

    public void setDetailedLogs(boolean verbose) {
        this.verbose = verbose;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
}
