import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ItemFrame extends JPanel {
    JCheckBox buy;
    Item containedItem;
    
    
    ItemFrame(Item e) {
        this.setBackground(e.c);
        this.containedItem = e;
        this.setAlignmentY(CENTER_ALIGNMENT);
        
        JPanel panel1 = new JPanel();
        panel1.setBackground(e.c);
        panel1.setMinimumSize(new Dimension(400, 200));
        this.add(panel1, LEFT_ALIGNMENT);
        panel1.setAlignmentY(CENTER_ALIGNMENT);
        
        
        //icon
        String filename = "src/img/" + e.getID() + ".jpg";
        ImageIcon icon = new ImageIcon(filename);
        
        
        JLabel itemIcon = new JLabel();
        itemIcon.setIcon(icon);
        itemIcon.setMaximumSize(new Dimension(10, 10)); //does not work
//        itemIcon.setMinimumSize(new Dimension(10,10));
        
        
        panel1.add(itemIcon);
        
        //labels
        JPanel p2 = new JPanel();
        p2.setBackground(e.c);
//        p2.setLayout(new GridLayout(-1,1,15,2));
        p2.setLayout(new GridLayout(-1, 1, 15, 2));
        
        //Name
        JLabel itemName = new JLabel(e.getName());
        itemName.setForeground(Color.BLUE);
        itemName.setFont(new Font("Serif", Font.BOLD, 16));
        p2.add(itemName, TOP_ALIGNMENT);
        
        //price
        JLabel price = new JLabel(String.format("$%2d", e.getPrice()));
        p2.add(price);
        
        //capacity
        JLabel cap = new JLabel(e.getCapacity() + e.getUnit());
        p2.add(cap);
        
        
        //nutrition
        JLabel NRV = new JLabel((e.getNutritionVal()) + "KCal");
        p2.add(NRV);
        
        //CheckBox buy;
        buy = new JCheckBox("");
        buy.setBackground(e.c);
        p2.add(buy, BOTTOM_ALIGNMENT);
        panel1.add(p2);
        
        buy.addItemListener(new cartHandler());
        
        
    }
    
    @Override
    public Font getFont() {
        return new Font("Comic Sans", Font.PLAIN, 12);
    }
    
    private class cartHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (buy.isSelected()) {
                Cart.cartAdd(containedItem);
                ShoppingList.update();
                
                if (Main.DEBUG == 1) {
                    System.out.println(Cart.CartList);
                    
                    new Thread(() -> {
                        JOptionPane.showMessageDialog(null, "Selected item " + containedItem.toString());
                    }).start();
                    //if not contained in thread, the checkbox cannot be checked.
                }
            } else {
                Cart.cartRemove(containedItem);
                ShoppingList.update();
                
                if (Main.DEBUG == 1) {
                    
                    System.out.println(Cart.CartList);
                    
                    new Thread(() -> {
                        JOptionPane.showMessageDialog(null, "Unselected item " + containedItem.toString());
                    }).start();
                    //if not contained in thread, the checkbox cannot be checked.
                }
            }
            
        }
        
        
    }
}
    

    


