/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cheaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fantas
 */
class Basket {
    // Словарь: ключ - Продукт, значение - его Количество 
    private HashMap<Product, Integer> products = new HashMap<>();
    private double totalWeight = 0.0;
    private List<BasketListener> listeners = new ArrayList<>();

    // Добавление слушателя
    public void addBasketListener(BasketListener listener) {
        listeners.add(listener);
    }

    // Удаление слушателя
    public void removeBasketListener(BasketListener listener) {
        listeners.remove(listener);
    }

    // Уведомление всех слушателей
    private void notifyListeners() {
        for (BasketListener listener : listeners) {
            listener.basketChanged();
        }
    }
    
    public Basket(HashMap<Product, Integer> products){
        this.products = products;
        double totalW = 0.0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            
            totalW += product.getWeight() * quantity;
        }
        this.totalWeight = totalW;
    }

    public Basket() {}
    
    public HashMap<Product, Integer> getProducts(){
        return products;
    }
    
    public void setProducts(HashMap<Product, Integer> products){
        this.products = products;
        notifyListeners(); // Уведомляем слушателей об изменении корзины
    }
    
    public double getTotalWeight(){
        return totalWeight;
    }
    
    public void setTotalWeight(double totalWeight){
        this.totalWeight = totalWeight;
        notifyListeners(); // Уведомляем слушателей об изменении корзины
    }
            
    
    // Функция добавляет товар в корзину
    public void addProduct(Product newProduct) {
        // Найти продукт с таким же ID в корзине
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product existingProduct = entry.getKey();
            if (existingProduct.getId() == newProduct.getId()) {
                // Если нашли, обновляем количество
                products.put(existingProduct, entry.getValue() + 1);
                this.totalWeight += newProduct.getWeight();
                notifyListeners(); // Уведомляем слушателей об изменении корзины
                return;
            }
        }
        // Если не нашли, добавляем новый продукт
        products.put(newProduct, 1);
        this.totalWeight += newProduct.getWeight();
        notifyListeners(); // Уведомляем слушателей об изменении корзины
    }

    
    // Функция убирает товар из корзины
    public void subtractProduct(Product product){
        // Если Товаров больше, чем 1 в корзине, Уменьшаем его количество на 1
        // Если остался всего 1 Товар в корзине, Убираем товар из корзины
        // Если Товара нет в корзине ничего не делаем
        products.computeIfPresent(product, (k, v) -> v > 1 ? v - 1 : null);
        this.totalWeight -= product.getWeight();
        notifyListeners(); // Уведомляем слушателей об изменении корзины
    }
    
    public double calculateTotalPrice(){
        double totalPrice = 0.0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }
    
    public boolean isEmpty(){
        return products.isEmpty();
    }
    
     public void clear(){
        products.clear();
        totalWeight = 0.0;
        notifyListeners(); // Уведомляем слушателей об изменении корзины
    }
}
