package model.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate utility class for session factory management.
 * 
 * Reads environment variables if available and sets them as system properties.
 * Priority: System properties (-D...) > Environment variables > hibernate.cfg.xml
 */
public class HibernateUtil {
    private static final SessionFactory SF;

    static {
        try {
            // Read environment variables and set as system properties if not already set
            // This allows flexibility when running from IDE (NetBeans, etc.)
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
            
            // Configuration automatically reads system properties
            // (e.g., -Dhibernate.connection.* or set via System.setProperty above)
            // which override values in hibernate.cfg.xml
            SF = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();              // Check console for the CAUSE
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSF() { return SF; }
}
