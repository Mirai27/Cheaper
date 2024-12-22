/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cheaper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Mirai
 */
public class MainFrame extends javax.swing.JFrame {
    private ArrayList<ProductPanel> productPanels = new ArrayList<>();
    private BasketWindow basketWindow;
    public MainFrame() {
    }

    public MainFrame(ArrayList<Store> stores, Basket basket) {
        initComponents();
        
        setTitle("Cheaper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Пример добавления обработчика события на кнопку корзины
            jButton1.addActionListener(e -> {
                if (basketWindow != null) {
                    basketWindow.dispose();
                }
            try {
                basketWindow = new BasketWindow(basket, stores, this);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            });



        JTabbedPane tabbedPane = new JTabbedPane();
        // Создание панелей с продуктами
        JPanel storePanel0 = createProductPanel(stores.get(0), basket);
        JPanel storePanel1 = createProductPanel(stores.get(1), basket);
        JPanel storePanel2 = createProductPanel(stores.get(2), basket);
        
        ArrayList<JPanel> storePanels = new ArrayList<>();
        storePanels.add(storePanel0);
        storePanels.add(storePanel1);
        storePanels.add(storePanel2);
        
        
        JPanel storePanelOriginal0 = createProductPanel(stores.get(0), basket);
        JPanel storePanelOriginal1 = createProductPanel(stores.get(1), basket);
        JPanel storePanelOriginal2 = createProductPanel(stores.get(2), basket);
        
        ArrayList<JPanel> storePanelsOriginal = new ArrayList<>();
        storePanelsOriginal.add(storePanelOriginal0);
        storePanelsOriginal.add(storePanelOriginal1);
        storePanelsOriginal.add(storePanelOriginal2);
        
        JScrollPane scrollPane0 = new JScrollPane(storePanel0);
        JScrollPane scrollPane1 = new JScrollPane(storePanel1);
        JScrollPane scrollPane2 = new JScrollPane(storePanel2);
        
        ArrayList<JScrollPane> scrollPanes = new ArrayList<>();
        scrollPanes.add(scrollPane0);
        scrollPanes.add(scrollPane1);
        scrollPanes.add(scrollPane2);
        
        scrollPane0.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane2.getVerticalScrollBar().setUnitIncrement(16);
        
        tabbedPane.addTab("Пятерочка", scrollPane0);
        tabbedPane.addTab("Дикси", scrollPane1);
        tabbedPane.addTab("Лента", scrollPane2);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGridLayout(storePanel0); // Обновляем раскладку панели
                updateGridLayout(storePanel1); // Обновляем раскладку панели
                updateGridLayout(storePanel2); // Обновляем раскладку панели
                updateGridLayout(jPanel3);
            }
        });
        
        jPanel2.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; // Заполняет всю строку
        constraints.gridy = 0; // Заполняет всю колонку
        constraints.gridwidth = GridBagConstraints.REMAINDER; // Заполняет всю оставшуюся ширину
        constraints.gridheight = GridBagConstraints.REMAINDER; // Заполняет всю оставшуюся высоту
        constraints.fill = GridBagConstraints.BOTH; // Заполняет всю область
        constraints.weightx = 1.0; // Растягивает компонент по горизонтали
        constraints.weighty = 1.0; // Растягивает компонент по вертикали
        jPanel2.add(tabbedPane, constraints);
        
        
        jPanel3.setLayout(new GridBagLayout());
        Set<String> categories = new TreeSet<>();
        
