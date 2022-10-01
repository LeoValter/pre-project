package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.*;

public class UserDaoHibernateImpl implements UserDao {
    private Transaction transaction;

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        executeQuery(CREATE_TABLE_SQL);
    }

    @Override
    public void dropUsersTable() {
        executeQuery(DELETE_TABLE_SQL);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            Object persistentInstance = session.get(User.class, id);
            if (persistentInstance != null) {
                session.delete(persistentInstance);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("from User ").list();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        executeQuery(CLEAN_TABLE_SQL);
    }

    private void executeQuery(String sqlQuery) {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sqlQuery).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
