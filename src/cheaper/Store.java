/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cheaper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mirai
 */
public class Store {
    private String name;
    private HashMap<String, ArrayList<Product>> products = new HashMap<>();

    public Store(String name, HashMap<String, ArrayList<Product>> products) {
        this.name = name;
        this.products = products;
    }

    public Store() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, ArrayList<Product>> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, ArrayList<Product>> products) {
        this.products = products;
    }
    
    public boolean isProduct(String category, Product product){
        ArrayList<Product> productsList = products.get(category);
        if (productsList == null) {
            // Если категории нет
            return false;
        }
        return productsList.contains(product);
    }
}
