package model.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory SF;

    static {
        try {
            String username = System.getenv("HIBERNATE_USERNAME");
            String password = System.getenv("HIBERNATE_PASSWORD");
            String url = System.getenv("HIBERNATE_URL");
            
            if (username != null && System.getProperty("hibernate.connection.username") == null) {
                System.setProperty("hibernate.connection.username", username);
            }
            if (password != null && System.getProperty("hibernate.connection.password") == null) {
                System.setProperty("hibernate.connection.password", password);
            }
            if (url != null && System.getProperty("hibernate.connection.url") == null) {
                System.setProperty("hibernate.connection.url", url);
            }
            
            SF = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSF() { return SF; }
}
