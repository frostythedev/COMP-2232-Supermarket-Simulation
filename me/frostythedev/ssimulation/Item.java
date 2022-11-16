package me.frostythedev.ssimulation;

public class Item {

    private String name;
    private double cost;
    private int quantity;
    private int spoilt;
    private Type itemType;

    private int spoiltCycle;

    public Item(String name, double cost, int quantity, int spoilt, Type itemType) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.spoilt = spoilt;
        this.itemType = itemType;
        this.spoiltCycle = spoilt;
    }

    // GETTERS and MUTATORS for fields of Item class
    // isVegetable returns true of it's itemType enum starts with VEG
    // isFruit returns true if the item is not a vegetable

    public boolean isVegetable(){
        return itemType.toString().startsWith("VEG");
    }

    public boolean isFruit(){
        return !isVegetable();
    }

    public Type getItemType() {
        return itemType;
    }

    public void setItemType(Type itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSpoilt() {
        return spoilt;
    }

    public void setSpoilt(int spoilt) {
        this.spoilt = spoilt;
    }

    public int getSpoiltCycle() {
        return spoiltCycle;
    }

    public void setSpoiltCycle(int spoiltCycle) {
        this.spoiltCycle = spoiltCycle;
    }

    // Used to distinguish between items , contains default variables such as the name of the item as well as the cost
    // Contains useful functions such as byType which can return a type based on the string name and an item function
    // which returns an empty item
    // Contains an array which stores all the vegetables and a seperate array which stores all the fruits of type
    protected enum Type {
        FRUIT_MANGO("Mango", 5),
        FRUIT_AVOCADO("Avacado", 10),
        FRUIT_LIME("Lime", 3),
        FRUIT_BANANA("Banana", 5),
        FRUIT_WATERMELONS("Watermelon", 10),
        VEG_CARROTS("Carrots", 4),
        VEG_LETTUCE("Lettuce", 5),
        VEG_CUCUMBERS("Cucumber", 5),
        VEG_PARSLEY("Parsley", 3),
        VEG_ONIONS("Onions", 10);

        public static Type[] VEGETABLES = new Type[]{VEG_CARROTS, VEG_LETTUCE, VEG_CUCUMBERS, VEG_PARSLEY, VEG_ONIONS};
        public static Type[] FRUITS = new Type[]{FRUIT_AVOCADO, FRUIT_BANANA, FRUIT_MANGO, FRUIT_LIME, FRUIT_WATERMELONS};
        private String itemName;
        private int itemCost;

        Type(String itemName, int itemCost) {
            this.itemName = itemName;
            this.itemCost = itemCost;
        }

        // Gets the type by comparing the name of al values of this enum until it finds a matching name, if none is
        // found, null is returned
        public static Type getBy(String name){
            for(Type type : values()){
                if(type.getItemName().equalsIgnoreCase(name)){
                    return type;
                }
            }

            return null;
        }

        // GETTERS and MUTATORS for itemName and itemCost of Type
        public String getItemName() {
            return itemName;
        }

        public int getItemCost() {
            return itemCost;
        }

        public Item item(){
            return new Item(itemName, itemCost, 0, 0, this);
        }
    }
}
