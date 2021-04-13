import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static final int DEBUG = 0;
    //1. show splash
    //2. parse data
    //3. launch GUI
    
    private static JFrame w;
    
    public static void main(String[] args) throws InterruptedException, IOException, IllegalAccessException {
        
        //another theme, looks much better than the Ugly default.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
        
        //check for files
        try {
            int check = 0;
            File check1 = new File("data/User.txt");
            File check2 = new File("data/User.txt");
            
            if (check1.exists()) check++;
            if (check2.exists()) check++;
            
            if (check != 2) {
                throw new IOException();
            }
            
            
        } catch (IOException e) {
            String msg = "Error: File missing";
            System.out.println(msg);
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-5);
        }
        
        
        //THREAD for data Parse: anonymous runnable in lambda expression
        Thread parseData = new Thread(() -> {
            try {
                User.ParseUsers();
                Item.ParseItems();
                
                if (DEBUG == 0) Thread.sleep(3000);
            } catch (FileNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
            
        });
        parseData.start();
        
        
        //THREAD for splash: normal anonymous runnable
        Thread splash = new Thread() {
            public void run() {
                JWindow window = new JWindow();
                ImageIcon splash = new ImageIcon("src/img/splash.png");
                
                window.getContentPane().add(
                    new JLabel("", splash
                        , SwingConstants.CENTER)
                );
                window.setBounds(0, 0, 400, 400);
                window.setLocationRelativeTo(null);
                window.setAlwaysOnTop(true);
                window.setVisible(true);
                
                try {
                    parseData.join();
                    window.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        };
        splash.start();
        
        //Thread: IMPORTANT
        parseData.join(); //if this line is missing, the following would run before parse end
        ActionListener oh = new OrderHandler();
        
        //Main UI
        Thread shoppingList = new Thread(new ShoppingList());
        Thread mainUI = new Thread(() -> {
            w = new JFrame("Welcome!");
            w.setMinimumSize(new Dimension(1000, 800));
            w.setLocationRelativeTo(null);
            w.setLayout(new BorderLayout());
            w.setDefaultCloseOperation(3);
            
            JPanel grid = new JPanel();
//            grid.setBorder(new TitledBorder("Food&Beverages")); //too ugly, disabled
            w.add(grid, BorderLayout.CENTER);
            grid.setSize(1000, 720);
            grid.setMinimumSize(new Dimension(800, 800));
            grid.setLayout(new GridLayout(-1, 2, 5, 5));
//            grid.setBackground(Color.lightGray);
            
            for (Item i : Item.itemList) {
                ItemFrame itemI = new ItemFrame(i);
                grid.add(itemI);
                
            }
            
            //place on right
            JPanel rightPane = new JPanel();
            rightPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            rightPane.setSize(30, -1);
            rightPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            JButton order = new JButton("Place Order");
            order.setFont(new Font("Sans", Font.BOLD, 16));
            w.add(order, BorderLayout.EAST);
            w.pack();
            w.setVisible(true);
            System.out.println("Finish");
            
            
            //click order
            order.addActionListener(oh);
            
            
            //Main UI
            
            
            shoppingList.start();
        });
        
        mainUI.start();


//        //cart test
//        Cart.cartAdd(Item.itemList.get(0)); //add test: ok
//        Cart.cartAdd(Item.itemList.get(0)); //duplicate test: ok
//        Cart.cartRemove(Item.itemList.get(0)); //remove test: ok
    
    
    }
    
    static void payload() throws FileNotFoundException {
        int result = JOptionPane.showConfirmDialog(w, "Proceed?", "Begin Order", 1);
        boolean payment_result = false;
        
        if (result == JOptionPane.YES_OPTION) {
            //try payment
            Messenger m = new Messenger();
            
            new Checkout(m).authentication();
            System.out.println("END");
        } else {
            JOptionPane.showMessageDialog(null, "NO!");
        }
    }
    
    static class OrderHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            
            //freeze the pane
            w.setVisible(false);
            w.setEnabled(false);
            
            //enable checkout process
            try {
                payload();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            w.setVisible(true);
            w.setEnabled(true);
            
            
        }
    }
    
    
}

//this stub class is for carrying message
class Messenger {
    private boolean paymentResult;
    private boolean gotMsg;
    
    Messenger() {
        
        paymentResult = false;
    }
    
    public boolean gotMsg() {
        return gotMsg;
    }
    
    public void ammendStatus(long token, boolean result) {
        //only allows checkout class to edit
        if (token == Checkout.getToken()) {
            this.paymentResult = result;
            this.gotMsg = true;
        }
    }
    
    public boolean getStatus() {
        return paymentResult;
    }
}

