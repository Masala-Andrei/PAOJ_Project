package Services;

import Connection.DBConnector;
import Models.Account;
import Models.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class TransactionService extends DBConnector {
    public void addTransaction(Transaction transaction) {
        try {
            String sql = "INSERT INTO transaction(senderId, receiverId, amount, description, status) values(?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(transaction.getFromAccount()));
            stmt.setInt(2, Integer.parseInt(transaction.getToAccount()));
            stmt.setInt(3, Integer.parseInt(transaction.getAmount()));
            stmt.setString(4, transaction.getDescription());
            stmt.setString(5, transaction.getStatus());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<Transaction> getTransactions(int accId) {
        Set<Transaction> transactions = new HashSet<>();
        try{
            String sql = "SELECT * FROM transaction WHERE senderId=? or receiverId=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, accId);
            stmt.setInt(2, accId);
            ResultSet rs = stmt.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getString("senderId"),
                        rs.getString("receiverId"),
                        rs.getString("amount"),
                        rs.getString("description"),
                        rs.getString("status"),
                        LocalDateTime.parse(rs.getString("date"), formatter)
                ));
            }
            return transactions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
