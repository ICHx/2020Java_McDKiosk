import java.awt.*;

public class iDrink extends Item {
    iDrink(String[] tokens) {
        super(tokens);
        this.c = new Color(204, 255, 255); //light cyan
    }
    
    @Override
    public String getUnit() {
        return "mL";
    }
    
    public String getType() {
        return "Drink";
    }
}
