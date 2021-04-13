import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    public static ArrayList<User> UserList;
    private static File userData;
    private final int ID;
    private final String name;
    private int balance;
    private String password;
    
    User(int ID, String name, int balance) {
        //ID is generated, so that must be unique.
        //the ID in file is for reference only.
        this.ID = UserList.size() + 1;
        this.name = name;
        this.balance = balance;
    }
    
    User(String[] tokens) {
        try {
            this.ID = UserList.size() + 1;
            this.name = tokens[1];
            this.balance = Integer.parseInt(tokens[2].substring(1)); //dollar sign
            this.password = tokens[3];
        } catch (Exception e) {
            System.out.println("Error: Invalid user data");
            throw new IllegalArgumentException();
        }
    }
    
    //User(){    }
    public static void ParseUsers() throws FileNotFoundException {
        userData = new File("data/User.txt");
        Scanner ud = new Scanner(userData);
        UserList = new ArrayList<>();
        
        while (ud.hasNext()) {
            String line = ud.nextLine();
            if (line.charAt(0) == '#') continue;
            //ignore comments
            
            
            //start generate object
            if (Main.DEBUG == 1) System.out.println(line);
            
            String[] tokens = line.split(",");
            if (tokens.length != 4) {
                throw new IllegalArgumentException("error: User File corrupt");
            }
            
            User temp = new User(tokens);
            //correct object, add to list
            UserList.add(temp);
            
        }
        
        ud.close();
    }
    
    public static void WriteUpdateUsers() throws FileNotFoundException {
        //! I can actually overwrite original data if wished.
        
        PrintWriter out = new PrintWriter("data/newUser.txt");
        out.println("#ID Name Balance Password");
        for (User u : UserList) {
            out.println(u.toStringFull());
        }
        
        out.println();
        out.close();
        
    }
    
    public static User[] getUsers() {
        return UserList.toArray(new User[UserList.size()]);
    }
    
    public int getID() {
        return ID;
    }
    
    public int getBalance() {
        return balance;
    }
    
    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    public void decBalance(int dec) {
        this.balance -= dec;
    }
    
    public void incBalance(int inc) {
        this.balance += inc;
    }
    
    public boolean checkPassword(char[] o) {
        int i = 0;
        char[] thisArray = this.password.toCharArray();
        if (thisArray.length != o.length) {
            return false;
        }
        
        for (char c : thisArray) {
            if (c != o[i++]) return false;
        }
        
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof User)
            return this.equals((User) o);
        else {
            throw new IllegalArgumentException("Debug: Object mismatch");
        }
    }
    
    
    public boolean equals(User o) {
        return this.ID == o.ID;
    }
    
    @Override
    public String toString() {
        return String.format("%s", this.name);
    }
    
    public String toStringFull() {
        return String.format("%d,%s,$%d,%s", this.ID, this.name, this.balance, this.password);
    }
}
