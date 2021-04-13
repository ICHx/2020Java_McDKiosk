import java.awt.*;

public class iMeal extends Item {
    iMeal(String[] tokens) {
        super(tokens);
        this.c = new Color(204, 255, 204); //apple green
    }
    
    public String getUnit() {
        return "g";
    }
    
    public String getType() {
        return "Meal";
    }
}
