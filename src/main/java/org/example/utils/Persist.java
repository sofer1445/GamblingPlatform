package org.example.utils;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.example.entities.User;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private final SessionFactory sessionFactory;
    private Connection connection;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @PostConstruct
    public void init() {
        createDbConnection(Constants.DB_USERNAME, Constants.DB_PASSWORD);

    }


    private void createDbConnection(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/easyFun", username, password);
            System.out.println("Connection successful!");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Cannot create DB connection!");
        }
    }


    public User login(String mail, String password) {
        User user = null;
        if(mail == null || password == null) {
            return null;
        }
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("FROM User WHERE mail = :mail AND password = :password");
            query.setParameter("mail", mail);
            query.setParameter("password", password);
            user = (User) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean checkIfUsernameAvailable(String username) {
        boolean available = false;
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("FROM User WHERE username = :username");
            query.setParameter("username", username);
            if (query.getResultList().isEmpty()) {
                available = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public boolean checkIfMailAvailable(String mail) {
        boolean available = false;
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("FROM User WHERE mail = :mail");
            query.setParameter("mail", mail);
            if (query.getResultList().isEmpty()) {
                available = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public boolean addUser(User user) {
        boolean success = false;
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(user);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public List<User> getUsers() {
        List<User> users = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            users = session.createQuery("FROM User").getResultList();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return users;
    }

    public boolean editUser(User user) {
        boolean success = false;
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(user);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }



}


