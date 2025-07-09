package Services;
import Connection.*;

import java.sql.Connection;

import static Connection.DBConnector.connect;

public class Debug {
    protected static boolean debug = false;
    protected static Connection conn = connect();
}
