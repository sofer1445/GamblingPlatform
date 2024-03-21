package org.example.utils;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.example.entities.User;
import org.example.entities.FootballClub;
import org.example.entities.League;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;


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
        if (mail == null || password == null) {
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

    public void createClub(FootballClub club) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("FROM FootballClub");
            List<FootballClub> existingClubs = query.getResultList();
            if (existingClubs.size() >= 10) {
                // There are already 10 or more clubs, so we don't need to create new ones
                return;
            }
            session.save(club);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInitialClubs() {
        Set<String> clubNames = new HashSet<>();
        clubNames.add("Hapoel Beer Sheva");
        clubNames.add("Maccabi Tel Aviv");
        clubNames.add("Maccabi Haifa");
        clubNames.add("Beitar Jerusalem");
        clubNames.add("Ashdod Sports Club");
        clubNames.add("Hapoel Petah Tikva");
        clubNames.add("Ironi Kiryat Shmona");
        clubNames.add("Hapoel Tel Aviv");
        clubNames.add("Hapoel Jerusalem");
        clubNames.add("Maccabi Netanya");

        int i = 1;
        for (String clubName : clubNames) {
            FootballClub club = new FootballClub();
            club.setName(clubName);
            club.setTeamStrength((int) (Math.random() * 70) + 30);
            createClub(club);
            i++;
            if (i > 10) {
                break;
            }
        }
    }

    public List<FootballClub> getClubs() {
        List<FootballClub> clubs = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            clubs = session.createQuery("FROM FootballClub").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clubs;
    }

    public void saveLeague(League league) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(league);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInitialLeague() {
        List<FootballClub> clubs = getClubs();
        FootballClub[] clubArray = new FootballClub[clubs.size()];
        clubArray = clubs.toArray(clubArray);
        League league = new League("Israeli Premier League", clubArray);
        saveLeague(league);
    }


    public void createInitialData() {
        createInitialClubs();
        createInitialLeague();

    }




}


