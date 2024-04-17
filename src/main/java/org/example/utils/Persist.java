package org.example.utils;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.example.entities.User;
import org.example.entities.FootballClub;
import org.example.entities.League;
import org.example.entities.Match;
import org.example.entities.Bet;
import org.example.utils.ResultsGenerator.GameProgression;

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

    public boolean deleteUser(User user) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.delete(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean currencyUpdate(User user, int amount) { // amount can be negative
        try {
            Session session = sessionFactory.getCurrentSession();
            user.setCoins(user.getCoins() + amount);
            session.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        // צריך להוציא את כל הקבוצות לfinalים
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

    public void updateClub(FootballClub club) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(club);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateClubs(Match game) {
        try {
            FootballClub homeTeam = game.getHomeTeam();
            FootballClub awayTeam = game.getAwayTeam();
            String result = game.getResult();
            String[] resultArray = result.split("-");
            int homeGoals = Integer.parseInt(resultArray[0]);
            int awayGoals = Integer.parseInt(resultArray[1]);
            homeTeam.setGoalsScored(homeTeam.getGoalsScored() + homeGoals);
            homeTeam.setGoalsConceded(homeTeam.getGoalsConceded() + awayGoals);
            awayTeam.setGoalsScored(awayTeam.getGoalsScored() + awayGoals);
            awayTeam.setGoalsConceded(awayTeam.getGoalsConceded() + homeGoals);
            homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed() + 1);
            awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed() + 1);
            if (homeGoals > awayGoals) {
                homeTeam.setWins(homeTeam.getWins() + 1);
                awayTeam.setLosses(awayTeam.getLosses() + 1);
                homeTeam.setPoints(homeTeam.getPoints() + 3);
            } else if (homeGoals < awayGoals) {
                awayTeam.setWins(awayTeam.getWins() + 1);
                homeTeam.setLosses(homeTeam.getLosses() + 1);
                awayTeam.setPoints(awayTeam.getPoints() + 3);
            } else {
                homeTeam.setDraws(homeTeam.getDraws() + 1);
                awayTeam.setDraws(awayTeam.getDraws() + 1);
                homeTeam.setPoints(homeTeam.getPoints() + 1);
                awayTeam.setPoints(awayTeam.getPoints() + 1);
            }
            updateClub(homeTeam);
            updateClub(awayTeam);

        } catch (Exception e) {
            e.printStackTrace();
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
            League existingLeague = session.get(League.class, league.getName());
            if (existingLeague == null) {
                session.save(league);
            }
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

    public void createMatch(Match match) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(match);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createGameProgression(GameProgression gameProgression) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(gameProgression);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createBet(Bet bet) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(bet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Match> getMatches() {
        List<Match> matches = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            matches = session.createQuery("FROM Match").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matches;
    }

    public List<Bet> getBets() {
        List<Bet> bets = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            bets = session.createQuery("FROM Bet").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bets;
    }

    public void updateDraw(Bet bet, boolean draw) {
        try {
            Session session = sessionFactory.getCurrentSession();
            bet.setDraw(draw);
            session.update(bet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(Bet bet, boolean status) {
        try {
            Session session = sessionFactory.getCurrentSession();
            bet.setStatus(status);
            session.update(bet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserBySecret(String secret) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM User WHERE secret = :secret", User.class)
                    .setParameter("secret", secret)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Bet getBetById(int idBet) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.get(Bet.class, idBet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Match getMatchById(int idMatch) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.get(Match.class, idMatch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GameProgression getGameProgressionById(int idGameProgression) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.get(GameProgression.class, idGameProgression);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByMail(String mail) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM User WHERE mail = :mail", User.class)
                    .setParameter("mail", mail)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FootballClub getClubByName(String team1Name) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM FootballClub WHERE name = :name", FootballClub.class)
                    .setParameter("name", team1Name)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getAllMatches() {
        try {
            Session session = sessionFactory.getCurrentSession();
            List<Match> matches = session.createQuery("FROM Match").getResultList();
            for (Match match : matches) {
                System.out.println(match);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllBets() {
        try {
            Session session = sessionFactory.getCurrentSession();
            List<Bet> bets = session.createQuery("FROM Bet").getResultList();
            for (Bet bet : bets) {
                System.out.println(bet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


