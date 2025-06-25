package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    //add product to the cart
    void addProduct (int userId, int productId);

    //update the quantity of a specific product in the cart
    void updateQuantity(int userId, int productId, int quantity);

    //remove all items from the users cart
    void clearCart(int userId);
}
