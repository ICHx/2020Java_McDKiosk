import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ShoppingList implements Runnable {
    
    //Shopping List
    //common componenets, static
    private static JFrame c;
    private static JPanel lowerPanel;
    private static JTextArea textPane;
    
    private static JScrollPane scrl;
    
    private static JLabel l_cost;
    private static int i_cost;
    private static JLabel l_nrv;
    private static int i_nrv;
    
    ShoppingList() {
        
        
        c = new JFrame();
        c.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //make windows unclosable
        c.setLayout(new BoxLayout(c.getContentPane(), BoxLayout.Y_AXIS));
        c.setAlwaysOnTop(true);
        
        {//set location
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x, y;
            x = (int) dim.getWidth() - 300;
            y = (int) dim.getHeight() / 3;
            c.setLocation(x, y);
            c.setPreferredSize(new Dimension(200, y));
            c.setMinimumSize(new Dimension(200, y));
        }
        
        textPane = new JTextArea();
        textPane.setEditable(false);
        
        l_nrv = new JLabel();
        l_cost = new JLabel();
        
        scrl = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    public static void update() { //pass
        i_cost = 0;
        i_nrv = 0;
        
        //clear text pane
        textPane.setText("");
        for (Item e : Cart.CartList) {
            i_cost += e.getPrice();
            i_nrv += e.getNutritionVal();
            textPane.append(e.getName() + "\n");
        }
        l_nrv.setText("Total Calorie: " + i_nrv + "KCal");
        l_cost.setText("Cost Total: $" + i_cost);
        
        c.pack();
        lowerPanel.repaint();
    }
    
    public static void destroy() { //work!
        c.dispose();
    }
    
    public void run() {
        
        textPane.setBorder(new TitledBorder("Added Items"));
        textPane.setOpaque(false);
        
        
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        lowerPanel.add(l_nrv);
        lowerPanel.add(l_cost);
        
        c.add(scrl);
        c.add(lowerPanel);
        c.pack();
        
        update();
        
        c.setVisible(true);
    }
}
