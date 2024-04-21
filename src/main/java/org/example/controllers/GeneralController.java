package org.example.controllers;

import org.example.entities.Bet;
import org.example.entities.Match;
import org.example.entities.User;
import org.example.responses.BasicResponse;
import org.example.responses.LoginResponse;
import org.example.utils.Persist;
import org.example.entities.FootballClub;
import org.example.utils.ResultsGenerator.GameProgression;
import org.example.utils.Validator.EmailValidator;
import org.example.utils.Validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.example.utils.ResultsGenerator.GameResultGenerator;
import org.example.utils.ResultsGenerator.BettingSystem;

import java.util.*;

import static org.example.utils.Errors.*;

@RestController
public class GeneralController {

    @Autowired
    private Persist persist;
    @Autowired
    private BettingSystem bettingSystem;
//    private List<User> users = persist.getUsers();


    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String mail, String password) {
        BasicResponse basicResponse = null;
        boolean success = false;
        Integer errorCode = null;
        if (mail != null && mail.length() > 0) {
            if (password != null && password.length() > 0) {
                User user = persist.login(mail, password);
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

    @RequestMapping(value = "sign-up")
    public boolean addUser(String username, String password, String mail) {
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

    @RequestMapping(value = "edit-user")
    public boolean editUser(String secretNewUser, String username, String password, String mail) {
        boolean success = false;
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            if (user != null) {
                User newUser = user;
                if (username != null && !username.isEmpty()) {
                    newUser.setUsername(username);
                } else {
                    return success;
                }
                if (password != null && !password.isEmpty()) {
                    if (PasswordValidator.isValid(password)) {
                        newUser.setPassword(password);
                    } else {
                        return success;
                    }
                }
                if (mail != null && !mail.isEmpty()) {
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

    @RequestMapping(value = "delete-user")
    public boolean deleteUser(String secretNewUser) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            if (user != null) {
                return persist.deleteUser(user);
            }
        }
        return false;
    }

    @RequestMapping(value = "currency-Update")
    public boolean currencyUpdate(String secretNewUser, int coins) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            if (user != null) {
                user.setCoins(coins);
                return persist.currencyUpdate(user, coins);
            }
        }
        return false;
    }

    @RequestMapping(value = "get-clubs")
    public List<FootballClub> getClubs() {
        return persist.getClubs();
    }

    @RequestMapping(value = "generate-result")
    public GameProgression generateResult(String secretNewUser, String team1Name, String team2Name) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            FootballClub team1 = persist.getClubByName(team1Name);
            FootballClub team2 = persist.getClubByName(team2Name);

            if (user != null && team1 != null && team2 != null) {
                GameResultGenerator gameResultGenerator = new GameResultGenerator();
                GameProgression resultOfMatch = gameResultGenerator.generateResult(team1, team2);
                Match match = new Match(team1, team2, resultOfMatch.getResult().keySet().iterator().next());
                this.persist.createMatch(match);
                return resultOfMatch;
            }
        }
        return null;
    }

    @RequestMapping(value = "generate-full-round")
    public List<Match> generateFullRound(String secretNewUser) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            if (user != null) {
                List<FootballClub> clubs = persist.getClubs();
                List<Match> matches = new ArrayList<>();
                for (int i = 0; i < clubs.size(); i++) {
                    for (int j = i + 1; j < clubs.size(); j++) {
                        GameResultGenerator gameResultGenerator = new GameResultGenerator();
                        GameProgression resultOfMatch = gameResultGenerator.generateResult(clubs.get(i), clubs.get(j));
                        clubs.get(i).updateStrengths(clubs.get(i), clubs.get(j), resultOfMatch.getWinningTeamName());
                        clubs.get(j).updateStrengths(clubs.get(j), clubs.get(i), resultOfMatch.getWinningTeamName());
                        Match match = new Match(clubs.get(i), clubs.get(j), resultOfMatch.getResult().keySet().iterator().next());
                        this.persist.createMatch(match);
                        match.setGameProgression(resultOfMatch);
                        persist.updateClubs(match);
                        persist.createGameProgression(resultOfMatch);
                        matches.add(match);
                    }
                }
                return matches; // חוזר המשחקים וגם השינוים של הקבוצות
            }
        }

        return null;
    }

    @RequestMapping(value = "get-info-game")
    public Map<String, String> getInfoGame(int idMatch) {
        Match match = persist.getMatchById(idMatch);
        return Map.of("homeTeam", match.getHomeTeam().getName(),
                "awayTeam", match.getAwayTeam().getName(),
                "result", match.getResult());

    }

    @RequestMapping(value = "get-info-club")
    public Map<String, String> getInfoClub(String clubName) {
        FootballClub club = persist.getClubByName(clubName);
        return Map.of("name", club.getName(),
                "strength", String.valueOf(club.getTeamStrength()),
                "wins", String.valueOf(club.getWins()),
                "draws", String.valueOf(club.getDraws()),
                "losses", String.valueOf(club.getLosses()),
                "goalsScored", String.valueOf(club.getGoalsScored()),
                "goalsConceded", String.valueOf(club.getGoalsConceded()),
                "matchesPlayed", String.valueOf(club.getMatchesPlayed()));
    }

    @RequestMapping(value = "get-game-progression")
    public GameProgression getGameResultObject(int idGameProgression) {
        return persist.getGameProgressionById(idGameProgression);
    }

    @RequestMapping(value = "add-bet-result")
    public boolean addBet(String secretNewUser, int idMatch, String betOnResult) {
        Match newMatch = new Match();
        if (secretNewUser != null && secretNewUser.length() > 0) {
            List<User> users = persist.getUsers();
            List<Match> matches = persist.getMatches();
            for (Match match : matches) {
                if (match.getIdMatch() == idMatch) {
                    newMatch = match;
                }
            }
            for (User user : users) {
                if (user.getSecret().equals(secretNewUser)) {
                    Bet bet = new Bet(user.getSecret(), newMatch, betOnResult);
                    this.persist.createBet(bet);
                    return true;

                }
            }
        }
        return false;
    }

    @RequestMapping(value = "add-bet-win")
    public boolean addBetWin(String secretNewUser, int idMatch, String betOnWin) {
        if (secretNewUser == null || secretNewUser.isEmpty() || idMatch == 0 || betOnWin == null || betOnWin.isEmpty()) {
            return false;
        }
        Match newMatch = persist.getMatchById(idMatch);
        User user = persist.getUserBySecret(secretNewUser);
        FootballClub team;
        if (user.getSecret().equals(secretNewUser)) {
            if (betOnWin.equals("draw")) {
                boolean draw = true;
                Bet bet = new Bet(user.getSecret(), newMatch, draw);
                this.persist.createBet(bet);
                return true;
            }
            team = persist.getClubByName(betOnWin);
            Bet bet = new Bet(user.getSecret(), newMatch, team);
            this.persist.createBet(bet);
            return true;
        }

        return false;
    }

    @RequestMapping(value = "get-matches")
    public List<Match> getMatches() {
        return persist.getMatches();
    }

    @RequestMapping(value = "get-bets")
    public List<Bet> getBets() {
        return persist.getBets();
    }

    @RequestMapping(value = "check-bet")
    public boolean checkBet(String secretNewUser, int idBet, int idMatch) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            Bet bet = persist.getBetById(idBet);
            Match match = persist.getMatchById(idMatch);
            GameProgression gameProgression = new GameProgression();
            gameProgression.setResult(Map.of(match.getResult(), match.getHomeTeam().getName()));
            String winningTeam = gameProgression.getWinningTeamName().toLowerCase(Locale.ROOT);
            if (user != null && bet != null && user.getSecret().equals(bet.getSecretUser())) {
                if (Objects.equals(winningTeam, "draw") && bet.isDraw()) {
                    persist.updateStatus(bet, true);
                    return true;

                }
                if (match.getResult().equals(bet.getPredictedResult())
                        || match.getHomeTeam().getName().equals(bet.getPredictedWinner().getName())
                        || match.getAwayTeam().getName().equals(bet.getPredictedWinner().getName())) {
                    persist.updateStatus(bet, true);
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping(value = "get-ratio-calculation")
    public Map<String, Double> getRatioCalculation(int idMatch) {
        Match match = persist.getMatchById(idMatch);
        return bettingSystem.ratioCalculation(match);
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
