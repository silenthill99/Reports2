package fr.silenthill99.reports.mysq;

public class DbCredentials {
    private final String host;
    private final String user;
    private final String password;
    private final String dbName;
    private final int port;

    public DbCredentials(String host, String user, String password, String dbName, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
    }

    public String toURI() {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(dbName);
        return url.toString();
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
