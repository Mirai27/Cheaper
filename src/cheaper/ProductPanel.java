package cheaper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductPanel extends JPanel {

    private Product product;
    private JLabel nameLabel;
    private JLabel imageLabel;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JLabel quantityLabel;
    private int quantity;

    public ProductPanel(Product product) {
        this.product = product;
        this.quantity = 0; // Начальное количество, возможно, из базы данных

        setLayout(new BorderLayout());

        // Создание компонентов
        nameLabel = new JLabel(product.getName());
        //ImageIcon icon = new ImageIcon(product.getImagePath());
        //imageLabel = new JLabel(icon);
        
        increaseButton = new JButton("+");
        decreaseButton = new JButton("-");
        quantityLabel = new JLabel("Количество: " + quantity);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(decreaseButton);
        buttonPanel.add(increaseButton);

        // Добавление слушателей
        increaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quantity++;
                updateQuantityDisplay();
                // Здесь вы могли бы также обновить количество в базе данных
            }
        });

        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quantity > 0) {
                    quantity--;
                    updateQuantityDisplay();
                    // Здесь вы могли бы также обновить количество в базе данных
                }
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

    private void updateQuantityDisplay() {
        quantityLabel.setText("Количество: " + quantity);
    }
}
