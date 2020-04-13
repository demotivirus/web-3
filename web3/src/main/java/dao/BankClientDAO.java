package dao;

import com.sun.deploy.util.SessionState;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> bankClients = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from bank_client");

        while (resultSet.next()){
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            long money = resultSet.getLong("money");
            bankClients.add(new BankClient(id, name, password, money));
        }
        resultSet.close();
        statement.close();
        return bankClients;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("select * FROM bank_client WHERE name = ? and password = ?");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            resultSet.close();
            preparedStatement.close();
            return true;
        }
        else {
            resultSet.close();
            preparedStatement.close();
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) {
        try(PreparedStatement preparedStatement =
                connection.prepareStatement("UPDATE bank_client SET money = money + ? WHERE name = ? AND password = ?")){
            preparedStatement.setLong(1, transactValue);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, password);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        BankClient bankClient = null;
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM bank_client WHERE id = ?")){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                long money = resultSet.getLong("money");
                bankClient = new BankClient(id, name, password, money);
            }
            resultSet.close();
            //else return bankClient;
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) {
        long money = 0L;
        try (PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT money FROM bank_client WHERE name = ?")){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                money = resultSet.getLong("money");

            resultSet.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return money >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) {
        BankClient bankClient = null;
        try (PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM bank_client where name = ?")){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                long money = resultSet.getLong("money");

                bankClient = new BankClient(id, name, password, money);
            }

            resultSet.close();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException {
        try (PreparedStatement preparedStatement =
                connection.prepareStatement("INSERT INTO  bank_client (name, password, money) VALUES (?, ?, ?)")){
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPassword());
            preparedStatement.setLong(3, client.getMoney());

            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }

    public void deleteClient(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM bank_client WHERE name=?");
        preparedStatement.setString(1, "name");
        preparedStatement.execute();
        preparedStatement.close();
    }
}
