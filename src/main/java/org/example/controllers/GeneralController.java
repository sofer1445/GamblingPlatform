package org.example.controllers;

import org.example.entities.Bet;
import org.example.entities.Match;
import org.example.entities.User;
import org.example.responses.BasicResponse;
import org.example.responses.LoginResponse;
import org.example.utils.Persist;
import org.example.entities.FootballClub;
import org.example.utils.Validator.EmailValidator;
import org.example.utils.Validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.example.utils.ResultsGenerator.GameResultGenerator;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.example.utils.Errors.*;

@RestController
public class GeneralController {

    @Autowired
    private Persist persist;
//    private List<User> users = persist.getUsers();



    @RequestMapping (value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login (String username, String password) {
        BasicResponse basicResponse = null;
        boolean success = false;
        Integer errorCode = null;
        if (username != null && username.length() > 0) {
            if (password != null && password.length() > 0) {
                User user = persist.login(username, password);
                if (user != null) {
                    basicResponse = new LoginResponse(true, errorCode, user.getId(), user.getSecret());
                } else {
                    errorCode = ERROR_LOGIN_WRONG_CREDS;
                }
            } else {
                errorCode = ERROR_SIGN_UP_NO_PASSWORD;
            }
        } else {
            errorCode = ERROR_SIGN_UP_NO_USERNAME;
        }
        if (errorCode != null) {
            basicResponse = new BasicResponse(success, errorCode);
        }
        return basicResponse;
    }

    @RequestMapping (value = "add-user")
    public boolean addUser (String username, String password , String mail) {
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty() && mail != null && !mail.isEmpty()) {
            if (EmailValidator.isValid(mail) && PasswordValidator.isValid(password)) {
                User existingUserByUsername = persist.getUserByUsername(username);
                User existingUserByMail = persist.getUserByMail(mail);
                if (existingUserByUsername == null && existingUserByMail == null) {
                    User user = new User(username, password, mail);
                    persist.addUser(user);
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping (value = "edit-user")
    public boolean editUser (String secretNewUser , String username, String password , String mail) {
        boolean success = false;
        if (secretNewUser != null && secretNewUser.length() > 0) {
            List<User> users = persist.getUsers();
            for (User user : users) {
                if (user.getSecret().equals(secretNewUser)) {
                    User newUser = user;
                    if (username != null && username.length() > 0) {
                        newUser.setUsername(username);
                    } else {
                        return success;
                    }
                    if (password != null && password.length() > 0) {
                        if (PasswordValidator.isValid(password)) {
                            newUser.setPassword(password);
                        } else {
                            return success;
                        }
                    }
                    if (mail != null && mail.length() > 0) {
                        if (EmailValidator.isValid(mail)) {
                            newUser.setMail(mail);
                        } else {
                            return success;
                        }
                    }

                    return persist.editUser(newUser);
                }
            }
            return success;
        }
        return success;
    }

    @RequestMapping (value = "get-clubs")
    public List<FootballClub> getClubs () {
        return persist.getClubs();
    }

    @RequestMapping (value = "generate-result")
    public String generateResult (String secretNewUser , String team1Name, String team2Name) {
        FootballClub team1 = new FootballClub();
        FootballClub team2 = new FootballClub();
        if (secretNewUser != null && secretNewUser.length() > 0) {
            List<User> users = persist.getUsers();
            List<FootballClub> footballClubs = persist.getClubs();
            for (FootballClub footballClub : footballClubs) {
                if(footballClub.getName().equals(team1Name)){
                    team1 = footballClub;
                }
                if(footballClub.getName().equals(team2Name)){
                    team2 = footballClub;
                }
            }
            for (User user : users) {
                if (user.getSecret().equals(secretNewUser)) {
                    String resultOfMatch = "";
                    GameResultGenerator gameResultGenerator = new GameResultGenerator();
                    resultOfMatch = gameResultGenerator.generateResult(team1, team2).getResult();
                    Match match = new Match(team1, team2, resultOfMatch);
                    this.persist.createMatch(match);
                    return resultOfMatch;

                }
            }
        }
        return null;
    }

    @RequestMapping (value = "add-bet-result")
    public boolean addBet (String secretNewUser ,int idMatch, String betOnResult) {
        Match newMatch = new Match();
        if (secretNewUser != null && secretNewUser.length() > 0) {
            List<User> users = persist.getUsers();
            List<Match> matches = persist.getMatches();
            for (Match match : matches) {
                if(match.getIdMatch() == idMatch){
                    newMatch = match;
                }
            }
            for (User user : users) {
                if (user.getSecret().equals(secretNewUser)) {
                    Bet bet = new Bet(user.getSecret(),newMatch, betOnResult);
                    this.persist.createBet(bet);
                    return true;

                }
            }
        }
        return false;
    }

    @RequestMapping (value = "add-bet-win")
public boolean addBetWin (String secretNewUser ,int idMatch , String betOnWin) {
        Match newMatch = new Match();
        if (secretNewUser != null && secretNewUser.length() > 0) {
            List<User> users = persist.getUsers();
            List<Match> matches = persist.getMatches();
            for (Match match : matches) {
                if(match.getIdMatch() == idMatch){
                    newMatch = match;
                }
            }
            for (User user : users) {
                if (user.getSecret().equals(secretNewUser)) {
                    FootballClub team = new FootballClub();
                    List<FootballClub> footballClubs = persist.getClubs();
                    for (FootballClub footballClub : footballClubs) {
                        if(footballClub.getName().equals(betOnWin)){
                            team = footballClub;
                        }
                    }
                    Bet bet = new Bet(user.getSecret(),newMatch, team);
                    this.persist.createBet(bet);
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping (value = "get-matches")
    public List<Match> getMatches () {
        return persist.getMatches();
    }

    @RequestMapping (value = "get-bets")
    public List<Bet> getBets () {
        return persist.getBets();
    }

    @RequestMapping (value = "check-bet")
    public boolean checkBet (String secretNewUser ,int idBet , int idMatch) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            Bet bet = persist.getBetById(idBet);
            Match match = persist.getMatchById(idMatch);

            if (user != null && bet != null && match != null) {
                if (user.getSecret().equals(bet.getSecretUser()) && match.getResult().equals(bet.getPredictedResult())) {
                    persist.updateStatus(bet, true);
                    return true;
                }
            }
        }
        return false;
    }



}

//  @GetMapping(value = "/subscribe")
//    public SseEmitter subscribe(String secret) {
//        try{
//            SseEmitter emitter = new SseEmitter((long) 10000); // 10 seconds
//            ClientSse clientSse = new ClientSse(secret, emitter);
//            clients.add(clientSse);
//            emitter.onCompletion(() -> clients.remove(clientSse));
//            emitter.onTimeout(() -> clients.remove(clientSse));
//            return emitter;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//   @PostConstruct
//    public void init() {
//        new Thread(() ->{
//            while (true) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                for(ClientSse emitter : clients ) {
//                    try {
//                        emitter.getEmitter().send(new Date());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                System.out.println("Hello from thread");
//            }
//        }).start();
//    }
