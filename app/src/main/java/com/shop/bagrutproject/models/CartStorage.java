package com.shop.bagrutproject.models;

import java.util.HashMap;
import java.util.Map;

public class CartStorage {
    private static Map<String, Cart> cartMap = new HashMap<>();

 //   public static void addCart(Cart cart) {
 //       cartMap.put(cart.getId(), cart);
 //   }

    public static Cart getCartById(String cartId) {
        return cartMap.get(cartId);
    }
}