        for (Store item : stores) {
            categories.addAll(item.getProducts().keySet());
        }
        
        
        // Добавление ActionListener к каждой радиокнопке
        ActionListener actionListener = (ActionEvent e) -> {
            JRadioButton selectedButton = (JRadioButton) e.getSource();
            if ("Все".equals(selectedButton.getText())) {
                for (int i = 0; i < storePanels.size(); i++) {
                    //productPanel.setVisible(true);
                    copyPanelContents(storePanelsOriginal.get(i), storePanels.get(i), basket);
                    storePanels.get(i).revalidate();     // Обновляем раскладку
                    storePanels.get(i).repaint();        // Перерисовываем интерфейс
                    scrollPanes.get(i).revalidate();
                    scrollPanes.get(i).repaint();
                }
                tabbedPane.revalidate();
                tabbedPane.repaint();
            } else {
                for (int i = 0; i < storePanels.size(); i++) {
                    copyPanelContents(storePanelsOriginal.get(i), storePanels.get(i), basket);
                    for (Component component1 : storePanels.get(i).getComponents()) {
                        if (component1 instanceof ProductPanel) {
                            // Убедитесь, что вы используете правильный класс
                            ProductPanel productPanel = (ProductPanel) component1;
                            //productPanel.setVisible(true);
                            if ((!productPanel.getCategory().equals(selectedButton.getText()))) {
                                //productPanel.setVisible(false);
                                storePanels.get(i).remove(productPanel);
                            }
                        }
                        storePanels.get(i).revalidate();     // Обновляем раскладку
                        storePanels.get(i).repaint();        // Перерисовываем интерфейс
                        scrollPanes.get(i).revalidate();
                        scrollPanes.get(i).repaint();
                    }
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                }
            }
        };
        ButtonGroup categoriesGroup = new ButtonGroup();
        JRadioButton allButton = new JRadioButton("Все");
        allButton.setSelected(true);
        allButton.addActionListener(actionListener);
        categoriesGroup.add(allButton);
        jPanel3.add(allButton);
        for (String category : categories) {
            JRadioButton button = new JRadioButton(category);
            button.addActionListener(actionListener);
            categoriesGroup.add(button);
            jPanel3.add(button);
        }

