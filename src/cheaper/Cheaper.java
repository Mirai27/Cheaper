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
import java.util.HashMap;

/**
 *
 * @author Mirai
 */
public class Cheaper {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/shop_web";
        String user = "java_user";
        String password = "password";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Если соединение успешно установлено
            System.out.println("Connected to the PostgreSQL server successfully.");
            // Список для магазинов (Store: Имя(String), Продукты(HashMap))
            ArrayList<Store> stores = new ArrayList<>();
            Statement statement = connection.createStatement();
            // Список для имён магазинов
            ArrayList<String> storeNames = new ArrayList<>();
            storeNames.add("ptrchka");
            storeNames.add("dixy");
            storeNames.add("lenta");
            // Список для запросов к БД для каждого магазина
            ArrayList<String> queryList = new ArrayList<>();
            for (String next : storeNames) {
                queryList.add("SELECT * FROM " + next + "_products;");
            }
            for (int i = 0; i < queryList.size(); i++) {
                ResultSet resultSet = statement.executeQuery(queryList.get(i));
                // Обход результатов запроса
                // Создаём HashMap
                HashMap<String, ArrayList<Product>> products = new HashMap<>();
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
                    // Добавляем Продукт в Хешмап по его категории
                    // Получаем список продуктов для данной категории
                    ArrayList<Product> productsList = products.get(category);
                    // Проверяем, есть ли уже список для этой категории
                    if (productsList == null) {
                        // Если категории нет, создаём новый список
                        productsList = new ArrayList<>();
                        // Устанавливаем новый список в карте
                        products.put(category, productsList);
                    }
                    // Добавляем продукт в список
                    productsList.add(product);
                }
                stores.add(new Store(storeNames.get(i), products));
            }
            Basket basket = new Basket();
            ApplicationWindow ap = new ApplicationWindow(); 
            MainFrame mainFrame = new MainFrame(stores, basket);    
        } catch (SQLException ex) {
            Logger.getLogger(Cheaper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
