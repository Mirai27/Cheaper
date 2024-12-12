package cheaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import java.beans.Visibility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasketWindow extends JFrame implements BasketListener {
    private Basket basket;
    private ArrayList<Store> stores;
    private JTabbedPane tabbedPane;
    private JPanel summaryPanel;
    private String storename;
    private String storename_shown = "";

    public BasketWindow(Basket basket, ArrayList<Store> stores) throws SQLException {
        this.basket = basket;
        this.stores = stores;
        this.basket.addBasketListener(this); // Подписываемся на обновления корзины

        setTitle("Корзина");
        setSize(600, 400);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        summaryPanel = new JPanel(new GridLayout(3, 1));

        // Создаем вкладки в JTabbedPane
        tabbedPane.addTab("Пятерочка", new JPanel());
        tabbedPane.addTab("Дикси", new JPanel());
        tabbedPane.addTab("Лента", new JPanel());
        tabbedPane.addTab("Дешевле!", new JPanel());

        updateBasketDisplay();

        add(tabbedPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void updateBasketDisplay() throws SQLException {
        // Очистка перед обновлением
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JPanel panel = (JPanel) tabbedPane.getComponentAt(i);
            panel.removeAll();
        }
        summaryPanel.removeAll();

        Map<Product, Integer> productsMap = basket.getProducts();
        boolean flag = true;

        for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            
            if (flag) {
                for (Store store : stores) {
                    if (store.isProduct(product.getCategory(), product)) {
                        storename = store.getName();
                    }
                }
                flag = false;
            }
        }
        if ((storename != null) & !storename.isEmpty() ) {
            storename_shown = switch (storename) {
                case "ptrchka" -> "Пятерочка";
                case "dixy" -> "Дикси";
                case "lenta" -> "Лента";
                default -> "";
            };
        }
        else {
            storename_shown = "";
        }
        // Заполняем соответствующую вкладку
        if (!storename_shown.isEmpty() & !storename.isEmpty() & (storename != null) & (storename_shown != null) ) {
            JPanel ptrchkaPanel = (JPanel) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Пятерочка"));
            JPanel cheaperPanel = (JPanel) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Дешевле!"));
            JPanel dixyPanel = (JPanel) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Дикси"));
            JPanel lentaPanel = (JPanel) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Лента"));
            for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                // Получаем список аналогичных продуктов из БД по ID продукта
                
                if ("Пятерочка".equals(storename_shown)) {
                    ptrchkaPanel.add(new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity));
                    
                    Product similarProduct = queryDatabaseForSimilarProducts(product, storename, "dixy");
                    if (similarProduct != null)
                        dixyPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
                    else {
                        JLabel label = new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity);
                        label.setForeground(Color.RED); // Устанавливаем цвет текста в красный
                        dixyPanel.add(label);
                    } 
                    similarProduct = queryDatabaseForSimilarProducts(product, storename, "lenta");
                    if (similarProduct != null)
                        lentaPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
                    else {
                        JLabel label = new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity);
                        label.setForeground(Color.RED); // Устанавливаем цвет текста в красный
                        lentaPanel.add(label);
                    } 
                }
                if ("Дикси".equals(storename_shown)) {
                    dixyPanel.add(new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity));
                    
                    Product similarProduct = queryDatabaseForSimilarProducts(product, storename, "ptrchka");
                    if (similarProduct != null)
                        ptrchkaPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
                    else {
                        JLabel label = new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity);
                        label.setForeground(Color.RED); // Устанавливаем цвет текста в красный
                        ptrchkaPanel.add(label);
                    } 
                    similarProduct = queryDatabaseForSimilarProducts(product, storename, "lenta");
                    if (similarProduct != null)   
                        lentaPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
                    else {
                        JLabel label = new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity);
                        label.setForeground(Color.RED); // Устанавливаем цвет текста в красный
                        lentaPanel.add(label);
                    } 
                }
                if ("Лента".equals(storename_shown)) {
                    lentaPanel.add(new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity));
                    
                    Product similarProduct = queryDatabaseForSimilarProducts(product, storename, "dixy");
                    if (similarProduct != null)
                        dixyPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
                    else {
                        JLabel label = new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity);
                        label.setForeground(Color.RED); // Устанавливаем цвет текста в красный'
                        dixyPanel.add(label);
                    } 
                    similarProduct = queryDatabaseForSimilarProducts(product, storename, "ptrchka");
                    if (similarProduct != null)
                        ptrchkaPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
                    else {
                            JLabel label = new JLabel(product.getName() + " - Цена: " + product.getPrice() + ", Вес: " + product.getWeight() + ", Кол-во: " + quantity);
                            label.setForeground(Color.RED); // Устанавливаем цвет текста в красный
                            ptrchkaPanel.add(label);
                        } 
                }
                Product similarProduct = queryDatabaseForSimilarProducts(product, storename, storename);
                cheaperPanel.add(new JLabel(similarProduct.getName() + " - Цена: " + similarProduct.getPrice() + ", Вес: " + similarProduct.getWeight() + ", Кол-во: " + quantity));
            }
        }
        

       
        
        summaryPanel.add(new JLabel(String.format("Общая стоимость: %.2f", calculateTotalPrice(productsMap))));
        summaryPanel.add(new JLabel(String.format("Общий вес: %.2f", basket.getTotalWeight())));
        summaryPanel.add(new JLabel("Ваша корзина собрана в магазине: " + storename_shown));

        // Обновление интерфейса
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.getComponentAt(i).revalidate();
            tabbedPane.getComponentAt(i).repaint();
        }
        summaryPanel.revalidate();
        summaryPanel.repaint();
    }
    
    private Product findCheapestProduct(ArrayList<Product> products) {
        return products.stream().min(Comparator.comparingDouble(Product::getPrice)).orElse(null);
    }
    
    private Product queryDatabaseForSimilarProducts(Product product_c, String storenamefrom, String storenameto) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/shop_web";
        String user = "java_user";
        String password = "password";
        ArrayList<Product> products = new ArrayList<>();
        int new_id = 0;
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Если соединение успешно установлено
            System.out.println("Connected to the PostgreSQL server successfully.");

            Statement statement = connection.createStatement();
            // Список для запросов к БД для каждого магазина
            String query = ("SELECT pl.global_product_id " +
                "FROM product_links pl " +
                "JOIN " + storenamefrom +"_product_map ppm ON pl.global_product_id = ppm.global_product_id " +
                "JOIN " + storenamefrom +"_products pp ON ppm." + storenamefrom + "_product_id = pp.product_id " +
                "WHERE pp.product_id ="+ product_c.getId() + ";");
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                
                new_id = resultSet.getInt("global_product_id");
            }
            query = "SELECT pp.* FROM " + storenameto + "_products pp JOIN " + storenameto + "_product_map ppm ON pp.product_id = ppm." + storenameto + "_product_id WHERE ppm.global_product_id = " + new_id +  ";";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("product_id");
                String name = result.getString("name");
                String category = result.getString("category");
                double price = result.getDouble("price");
                double weight = result.getDouble("weight");
                int total_quantity = result.getInt("total_quantity");
                String manufacture_date = result.getString("manufacture_date");
                String expiry_date = result.getString("expiry_date");
                String image_url = result.getString("image_url");

                Product product = new Product(id, name, category, price, weight, total_quantity, manufacture_date, expiry_date, image_url);
                // Добавляем Продукт в Хешмап по его категории
                // Получаем список продуктов для данной категории
                products.add(product);
            }
        }
        return findCheapestProduct(products);
    }

 
    
    @Override
    public void basketChanged() {
        try {
            if (isVisible())
            updateBasketDisplay(); // Обновляем отображение при изменении корзины
        } catch (SQLException ex) {
            Logger.getLogger(BasketWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double calculateTotalPrice(Map<Product, Integer> products) {
        double totalPrice = 0.0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }
}
