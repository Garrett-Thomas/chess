package dataaccess;

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

    public static ResultSet executeQuery(String statement, Object... params) throws ServiceException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = parseStatement(conn, statement, params);
            ps.executeQuery();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs;
            }


            // This means that no data was fetched from the server in essence
            return null;
        } catch (SQLException | DataAccessException e) {
            throw new ServiceException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
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
