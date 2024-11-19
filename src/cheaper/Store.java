/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cheaper;

import java.util.ArrayList;

/**
 *
 * @author Mirai
 */
public class Store {
    private String name;
    private ArrayList<Product> products = new ArrayList<>();

    public Store(String name, ArrayList<Product> products) {
        this.name = name;
        this.products = products;
    }

    public Store() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
    
    
}
