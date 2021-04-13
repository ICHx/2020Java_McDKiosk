import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Date;

public class Checkout {
    private static final User[] accounts = User.getUsers();
    private static long class_token;
    private final Messenger m;
    private JFrame UserForm;
    private JPasswordField input;
    private JComboBox<User> ucb;
    private User target;
    private int errorCnt;
    private int sum;
    
    //1 Confirm user identity
    Checkout(Messenger m) {
        class_token = (long) (Math.random() * Integer.MAX_VALUE);
        this.m = m;
        errorCnt = 0;
        createForm();
    }
    
    
    public static long getToken() {
        return class_token;
    }
    
    
    private void createForm() {
        UserForm = new JFrame("User Authentication");
        UserForm.setSize(300, 100);
        UserForm.setLocationRelativeTo(null);
        UserForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        UserForm.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        ucb = new JComboBox<>(accounts);
        input = new JPasswordField(12);
        
        
        JButton submit = new JButton("Submit");
        ActionListener l = new authHandler();
        input.addActionListener(l);
        submit.addActionListener(l);
        
        UserForm.add(ucb);
        UserForm.add(input);
        UserForm.add(submit);
        UserForm.pack();
        
        
    }
    
    
    public void authentication() throws FileNotFoundException {
        //let retry
        UserForm.setVisible(true);
        while (true) {
            assert target != null;
            if (target.checkPassword(input.getPassword())) break;
            
            //let retry
            errorCnt++;
            JOptionPane.showMessageDialog(null, "Password Failed.");
            UserForm.setVisible(true);
            if (errorCnt >= 3) {
                UserForm.setVisible(false);
                JOptionPane.showMessageDialog(null, "Nice try.", "User Auth Failure", JOptionPane.WARNING_MESSAGE);
                m.ammendStatus(class_token, false);
                System.exit(-2);
            }
            return;
            
            
        }
        
        System.out.println("Password pass");
        UserForm.dispose();
        //succeed, now proceed balance change
        Payment();
        
        //next stage
        boolean payment_result = m.getStatus();
        
        if (payment_result) {
            //invoke print receipt, thank you
            if (Main.DEBUG == 1) System.out.println("print receipt");
            Receipt();
            System.exit(0);
        } else {
            //payment failed, return to menu
            if (Main.DEBUG == 1) System.out.println("payment failed");
            System.exit(-1);
        }
    }
    
    private void Payment() throws FileNotFoundException {
        sum = 0;
        for (Item e : Cart.CartList) {
            sum += e.getPrice();
        }
        if (target.getBalance() <= sum) {
            JOptionPane.showMessageDialog(null, "Not enough balance");
            m.ammendStatus(class_token, false);
            
            return;
        }
        System.out.println("Original bal: " + target.getBalance());
        target.decBalance(sum);
        System.out.println("new bal: " + target.getBalance());
        
        //update balance in userData
        User.WriteUpdateUsers();
        
        m.ammendStatus(class_token, true);
    }
    
    private void Receipt() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Welcome to McDonna\n");
        sb.append(new Date().toString() + "\n\n");
        sb.append(Cart.CartList.toString());
        sb.append("\n\nTotal Cost:  $" + sum);
        sb.append("\nThank you, see you next time <3\n\n");
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    class authHandler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            target = (User) ucb.getSelectedItem();
            if (target == null || input.getPassword().length <= 3) {
                return;
            }
            
            UserForm.setVisible(false);
            try {
                authentication();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    
}
