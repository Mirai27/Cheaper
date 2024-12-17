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
import javax.swing.border.Border;

public class BasketWindow extends JFrame implements BasketListener {
    private Basket basket;
    private ArrayList<Store> stores;
    private JTabbedPane tabbedPane;
    private JPanel summaryPanel;
    private String storename;
    private String storename_shown = "";

    // Определяем ширину для различных частей строк
    private static final int NAME_WIDTH = 60;
    private static final int PRICE_WIDTH = 15;
    private static final int WEIGHT_WIDTH = 15;
    private static final int QUANTITY_WIDTH = 15;
    
    // Устанавливаем шрифт Roboto для всех JLabel
    private static final Font MONO_FONT = new Font("Monospaced", Font.PLAIN, 12);

    public BasketWindow(Basket basket, ArrayList<Store> stores) throws SQLException {
        this.basket = basket;
        this.stores = stores;
        this.basket.addBasketListener(this); // Подписываемся на обновления корзины

        setTitle("Корзина");
        setSize(900, 600);
        setLayout(new BorderLayout());
        
        // Создать панель, которая будет содержать вкладки и кнопку очистки
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        summaryPanel = new JPanel(new GridLayout(3, 1));
        
        // Создаем панели с прокруткой для каждой вкладки
        JScrollPane ptrchkaScroll = new JScrollPane(createScrollablePanel());
        JScrollPane dixyScroll = new JScrollPane(createScrollablePanel());
        JScrollPane lentaScroll = new JScrollPane(createScrollablePanel());
        JScrollPane cheaperScroll = new JScrollPane(createScrollablePanel());
        ptrchkaScroll.getVerticalScrollBar().setUnitIncrement(16);
        dixyScroll.getVerticalScrollBar().setUnitIncrement(16);
        lentaScroll.getVerticalScrollBar().setUnitIncrement(16);
        cheaperScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Создаем вкладки в JTabbedPane
        tabbedPane.addTab("Пятерочка", ptrchkaScroll);
        tabbedPane.addTab("Дикси", dixyScroll);
        tabbedPane.addTab("Лента", lentaScroll);
        tabbedPane.addTab("Дешевле!", cheaperScroll);
        
        // Панель заголовков с форматированием
        JPanel headerPanel = createHeaderPanel();
        
        // Кнопка очистки корзины
        JButton clearButton = new JButton("Очистить корзину");
        clearButton.addActionListener((ActionEvent e) -> {
            basket.clear(); // Очистка корзины
            try {
                updateBasketDisplay(); // Обновляем интерфейс после очистки
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Добавляем вкладки и заголовки в tabPanel
        tabPanel.add(tabbedPane, BorderLayout.CENTER);
        tabPanel.add(headerPanel, BorderLayout.SOUTH);
        tabPanel.add(clearButton, BorderLayout.NORTH);

        summaryPanel = new JPanel(new GridLayout(3, 1));

        updateBasketDisplay();

        add(tabPanel, BorderLayout.CENTER); // Располагаем панель с вкладками и кнопкой в центре
        add(summaryPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    
    // Создаем заголовки для столбцов
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Форматируем строку заголовков
        String headerFormat = "%-" + NAME_WIDTH + "s %" + (PRICE_WIDTH + 4) + "s %" + (WEIGHT_WIDTH + 3) + "s %" + (QUANTITY_WIDTH + 3) + "s";
        String formattedHeader = String.format(headerFormat, "Название", "Цена", "Вес", "Количество");
        
        JLabel label = new JLabel(formattedHeader);
        label.setFont(MONO_FONT);

        headerPanel.add(label, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createScrollablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Используем BoxLayout для вертикального размещения
        return panel;
    }

    private void updateBasketDisplay() throws SQLException {
        // Очистка перед обновлением
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(i);
            JPanel panel = (JPanel) scrollPane.getViewport().getView();
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
        if ((storename != null) && !storename.isEmpty()) {
            storename_shown = switch (storename) {
                case "ptrchka" -> "Пятерочка";
                case "dixy" -> "Дикси";
                case "lenta" -> "Лента";
                default -> "";
            };
        } else {
            storename_shown = "";
        }
        
        // Создаём корзины для всех магазинов
        Basket ptrchkaBasket = new Basket();
        Basket dixyBasket = new Basket();
        Basket lentaBasket = new Basket();
        Basket cheaperBasket = new Basket();

        // Заполняем соответствующую вкладку
        if (!storename_shown.isEmpty() && !storename.isEmpty() && (storename != null) && (storename_shown != null)) {
            JPanel ptrchkaPanel = (JPanel) ((JScrollPane) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Пятерочка"))).getViewport().getView();
            JPanel dixyPanel = (JPanel) ((JScrollPane) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Дикси"))).getViewport().getView();
            JPanel lentaPanel = (JPanel) ((JScrollPane) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Лента"))).getViewport().getView();
            JPanel cheaperPanel = (JPanel) ((JScrollPane) tabbedPane.getComponentAt(tabbedPane.indexOfTab("Дешевле!"))).getViewport().getView();

            // Создаем объект для отступов
            Border labelBorder = BorderFactory.createEmptyBorder(5, 0, 5, 0);

            // Форматирование строки продукта
            String productFormat = "%-" + NAME_WIDTH + "s %" + PRICE_WIDTH + ".2f руб %" + WEIGHT_WIDTH + ".2f кг %" + QUANTITY_WIDTH + "d шт";   

            for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                String formattedProduct = String.format(productFormat, product.getName(), product.getPrice(), product.getWeight(), quantity);
                if ("Пятерочка".equals(storename_shown)) {
                    ptrchkaPanel.add(new JLabel(formattedProduct) {{
                        setBorder(labelBorder);
                        setFont(MONO_FONT);
                    }});

                    Product similarProduct = queryDatabaseForSimilarProducts(product, storename, "dixy");
                    if (similarProduct != null) {
                        for (int i = 0; i < quantity; i++){
                            dixyBasket.addProduct(similarProduct);
                        }
                    } else {
                        dixyPanel.add(new JLabel(formattedProduct) {{
                            setForeground(Color.RED);
                            setBorder(labelBorder);
                            setFont(MONO_FONT);
                        }});
                    }

                    similarProduct = queryDatabaseForSimilarProducts(product, storename, "lenta");
                    if (similarProduct != null) {
                        for (int i = 0; i < quantity; i++){
                            lentaBasket.addProduct(similarProduct);
                        }
                    } else {
                        lentaPanel.add(new JLabel(formattedProduct) {{
                            setForeground(Color.RED);
                            setBorder(labelBorder);
                            setFont(MONO_FONT);
                        }});
                    }
                }

                if ("Дикси".equals(storename_shown)) {
                    dixyPanel.add(new JLabel(formattedProduct) {{
                        setBorder(labelBorder);
                        setFont(MONO_FONT);
                    }});

                    Product similarProduct = queryDatabaseForSimilarProducts(product, storename, "ptrchka");
                    if (similarProduct != null) {
                        for (int i = 0; i < quantity; i++){
                            ptrchkaBasket.addProduct(similarProduct);
                        }
                    } else {
                        ptrchkaPanel.add(new JLabel(formattedProduct) {{
                            setForeground(Color.RED);
                            setBorder(labelBorder);
                            setFont(MONO_FONT);
                        }});
                    }

                    similarProduct = queryDatabaseForSimilarProducts(product, storename, "lenta");
                    if (similarProduct != null) {
                        for (int i = 0; i < quantity; i++){
                            lentaBasket.addProduct(similarProduct);
                        }
                    } else {
                        lentaPanel.add(new JLabel(formattedProduct) {{
                            setForeground(Color.RED);
                            setBorder(labelBorder);
                            setFont(MONO_FONT);
                        }});
                    }
                }

                if ("Лента".equals(storename_shown)) {
                    lentaPanel.add(new JLabel(formattedProduct) {{
                        setBorder(labelBorder);
                        setFont(MONO_FONT);
                    }});

                    Product similarProduct = queryDatabaseForSimilarProducts(product, storename, "dixy");
                    if (similarProduct != null) {
                        for (int i = 0; i < quantity; i++){
                            dixyBasket.addProduct(similarProduct);
                        }
                    } else {
                        dixyPanel.add(new JLabel(formattedProduct) {{
                            setForeground(Color.RED);
                            setBorder(labelBorder);
                            setFont(MONO_FONT);
                        }});
                    }

                    similarProduct = queryDatabaseForSimilarProducts(product, storename, "ptrchka");
                    if (similarProduct != null) {
                        for (int i = 0; i < quantity; i++){
                            ptrchkaBasket.addProduct(similarProduct);
                        }
                    } else {
                        ptrchkaPanel.add(new JLabel(formattedProduct) {{
                            setForeground(Color.RED);
                            setBorder(labelBorder);
                            setFont(MONO_FONT);
                        }});
                    }
                }

                Product similarProduct = queryDatabaseForSimilarProducts(product, storename, storename);
                for (int i = 0; i < quantity; i++){
                    cheaperBasket.addProduct(similarProduct);
                }
            }
            
            HashMap<Product, Integer> ptrchkaProductsMap = ptrchkaBasket.getProducts();
            HashMap<Product, Integer> dixyProductsMap = dixyBasket.getProducts();
            HashMap<Product, Integer> lentaProductsMap = lentaBasket.getProducts();
            HashMap<Product, Integer> cheaperProductsMap = cheaperBasket.getProducts();
            // Форматирование товаров и добавление в панели
            for (Map.Entry<Product, Integer> entry : ptrchkaProductsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                String formattedProduct = String.format(productFormat, product.getName(), product.getPrice(), product.getWeight(), quantity);
                ptrchkaPanel.add(new JLabel(formattedProduct) {{
                    setBorder(labelBorder);
                    setFont(MONO_FONT);
                }});
            }

            for (Map.Entry<Product, Integer> entry : dixyProductsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                String formattedProduct = String.format(productFormat, product.getName(), product.getPrice(), product.getWeight(), quantity);
                dixyPanel.add(new JLabel(formattedProduct) {{
                    setBorder(labelBorder);
                    setFont(MONO_FONT);
                }});
            }

            for (Map.Entry<Product, Integer> entry : lentaProductsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                String formattedProduct = String.format(productFormat, product.getName(), product.getPrice(), product.getWeight(), quantity);
                lentaPanel.add(new JLabel(formattedProduct) {{
                    setBorder(labelBorder);
                    setFont(MONO_FONT);
                }});
            }

            for (Map.Entry<Product, Integer> entry : cheaperProductsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                String formattedProduct = String.format(productFormat, product.getName(), product.getPrice(), product.getWeight(), quantity);
                cheaperPanel.add(new JLabel(formattedProduct) {{
                    setBorder(labelBorder);
                    setFont(MONO_FONT);
                }});
            }
        }

        // Создание форматов
        String summaryFormat = "%-" + NAME_WIDTH + "s";
        
        // Создаем строки с выравниванием
        String s1 = formatWithDots("Общая стоимость:", basket.calculateTotalPrice());
        String s2 = formatWithDots("Общий вес:", basket.getTotalWeight());
        String summary = String.format(summaryFormat, "Ваша корзина собрана в магазине: " + storename_shown);
        
        if ("Пятерочка".equals(storename_shown)) {
            s1 += "\n" + formatWithDots("Стоимость в Дикси:", dixyBasket.calculateTotalPrice());
            s1 += "\n" + formatWithDots("Стоимость в Ленте:", lentaBasket.calculateTotalPrice());
            s1 += "\n" + formatWithDots("Мин. стоимость в Пятёрочке:", cheaperBasket.calculateTotalPrice());
            s2 += "\n" + formatWithDots("Вес в Дикси:", dixyBasket.getTotalWeight());
            s2 += "\n" + formatWithDots("Вес в Ленте:", lentaBasket.getTotalWeight());
            s2 += "\n" + formatWithDots("Мин. вес в Пятёрочке:", cheaperBasket.getTotalWeight());
        }

        if ("Дикси".equals(storename_shown)) {
            s1 += "\n" + formatWithDots("Стоимость в Пятёрочке:", ptrchkaBasket.calculateTotalPrice());
            s1 += "\n" + formatWithDots("Стоимость в Ленте:", lentaBasket.calculateTotalPrice());
            s1 += "\n" + formatWithDots("Мин. стоимость в Дикси:", cheaperBasket.calculateTotalPrice());
            s2 += "\n" + formatWithDots("Вес в Пятёрочке:", ptrchkaBasket.getTotalWeight());
            s2 += "\n" + formatWithDots("Вес в Ленте:", lentaBasket.getTotalWeight());
            s2 += "\n" + formatWithDots("Мин. вес в Дикси:", cheaperBasket.getTotalWeight());
        }

        if ("Лента".equals(storename_shown)) {
            s1 += "\n" + formatWithDots("Стоимость в Пятёрочке:", ptrchkaBasket.calculateTotalPrice());
            s1 += "\n" + formatWithDots("Стоимость в Дикси:", dixyBasket.calculateTotalPrice());
            s1 += "\n" + formatWithDots("Мин. стоимость в Ленте:", cheaperBasket.calculateTotalPrice());
            s2 += "\n" + formatWithDots("Вес в Пятёрочке:", ptrchkaBasket.getTotalWeight());
            s2 += "\n" + formatWithDots("Вес в Дикси:", dixyBasket.getTotalWeight());
            s2 += "\n" + formatWithDots("Мин. вес в Ленте:", cheaperBasket.getTotalWeight());
        }
        
        // Добавление в панель
        summaryPanel.add(new JLabel("<html><pre>" + s1 + "</pre></html>"));
        summaryPanel.add(new JLabel("<html><pre>" + s2 + "</pre></html>"));
        summaryPanel.add(new JLabel("<html><pre>" + summary + "</pre></html>"));
        
        // Обновление интерфейса
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(i);
            JPanel panel = (JPanel) scrollPane.getViewport().getView();
            panel.revalidate();
            panel.repaint();
        }

        summaryPanel.revalidate();
        summaryPanel.repaint();
    }
    
    // Функция для заполнения точками до определенной длины
    private String formatWithDots(String name, double value) {
            int dotsLength = NAME_WIDTH - name.length();
            String dots = ".".repeat(dotsLength > 0 ? dotsLength : 0);
            return String.format("%s%s %.2f", name, dots, value);  
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
}
