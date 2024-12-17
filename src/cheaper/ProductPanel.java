package cheaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class ProductPanel extends JPanel {

    private Product product;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JLabel weightLabel;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JLabel priceLabel;
    private JLabel quantityLabel;
    private JLabel leftQuantityLabel;
    private int quantity;
    private Store store;
    private MainFrame mainFrame;

    public ProductPanel(Product product, Basket basket, Store store, MainFrame mainFrame) {
        this.product = product;
        // Получаем количество продуктов из корзины
        HashMap<Product, Integer> productsFromBasket = basket.getProducts();
        Integer quantityFromBasket = productsFromBasket.get(this.product);
        if (quantityFromBasket == null) {
            quantityFromBasket = 0;
        }
        this.quantity = quantityFromBasket;
        this.store = store;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());

        // Создание компонентов
        nameLabel = new JLabel(product.getName());
        weightLabel = new JLabel("Вес: " + product.getWeight() + "кг.");
        
        // Загрузка изображения и создание JLabel для него
        ImageIcon originalIcon = new ImageIcon("images/" + product.getImage_url());
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(120, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        increaseButton = new JButton("+");
        decreaseButton = new JButton("-");
        quantityLabel = new JLabel("Кол-во: " + quantity);
        int leftQuantity = product.getTotal_quantity() - quantity;
        leftQuantityLabel = new JLabel("Осталось: " + leftQuantity);
        priceLabel = new JLabel("Цена: " + product.getPrice() + " руб.");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(priceLabel);
        
        // Панель для кнопок с горизонтальной компоновкой
        JPanel buttonsContainer = new JPanel(new FlowLayout());
        buttonsContainer.setBackground(Color.WHITE);
        buttonsContainer.add(decreaseButton);
        buttonsContainer.add(increaseButton);
        
        buttonPanel.add(buttonsContainer);      

        // Добавление слушателей
        increaseButton.addActionListener((ActionEvent e) -> {
            boolean isAdd = true;
            // Если корзина не пустая
            if (!basket.isEmpty()) {
                // Получаем Продукс из корзины
                Iterator<Product> iterator = basket.getProducts().keySet().iterator();
                Product productFromBasket = iterator.next();
                // Если Этого продукта нет в текущем магазине
                if (!store.isProduct(productFromBasket.getCategory(), productFromBasket)) {
                    // Показываем диалоговое окно с возможностью очистить корзину
                    int result = JOptionPane.showOptionDialog(
                            this,
                            "В корзине обнаружены товары из другого магазина. Что вы хотите сделать?",
                            "Ошибка",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[]{"Очистить корзину", "Отмена"},
                            "Отмена"
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        basket.clear();
                        mainFrame.updateAllProductPanels(basket); // Обновляем все панели
                    } else {
                        // Отмена действия
                        isAdd = false;
                    }
                }
                if (basket.getTotalWeight() + product.getWeight() > 20){
                    isAdd = false;
                    JOptionPane.showMessageDialog(this, "Вы достигли максимального веса в корзине", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                }
            }
            if (isAdd){
                // Получаем максимальное количество товара в магазине
                int maxQuantity = this.product.getTotal_quantity();
                if (quantity < maxQuantity){
                    quantity++;
                    basket.addProduct(this.product);
                    updateQuantityDisplay();
                }
                else{
                    // Показываем сообщение об ошибке
                    JOptionPane.showMessageDialog(this, "Вы достигли максимального числа продуктов", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        decreaseButton.addActionListener((ActionEvent e) -> {
            if (quantity > 0) {
                quantity--;
                basket.subtractProduct(this.product);
                updateQuantityDisplay();
            }
        });
     
        // Панель для Названия и веса
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(nameLabel);
        infoPanel.add(weightLabel);
        
        // Панель для количества
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.Y_AXIS));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(leftQuantityLabel);
       
        // Организация компонентов на панели
        add(infoPanel, BorderLayout.NORTH); 
        add(imageLabel, BorderLayout.CENTER); // Добавляем JLabel с изображением
        add(buttonPanel, BorderLayout.SOUTH);
        add(quantityPanel, BorderLayout.EAST);
        
        setPreferredSize(new Dimension(150, 200));
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Опционально
        
        // Установка белого фона
        setBackground(Color.WHITE);
        setOpaque(true);
        
        // Установка фона для панели кнопок
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setOpaque(true);
        
        // Установка фона для информационной панели
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setOpaque(true);
        
         // Установка фона для панели количества
        quantityPanel.setBackground(Color.WHITE);
        quantityPanel.setOpaque(true);
    }

    public Product getProduct() {
        return product;
    }
    
    public String getCategory() {
        return product.getCategory();
    }
    
    public Store getStore() {
        return store;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void updateQuantityDisplay() {
        quantityLabel.setText("Кол-во: " + quantity);
        int leftQuantity = product.getTotal_quantity() - quantity;
        leftQuantityLabel.setText("Осталось: " + leftQuantity);
    }
}
