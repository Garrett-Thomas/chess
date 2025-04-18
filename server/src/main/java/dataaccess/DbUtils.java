package dataaccess;

import dao.SQLAuthDAO;
import dao.SQLGameDAO;
import dao.SQLUserDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DbUtils {


    public static PreparedStatement parseStatement(Connection conn, String statement, Object... params) throws SQLException {
        var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS);
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            if (param instanceof String p) {
                ps.setString(i + 1, p);
            } else if (param instanceof Integer p) {
                ps.setInt(i + 1, p);
            } else if (param == null) {
                ps.setNull(i + 1, NULL);
            }
        }

        return ps;
    }

    public static boolean checkPassword(String userPassword, String hash) {
        return BCrypt.checkpw(userPassword, hash);
    }

    public static String hashPassword(String password) {
        return (BCrypt.hashpw(password, BCrypt.gensalt()));
    }


    public static ResultSet executeQuery(String statement, Object... params) throws ServiceException, SQLException {

        Connection conn;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ServiceException(500, String.format("unable to query database: %s, %s", statement, e.getMessage()));
        }

        var ps = parseStatement(conn, statement, params);
        return ps.executeQuery();
    }
    public static void clearDB(){
        SQLAuthDAO.getInstance().clear();
        SQLGameDAO.getInstance().clear();
        SQLUserDAO.getInstance().clear();
    }
    public static int executeUpdate(String statement, Object... params) throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {

            var ps = parseStatement(conn, statement, params);

            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException | DataAccessException e) {
            throw new ServiceException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }


}
