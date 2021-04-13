import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Item {
    public static ArrayList<Item> itemList;
    private static File itemFile;
    private final int Type;
    private final int ID;
    private final String Name;
    private final int Price;
    private final int Capacity;
    private final int NutritionVal;
    protected Color c;//directly referenced by GUI
    
    Item(String[] tokens) {
        try {
            
            this.Type = Integer.parseInt(tokens[0]);
            this.ID = Integer.parseInt(tokens[1]);
            this.Name = tokens[2];
            this.Price = Integer.parseInt(tokens[3].substring(1));
            this.Capacity = Integer.parseInt(tokens[4]);
            this.NutritionVal = Integer.parseInt(tokens[5]);
            
        } catch (Exception e) {
            System.out.println("Error: Invalid type data");
            throw new IllegalArgumentException();
        }
    }
    
    //User(){    }
    public static void ParseItems() throws FileNotFoundException {
        itemFile = new File("data/itemData.txt");
        Scanner td = new Scanner(itemFile);
        itemList = new ArrayList<>();
        
        while (td.hasNext()) {
            String line = td.nextLine();
            if (line.charAt(0) == '#') continue;
            //ignore comments
            
            
            //start generate object
            if (Main.DEBUG == 1) System.out.println(line);
            String[] tokens = line.split(",");
            
            if (tokens.length != 6) {
                throw new IllegalArgumentException("error: User File corrupt");
            }
            
            Item temp;
            switch (tokens[0]) {
                case "1":
                    temp = new iDrink(tokens);
                    break;
                case "2":
                    temp = new iMeal(tokens);
                    
                    break;
                case "3":
                    temp = new iSnack(tokens);
                    break;
                default:
                    throw new IllegalArgumentException("Error: Invalid Type");
            }
            
            //correct object, add to list
            itemList.add(temp);
            
        }
        td.close();
    }
    
    @Override
    public String toString() {
        return
            "{" + getType() +
                ", ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Price= $" + Price +
                ", Capacity=" + Capacity +
                ", NutritionVal=" + NutritionVal +
                "KCal}\n";
    }
    
    
    public abstract String getType();
    
    public int getID() {
        return ID;
    }
    
    public String getName() {
        return Name;
    }
    
    public int getPrice() {
        return Price;
    }
    
    public int getCapacity() {
        return Capacity;
    }
    
    public int getNutritionVal() {
        return NutritionVal;
    }
    
    public abstract String getUnit();
    
    
    //function overloading
    public boolean equals(Item o) {
        return this.ID == o.ID && this.Type == o.Type;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Item)
            return this.equals((Item) o);
        else {
            throw new IllegalArgumentException("Debug: Object mismatch");
        }
    }
    

}
