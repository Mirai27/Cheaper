/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cheaper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author Mirai
 */
public class Cheaper {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        String url = "jdbc:postgresql://localhost:5432/shop_web";
        String user = "java_user";
        String password = "password";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Если соединение успешно установлено
            System.out.println("Connected to the PostgreSQL server successfully.");
            System.out.println(connection.getSchema());
            ArrayList<Store> stores = new ArrayList<>();
            Statement statement = connection.createStatement();
            ArrayList<String> storeNames = new ArrayList<>();
            storeNames.add("ptrchka");
            storeNames.add("dixy");
            storeNames.add("lenta");
            
            ArrayList<String> queryList = new ArrayList<>();
            for (Iterator<String> iterator = storeNames.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                queryList.add("SELECT * FROM " + next + "_products;");
                
            }
            for (int i = 0; i < queryList.size(); i++) {
                ResultSet resultSet = statement.executeQuery(queryList.get(i));
                // Обход результатов запроса
                ArrayList<Product> products = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("product_id");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    double price = resultSet.getDouble("price");
                    double weight = resultSet.getDouble("weight");
                    int total_quantity = resultSet.getInt("total_quantity");
                    String manufacture_date = resultSet.getString("manufacture_date");
                    String expiry_date = resultSet.getString("expiry_date");

                    Product product = new Product(id, name, category, price, weight, total_quantity, manufacture_date, expiry_date);
                    products.add(product);
                }
                stores.add(new Store(storeNames.get(i), products));
            }
            ApplicationWindow ap = new ApplicationWindow();
            MainFrame mainFrame = new MainFrame(stores);
        } catch (SQLException ex) {
            Logger.getLogger(Cheaper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
