package cheaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProductPanel extends JPanel {

    private Product product;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JLabel quantityLabel;
    private int quantity;
    private Store store;

    public ProductPanel(Product product, Basket basket, Store store) {
        this.product = product;
        // Получаем количество продуктов из корзины
        HashMap<Product, Integer> productsFromBasket = basket.getProducts();
        Integer quantityFromBasket = productsFromBasket.get(this.product);
        if (quantityFromBasket == null) {
            quantityFromBasket = 0;
        }
        this.quantity = quantityFromBasket;
        this.store = store;
        
        setLayout(new BorderLayout());

        // Создание компонентов
        nameLabel = new JLabel(product.getName());
        //ImageIcon icon = new ImageIcon(product.getImagePath());
        //imageLabel = new JLabel(icon);
        
        increaseButton = new JButton("+");
        decreaseButton = new JButton("-");
        quantityLabel = new JLabel("Кол-во: " + quantity);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(decreaseButton);
        buttonPanel.add(increaseButton);

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
                        basket.clear(); // Предположим, что у вас есть метод для очистки корзины
                    } else {
                        // Отмена действия
                        isAdd = false;
                    }
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
     
        // Организация компонентов на панели
        add(nameLabel, BorderLayout.NORTH);
        //add(imageLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(quantityLabel, BorderLayout.EAST);
        
        setPreferredSize(new Dimension(150, 200));
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Опционально
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
    
    private void updateQuantityDisplay() {
        quantityLabel.setText("Кол-во: " + quantity);
    }
}
