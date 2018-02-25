package ag.threads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Operation {

    private final Connection conn;
    private static int id = 0;

    public Operation(Connection conn) {
        this.conn = conn;
    }

    public int getID() {
        return id;
    }

    public void increaseID() {
        id++;
    }

    public void insert(int id, String name) throws SQLException {
        String sql = "INSERT INTO thread(id, name, edited, deleted) VALUES (?, ?, FALSE, FALSE);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, name);
        int updated = ps.executeUpdate();
        if (updated != 1) {
            throw new RuntimeException("Não conseguiu inserir ID " + id);
        }
//        increaseID();
    }

    public void update(int id) throws SQLException {
        String sql = "UPDATE thread SET edited = TRUE WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int updated = ps.executeUpdate();
        if (updated != 1) {
            throw new RuntimeException("Não conseguiu atualizar ID " + id);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "UPDATE thread SET deleted = TRUE WHERE id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int updated = ps.executeUpdate();
        if (updated != 1) {
            throw new RuntimeException("Não conseguiu excluir ID " + id);
        }
    }

    public static void clear(Connection conn) throws SQLException {
        String sql = "delete from thread";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate();
    }

    public static int getLastId(Connection conn) throws SQLException {
        String sql = "SELECT MAX(id) FROM thread;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet result = ps.executeQuery();
        result.next();
        return result.getInt(1);
    }
}
