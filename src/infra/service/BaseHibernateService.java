package infra.service;

import model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;

import java.util.List;
import java.util.function.Function;

/**
 * Base class for Hibernate service implementations.
 * Provides common session and transaction management patterns.
 */
public abstract class BaseHibernateService {

    /**
     * Executes a read-only operation within a session.
     * The session is automatically closed after execution.
     */
    protected <T> T executeInSession(Function<Session, T> operation) {
        Session s = HibernateUtil.getSF().openSession();
        try {
            return operation.apply(s);
        } finally {
            s.close();
        }
    }

    /**
     * Executes a write operation within a transaction.
     * The transaction is automatically committed on success or rolled back on error.
     * The session is automatically closed after execution.
     */
    protected <T> T executeInTransaction(Function<Session, T> operation) {
        Session s = HibernateUtil.getSF().openSession();
        Transaction tx = s.beginTransaction();
        try {
            T result = operation.apply(s);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Database operation error: " + e.getMessage(), e);
        } finally {
            s.close();
        }
    }

    /**
     * Executes a write operation that doesn't return a value.
     */
    protected void executeInTransactionVoid(Function<Session, Void> operation) {
        executeInTransaction(operation);
    }

    /**
     * Creates a query from HQL string.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> createQuery(Session session, String hql, Class<T> resultType) {
        Query q = session.createQuery(hql);
        return q.list();
    }

    /**
     * Creates a query from HQL string with a single parameter.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> createQuery(Session session, String hql, String paramName, Object paramValue, Class<T> resultType) {
        org.hibernate.Query q = session.createQuery(hql);
        if (paramValue instanceof Integer) {
            q.setInteger(paramName, (Integer) paramValue);
        } else if (paramValue instanceof String) {
            q.setString(paramName, (String) paramValue);
        } else {
            q.setParameter(paramName, paramValue);
        }
        return q.list();
    }
    
    /**
     * Creates a query with multiple string parameters.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> createQueryWithParams(Session session, String hql, Class<T> resultType, Object... params) {
        org.hibernate.Query q = session.createQuery(hql);
        for (int i = 0; i < params.length; i += 2) {
            String paramName = (String) params[i];
            Object paramValue = params[i + 1];
            if (paramValue instanceof Integer) {
                q.setInteger(paramName, (Integer) paramValue);
            } else if (paramValue instanceof String) {
                q.setString(paramName, (String) paramValue);
            } else {
                q.setParameter(paramName, paramValue);
            }
        }
        return q.list();
    }
}
