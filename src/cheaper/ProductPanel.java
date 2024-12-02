package cheaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductPanel extends JPanel {

    private Product product;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JLabel quantityLabel;
    private int quantity;
    private Basket basket;

    public ProductPanel(Product product, Basket basket) {
        this.product = product;
        this.basket = basket;
        // Получаем количество продуктов из корзины
        HashMap<Product, Integer> productsFromBasket = basket.getProducts();
        Integer quantityFromBasket = productsFromBasket.get(this.product);
        if (quantityFromBasket == null) {
            quantityFromBasket = 0;
        }
        this.quantity = quantityFromBasket;

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
            // Получаем максимальное количество товара в магазине
            int maxQuantity = this.product.getTotal_quantity();
            System.out.println(maxQuantity);
            if (quantity < maxQuantity){
                quantity++;
                this.basket.addProduct(this.product);
                updateQuantityDisplay();
            }
            else{
                // Показываем сообщение об ошибке
                JOptionPane.showMessageDialog(this, "Вы достигли максимального числа продуктов", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            }
            
        });

        decreaseButton.addActionListener((ActionEvent e) -> {
            if (quantity > 0) {
                quantity--;
                this.basket.subtractProduct(this.product);
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
    
    private void updateQuantityDisplay() {
        quantityLabel.setText("Кол-во: " + quantity);
    }
}
