package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {
    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getByUserId(int userId) {
        // SQL statement to retrieve profile by user_id
            String sql = "SELECT * FROM profiles WHERE user_id = ?";
        // Get a connection
            try(Connection connection = getConnection();
                // Prepare the SQL statement
                PreparedStatement statement = connection.prepareStatement(sql)){

                // Set the userId parameter in the SQL query
                statement.setInt(1,userId);
                ResultSet result = statement.executeQuery();

                // If a matching row is found, create and return a Profile object
                if (result.next()) {
                    return new Profile(
                            result.getInt("user_id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            result.getString("address"),
                            result.getString("city"),
                            result.getString("state"),
                            result.getString("zip"),
                            result.getString("phone")
                    );
                }
                // If no profile found, return null
                return null;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    @Override
    public void update(int userId, Profile profile) {

        // SQL statement to update a profile by user_id
        String sql = "UPDATE profiles SET" +
                "first_name = ?, " +
                "last_name = ?, " +
                "phone = ?, " +
                "email = ?, " +
                "address = ?, " +
                "city = ?, " +
                "state = ?, " +
                "zip = ? " +
                "WHERE user_id = ?";

        // Open connection to database
        try(Connection connection = getConnection();
            // Prepare SQL statement
            PreparedStatement statement = connection.prepareStatement(sql)){

            // Set each parameter from the Profile object
            statement.setString(1,profile.getFirstName());
            statement.setString(2,profile.getLastName());
            statement.setString(3,profile.getPhone());
            statement.setString(4,profile.getEmail());
            statement.setString(5,profile.getAddress());
            statement.setString(6,profile.getCity());
            statement.setString(7,profile.getState());
            statement.setString(8,profile.getZip());
            // Set the userId for the WHERE clause
            statement.setInt(9, userId);


            // Execute the update in the database
            statement.executeUpdate();

        } catch (SQLException e) {
            // If something goes wrong, throw a runtime exception
            throw new RuntimeException(e);
        }
    }
}


