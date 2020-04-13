package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() {
        List<BankClient> bankClients = null;
        try{
            //bankClients = new ArrayList<>();
            //bankClients = getBankClientDAO().getAllBankClient();
            return bankClients = new ArrayList<>(getBankClientDAO().getAllBankClient());
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return bankClients;
    }

    public boolean deleteClient(String name) {
        try {
            if(getBankClientDAO().getClientByName(name) != null)
                getBankClientDAO().deleteClient(name);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addClient(BankClient client) {
        BankClientDAO dao = getBankClientDAO();
        try {
            BankClient bankClient = getClientByName(client.getName());
            if(bankClient == null && client.getPassword() != null && !client.getPassword().isEmpty()) {
                dao.addClient(client);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        String senderName = sender.getName();
        String senderPass = sender.getPassword();

        BankClient receiver = getBankClientDAO().getClientByName(name);
        try {
            if(getBankClientDAO().isClientHasSum(senderName, value) &&
                getBankClientDAO().validateClient(senderName, senderPass)) {
                //sender -money
                getBankClientDAO().updateClientsMoney(senderName, senderPass, -value);

                //next people add money
                getBankClientDAO().updateClientsMoney(receiver.getName(),
                        receiver.getPassword(), value);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=root");
                    //append("&useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false");

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
