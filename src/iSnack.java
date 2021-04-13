import java.awt.*;

public class iSnack extends Item {
    iSnack(String[] tokens) {
        super(tokens);
        this.c = new Color(255, 204, 153); //Orange
    }
    
    public String getUnit() {
        return "g";
    }
    
    public String getType() {
        return "Snack";
    }
}
