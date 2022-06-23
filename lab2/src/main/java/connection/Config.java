package connection;
public class Config {
    public static final byte[] KEY = "1234567898765432".getBytes();
    public static final String ALGORITHM  = "AES";
    public static final String DATABASE_URL = "jdbc:mysql://127.0.0.1";
    public static final String DATABASE_PORT = "3310";
    public static final String DATABASE_SCHEMA = "committee";
    public static final String DATABASE_USER = "root";
    public static final String DATABASE_PASSWORD = "1933fearukr2";
    public static final String DATABASE_DRIVER =  "com.mysql.cj.jdbc.Driver";
    public static final String MAIL_PORT = "587";
    public static final String MAIL_USER = "chorvladislaw@gmail.com";
    public static final String MAIL_PASSWORD = "doxjjnhhiegbutql";
    private Config(){};
}
