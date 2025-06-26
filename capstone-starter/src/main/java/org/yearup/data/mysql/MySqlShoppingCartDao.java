package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private ProductDao productDao;


    // Constructor to initialize the data source
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    // Retrieves all shopping cart items for the given userId
    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        // SQL to join shopping_cart with products to get full product info
        String sql = "SELECT  product_id, quantity FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            while (row.next()) {
                int productId = row.getInt("product_id");
                Product product = productDao.getById(productId);


                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(row.getInt("quantity"));

                cart.add(item);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    @Override
    // Adds a product to the cart, or increments the quantity if it already exists
    public void addProduct(int userId, int productId) {
        String updateSql = "    UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id =? AND product_id = ? ";
        String insertSql = "INSERT INTO shopping_cart (user_id, product_id,quantity) " +
                "SELECT ?,?,1 FROM DUAL WHERE NOT EXISTS " +
                "(SELECT 1 FROM shopping_cart WHERE user_id =? AND product_id = ?)";

        try (Connection connection = getConnection()) {

            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setInt(1, userId);
            updateStmt.setInt(2, productId);
            int rows = updateStmt.executeUpdate();
            // If no row was updated, insert a new one
            if (rows == 0) {
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, productId);
                insertStmt.setInt(3, userId);
                insertStmt.setInt(4, productId);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // Updates the quantity of a specific product in the user's cart
    public void updateQuantity(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id =? ";

        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Deletes all items from the user's shopping cart
    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? ";

        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            {
                statement.setInt(1, userId);
                statement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
