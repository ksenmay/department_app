package by.may.department.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;

    private final List<PooledConnection> freeConnections = new ArrayList<>();
    private final List<PooledConnection> usedConnections = new ArrayList<>();

    private final String url;
    private final String user;
    private final String password;

    private static ConnectionPool instance;

    private ConnectionPool() {
        try {
            Class.forName(PropertyConfig.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgresSQL JDBC Driver не найден", e);
        }

        this.url = PropertyConfig.getUrl();
        this.user = PropertyConfig.getUsername();
        this.password = PropertyConfig.getPassword();

        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            freeConnections.add(createPooledConnection());
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private PooledConnection createPooledConnection() {
        try {
            Connection realConnection = DriverManager.getConnection(url, user, password);
            return new PooledConnection(realConnection, this);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось установить соединение с бд", e);
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        if (freeConnections.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                freeConnections.add(createPooledConnection());
            } else {
                throw new SQLException("No available connections. Pool size limit reached: " + MAX_POOL_SIZE);
            }
        }
        PooledConnection pooledConnection = freeConnections.removeLast();
        usedConnections.add(pooledConnection);
        return pooledConnection;
    }

    public synchronized void returnConnection(PooledConnection pooledConnection) {
        if (usedConnections.remove(pooledConnection)) {
            try {
                pooledConnection.reset();
            } catch (SQLException e) {
                try {
                    pooledConnection.getRealConnection().close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                return;
            }
            freeConnections.add(pooledConnection);
        }
    }

    public synchronized void closeAll() throws SQLException {
        for (PooledConnection pooledConnection : freeConnections) {
            try {
                pooledConnection.getRealConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        freeConnections.clear();

        for (PooledConnection pooledConnection : usedConnections) {
            try {
                pooledConnection.getRealConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        usedConnections.clear();
    }
}
