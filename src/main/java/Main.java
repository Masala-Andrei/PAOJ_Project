import Models.Menu;
import Connection.*;


public class Main {
    public static void main(String[] args) {

//        DBConnector con = new DBConnector();
//        System.out.println(con.connect());
        Menu menu = new Menu();
        menu.start();
    }
}