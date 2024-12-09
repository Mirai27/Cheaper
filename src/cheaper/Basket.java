/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cheaper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fantas
 */
class Basket {
    // Словарь: ключ - Продукт, значение - его Количество 
    private HashMap<Product, Integer> products = new HashMap<>();
    private double totalWeight = 0.0;

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
    }
    
    public double getTotalWeight(){
        return totalWeight;
    }
    
    public void setTotalWeight(double totalWeight){
        this.totalWeight = totalWeight;
    }
            
    
    // Функция добавляет товар в корзину
    public void addProduct(Product product){
        // Если Товара нет в корзине, Устанавливаем новый товар в корзине
        // Если Товар есть в корзине, Увеличиваем его количество на 1
        products.compute(product, (k, v) -> v == null ? 1 : v + 1);
        this.totalWeight += product.getWeight();
    }
    
    // Функция убирает товар из корзины
    public void subtractProduct(Product product){
        // Если Товаров больше, чем 1 в корзине, Уменьшаем его количество на 1
        // Если остался всего 1 Товар в корзине, Убираем товар из корзины
        // Если Товара нет в корзине ничего не делаем
        products.computeIfPresent(product, (k, v) -> v > 1 ? v - 1 : null);
        this.totalWeight -= product.getWeight();
    }
    
    public boolean isEmpty(){
        return products.isEmpty();
    }
    
     public void clear(){
        products.clear();
        totalWeight = 0.0;
    }
}