         // Добавляем DocumentListener для отслеживания изменений текста
        jTextField2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextFieldChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextFieldChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextFieldChange();
            }

            private void handleTextFieldChange() {
                if (jTextField2.getText().trim().isEmpty()) {
                            String searchText = jTextField2.getText().toLowerCase().trim();
        for (int i = 0; i < storePanels.size(); i++) {
            copyPanelContents(storePanelsOriginal.get(i), storePanels.get(i), basket);
            if (!searchText.isEmpty()) {
                for (Component component : storePanels.get(i).getComponents()) {
                    if (component instanceof ProductPanel) {
                        ProductPanel productPanel = (ProductPanel) component;
                        String productName = productPanel.getProduct().getName().toLowerCase();
                        if (!productName.contains(searchText)) {
                            storePanels.get(i).remove(productPanel);
                        }
                    }
                }
            }
            storePanels.get(i).revalidate();     
            storePanels.get(i).repaint();        
            scrollPanes.get(i).revalidate();
            scrollPanes.get(i).repaint();
        }
        tabbedPane.revalidate();
        tabbedPane.repaint();
                }
            }
        });
        // Добавляем ActionListener для поля поиска
        jTextField2.addActionListener(e -> {
        String searchText = jTextField2.getText().toLowerCase();
        for (int i = 0; i < storePanels.size(); i++) {
            copyPanelContents(storePanelsOriginal.get(i), storePanels.get(i), basket);
            for (Component component : storePanels.get(i).getComponents()) {
                if (component instanceof ProductPanel) {
                    ProductPanel productPanel = (ProductPanel) component;
                    String productName = productPanel.getProduct().getName().toLowerCase();
                    if (!productName.contains(searchText)) {
                        storePanels.get(i).remove(productPanel);
                    }
                }
            }
            storePanels.get(i).revalidate();     
            storePanels.get(i).repaint();        
            scrollPanes.get(i).revalidate();
            scrollPanes.get(i).repaint();
        }
        tabbedPane.revalidate();
        tabbedPane.repaint();
    });
        // Добавляем ActionListener для кнопки поиска
        jButton2.addActionListener(e -> {
        String searchText = jTextField2.getText().toLowerCase();
        for (int i = 0; i < storePanels.size(); i++) {
            copyPanelContents(storePanelsOriginal.get(i), storePanels.get(i), basket);
            for (Component component : storePanels.get(i).getComponents()) {
                if (component instanceof ProductPanel) {
                    ProductPanel productPanel = (ProductPanel) component;
                    String productName = productPanel.getProduct().getName().toLowerCase();
                    if (!productName.contains(searchText)) {
                        storePanels.get(i).remove(productPanel);
                    }
                }
            }
            storePanels.get(i).revalidate();     
            storePanels.get(i).repaint();        
            scrollPanes.get(i).revalidate();
            scrollPanes.get(i).repaint();
        }
        tabbedPane.revalidate();
        tabbedPane.repaint();
    });
        
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setVisible(true);
        
    }
    
    private void copyPanelContents(JPanel source, JPanel target, Basket basket) {
        target.removeAll(); // Очистка целевой панели

        for (Component comp : source.getComponents()) {
            if (comp instanceof ProductPanel) {
                ProductPanel originalPanel = (ProductPanel) comp;
                // Предполагая, что существует метод для клонирования или копирования ProductPanel
                ProductPanel newPanel = new ProductPanel(originalPanel.getProduct(), basket, originalPanel.getStore(), this);
                target.add(newPanel);
                productPanels.add(newPanel);
            }
        }

        target.revalidate(); // Обновление компоновки
        target.repaint();    // Перерисовка интерфейса
    }

    
     private JPanel createProductPanel(Store store, Basket basket) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 3)); // Вертикальная раскладка
        // Добавляем продукты на панель согласно HashMap
        HashMap<String, ArrayList<Product>> products = store.getProducts();
        for (ArrayList<Product> items : products.values()) {
            for (Product product : items) {
                ProductPanel productPanel = new ProductPanel(product, basket, store, this);
                panel.add(productPanel);
                productPanels.add(productPanel);
            }
        }
        
        return panel;
    }
    
    public void updateAllProductPanels(Basket basket) {
        for (ProductPanel panel : productPanels) {
            int trueQuantity = basket.getProducts().getOrDefault(panel.getProduct(), 0);
            
            if(panel.getQuantity() != trueQuantity)
            {
                panel.setQuantity(trueQuantity);
                panel.updateQuantityDisplay();
            }
        }
    }
     
    private void updateGridLayout(JPanel panel) {
        // Получите ширину экрана
        int width = getWidth();

        // Ширина одной ячейки
        int cellHorizontal = (width / 250);

        // Установите раскладку с динамическим количеством столбцов
        panel.setLayout(new GridLayout(0, cellHorizontal));

        // Обновите компоновку 
        panel.revalidate();
        panel.repaint();
    }
//    //индусский код
//    public void addProductsToAllPanels(ArrayList<ArrayList> stores) {
//        addProductsToPanel(stores.get(0), jStorePanel1);
//        addProductsToPanel(stores.get(1), jStorePanel2);
//        addProductsToPanel(stores.get(2), jStorePanel3);
//    }
   
    
//    private void addProductsToPanel(ArrayList<Product> products, JPanel panel) {
//        panel.removeAll(); // Очистка панели перед добавлением новых элементов
//        panel.setLayout(new GridLayout(6, 6)); // Пример вертикальной раскладки
//        
//        for (Product product : products) {
//            // Создание компонента для каждого продукта
//            JLabel productLabel = new JLabel(product.getName());
//            //ImageIcon icon = new ImageIcon(product.getImagePath());
//            //JLabel imageLabel = new JLabel(icon);
//
//            JPanel productPanel = new JPanel();
//            //productPanel.add(imageLabel);
//            productPanel.add(productLabel);
//
//            panel.add(productPanel);
//        }
//
//        panel.revalidate();
//        panel.repaint();
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cheaper");
        setBackground(new java.awt.Color(252, 200, 200));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(220, 242, 220));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = -7;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanel1.add(jTextField2, gridBagConstraints);

        jButton1.setText("Корзина");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanel1.add(jButton1, gridBagConstraints);

        jButton2.setText("Поиск");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        jPanel1.add(jButton2, gridBagConstraints);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(230, 242, 230));
        jPanel2.setMinimumSize(new java.awt.Dimension(128, 50));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
