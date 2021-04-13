import java.util.ArrayList;

//A class to contain common itemlist, using static
public class Cart {
    public static ArrayList<Item> CartList = new ArrayList<>() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Cart:\n\n");
            for (Item e : CartList) {
                sb.append(e);
            }
            sb.append("================");
            return sb.toString();
        }
    };
    
    //add item
    public static void cartAdd(Item o) {
        if (CartList.contains(o)) {
            if (Main.DEBUG == 1) {
                System.out.println("already there");
                //ignore
                
            }
            return;
        }
        CartList.add(o);
    }
    
    //remove item
    public static void cartRemove(Item o) {
        CartList.removeIf((n -> n == o));
        
    }
    
    //clear item
    public static void cartClear() {
        CartList.clear();
    }

//    //return list
//    public static ArrayList<Item> getCartList() {
//        return CartList;
//    }


}
