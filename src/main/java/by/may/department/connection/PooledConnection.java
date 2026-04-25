package by.may.department.connection;


import lombok.Getter;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class PooledConnection implements Connection {

    @Getter
    private final Connection realConnection;
    private final ConnectionPool pool;

    private volatile boolean isClosed = false;

    public PooledConnection(Connection realConnection, ConnectionPool pool) {
        this.realConnection = realConnection;
        this.pool = pool;
    }

    private void ensureOpen() {
        if (isClosed) {
            throw new IllegalStateException("Connection is closed (returned to pool)");
        }
    }

    @Override
    public void close() {
        if (!isClosed) {
            isClosed = true;
            pool.returnConnection(this);
        }
    }

    public void reset() throws SQLException {
        if (!realConnection.isClosed()) {
            if (!realConnection.getAutoCommit()) {
                realConnection.rollback();
            }
            realConnection.clearWarnings();
        }
        isClosed = false;
    }

    @Override
    public Statement createStatement() throws SQLException { ensureOpen(); return realConnection.createStatement(); }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException { ensureOpen(); return realConnection.prepareStatement(sql); }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException { ensureOpen(); return realConnection.prepareCall(sql); }

    @Override
    public String nativeSQL(String sql) throws SQLException { ensureOpen(); return realConnection.nativeSQL(sql); }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException { ensureOpen(); realConnection.setAutoCommit(autoCommit); }

    @Override
    public boolean getAutoCommit() throws SQLException { ensureOpen(); return realConnection.getAutoCommit(); }

    @Override
    public void commit() throws SQLException { ensureOpen(); realConnection.commit(); }

    @Override
    public void rollback() throws SQLException { ensureOpen(); realConnection.rollback(); }

    @Override
    public boolean isClosed() throws SQLException { return isClosed || realConnection.isClosed(); }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException { ensureOpen(); return realConnection.getMetaData(); }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException { ensureOpen(); realConnection.setReadOnly(readOnly); }

    @Override
    public boolean isReadOnly() throws SQLException { ensureOpen(); return realConnection.isReadOnly(); }

    @Override
    public void setCatalog(String catalog) throws SQLException { ensureOpen(); realConnection.setCatalog(catalog); }

    @Override
    public String getCatalog() throws SQLException { ensureOpen(); return realConnection.getCatalog(); }

    @Override
    public void setTransactionIsolation(int level) throws SQLException { ensureOpen(); realConnection.setTransactionIsolation(level); }

    @Override
    public int getTransactionIsolation() throws SQLException { ensureOpen(); return realConnection.getTransactionIsolation(); }

    @Override
    public SQLWarning getWarnings() throws SQLException { ensureOpen(); return realConnection.getWarnings(); }

    @Override
    public void clearWarnings() throws SQLException { ensureOpen(); realConnection.clearWarnings(); }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException { ensureOpen(); return realConnection.createStatement(resultSetType, resultSetConcurrency); }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { ensureOpen(); return realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency); }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { ensureOpen(); return realConnection.prepareCall(sql, resultSetType, resultSetConcurrency); }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException { ensureOpen(); return realConnection.getTypeMap(); }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException { ensureOpen(); realConnection.setTypeMap(map); }

    @Override
    public void setHoldability(int holdability) throws SQLException { ensureOpen(); realConnection.setHoldability(holdability); }

    @Override
    public int getHoldability() throws SQLException { ensureOpen(); return realConnection.getHoldability(); }

    @Override
    public Savepoint setSavepoint() throws SQLException { ensureOpen(); return realConnection.setSavepoint(); }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException { ensureOpen(); return realConnection.setSavepoint(name); }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException { ensureOpen(); realConnection.rollback(savepoint); }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException { ensureOpen(); realConnection.releaseSavepoint(savepoint); }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { ensureOpen(); return realConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability); }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { ensureOpen(); return realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability); }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { ensureOpen(); return realConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability); }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException { ensureOpen(); return realConnection.prepareStatement(sql, autoGeneratedKeys); }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException { ensureOpen(); return realConnection.prepareStatement(sql, columnIndexes); }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException { ensureOpen(); return realConnection.prepareStatement(sql, columnNames); }

    @Override
    public Clob createClob() throws SQLException { ensureOpen(); return realConnection.createClob(); }

    @Override
    public Blob createBlob() throws SQLException { ensureOpen(); return realConnection.createBlob(); }

    @Override
    public NClob createNClob() throws SQLException { ensureOpen(); return realConnection.createNClob(); }

    @Override
    public SQLXML createSQLXML() throws SQLException { ensureOpen(); return realConnection.createSQLXML(); }

    @Override
    public boolean isValid(int timeout) throws SQLException { return !isClosed && realConnection.isValid(timeout); }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        if (isClosed) {
            throw new SQLClientInfoException("Connection is closed", null);
        }
        realConnection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        if (isClosed) {
            throw new SQLClientInfoException("Connection is closed", null);
        }
        realConnection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException { ensureOpen(); return realConnection.getClientInfo(name); }

    @Override
    public Properties getClientInfo() throws SQLException { ensureOpen(); return realConnection.getClientInfo(); }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException { ensureOpen(); return realConnection.createArrayOf(typeName, elements); }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException { ensureOpen(); return realConnection.createStruct(typeName, attributes); }

    @Override
    public void setSchema(String schema) throws SQLException { ensureOpen(); realConnection.setSchema(schema); }

    @Override
    public String getSchema() throws SQLException { ensureOpen(); return realConnection.getSchema(); }

    @Override
    public void abort(Executor executor) throws SQLException { ensureOpen(); realConnection.abort(executor); }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException { ensureOpen(); realConnection.setNetworkTimeout(executor, milliseconds); }

    @Override
    public int getNetworkTimeout() throws SQLException { ensureOpen(); return realConnection.getNetworkTimeout(); }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException { ensureOpen(); return realConnection.unwrap(iface); }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException { ensureOpen(); return realConnection.isWrapperFor(iface); }

}
