package org.example.controllers;

import org.apache.commons.lang3.ArrayUtils;
import org.example.entities.Bet;
import org.example.entities.Match;
import org.example.entities.User;
import org.example.responses.BasicResponse;
import org.example.responses.LoginResponse;
import org.example.utils.Errors;
import org.example.utils.Persist;
import org.example.entities.FootballClub;
import org.example.utils.ResultsGenerator.GameProgression;
import org.example.utils.Validator.EmailValidator;
import org.example.utils.Validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.utils.ResultsGenerator.GameResultGenerator;
import org.example.utils.ResultsGenerator.BettingSystem;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.example.utils.Errors.*;



@RestController
public class GeneralController {
    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Autowired
    private Persist persist;
    @Autowired
    private BettingSystem bettingSystem;
//    private ArrayUtils emitters;
//    private List<User> users = persist.getUsers();


    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String mail, String password) {
        if (!EmailValidator.isValid(mail)) {
            return new BasicResponse(false, ERROR_INVALID_EMAIL_OR_PASSWORD);
        }

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
    public int addUser(String username, String password, String mail) {
        if (username == null || username.isEmpty()) {
            return ERROR_NO_SUCH_USERNAME;
        }
        if (password == null || password.isEmpty()) {
            return ERROR_SIGN_UP_PASSWORDS_DONT_MATCH;
        }
        if (mail == null || mail.isEmpty()) {
            return ERROR_NO_SUCH_MAIL;
        }
        if (EmailValidator.isValid(mail) && PasswordValidator.isValid(password)) {
            User existingUserByUsername = persist.getUserByUsername(username);
            User existingUserByMail = persist.getUserByMail(mail);
            if (existingUserByUsername != null) {
                return ERROR_SIGN_UP_USERNAME_TAKEN;
            }
            if (existingUserByMail != null) {
                return ERROR_MAIL_TAKEN;
            }
            User user = new User(username, password, mail);
            persist.addUser(user);
            return 0; // 0 can be considered as success
        }
        return ERROR_INVALID_EMAIL_OR_PASSWORD;
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

                success = persist.editUser(newUser);
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

    @RequestMapping(value = "get-user-by-secret")
    public Map<String, String> getUserBySecret(String secretNewUser) {
        User user = persist.getUserBySecret(secretNewUser); // קורא לפונקציה שמחזירה משתמש לפי סיסמא
        return Map.of("username", user.getUsername(),
                "password", user.getPassword(),
                "mail", user.getMail(),
                "coins", String.valueOf(user.getCoins()));
    }

    @RequestMapping(value = "get-coins-from-user")
    public int getCoinsFromUser(String secretNewUser) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            if (user != null) {
                return user.getCoins();
            }
        }
        return 0;
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

    @RequestMapping(value = "get-name-clubs")
    public List<String> getNameClubs() {
        List<FootballClub> clubs = persist.getClubs();
        List<String> nameClubs = new ArrayList<>();
        for (FootballClub club : clubs) {
            nameClubs.add(club.getName());
        }
        return nameClubs;
    }


    @RequestMapping(value = "generate-result")
    public Match generateResult(String team1Name, String team2Name, String secretNewUser) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            FootballClub team1 = persist.getClubByName(team1Name);
            FootballClub team2 = persist.getClubByName(team2Name);

            if (user != null && team1 != null && team2 != null) {
                GameResultGenerator gameResultGenerator = new GameResultGenerator();
                GameProgression resultOfMatch = gameResultGenerator.generateResult(team1, team2);
                Match match = new Match(secretNewUser,team1, team2, resultOfMatch.getResult().keySet().iterator().next());
                this.persist.createMatch(match);
                match.setGameProgression(resultOfMatch);
                persist.updateClubs(match);
                persist.createGameProgression(resultOfMatch);
                return match;
            }
        }
        return null;
    }

//    @RequestMapping(value = "registration-unplayed-games")
//    public int[] getIdGameUnplayed(String[] futureMatches, String secretNewUser) {
//        if (secretNewUser != null && !secretNewUser.isEmpty()) {
//            User user = persist.getUserBySecret(secretNewUser);
//            int[] idMatches = new int[0];
//            if (user != null) {
//                for(String futureMatch : futureMatches) {
//                    String[] teams = futureMatch.split(",");
//                    FootballClub team1 = persist.getClubByName(teams[0]);
//                    FootballClub team2 = persist.getClubByName(teams[1]);
//                    if (team1 != null && team2 != null) {
//                        Match match = new Match(team1, team2);
//                        this.persist.createMatch(match);
//                        idMatches = ArrayUtils.add(idMatches, match.getIdMatch());
//                    }
//                }
//                return idMatches;
//            }
//        }
//        return null;
//    }

//    @RequestMapping(value = "generate-results-for-future-matches")
//    public List<Match> generateResultsForFutureMatches(int[] matchIds, String secretNewUser) {
//        List<Match> updatedMatches = new ArrayList<>();
//        if (secretNewUser != null && !secretNewUser.isEmpty()) {
//            User user = persist.getUserBySecret(secretNewUser);
//            if (user != null) {
//                for (Integer matchId : matchIds) {
//                    Match match = persist.getMatchById(matchId);
//                    if (match != null) {
//                        FootballClub team1 = match.getHomeTeam();
//                        FootballClub team2 = match.getAwayTeam();
//                        GameResultGenerator gameResultGenerator = new GameResultGenerator();
//                        GameProgression resultOfMatch = gameResultGenerator.generateResult(team1, team2);
//                        match.setResult(resultOfMatch.getResult().keySet().iterator().next());
//                        match.setGameProgression(resultOfMatch);
//                        persist.updateMatch(match);
//                        persist.updateClubs(match);
//                        persist.createGameProgression(resultOfMatch);
//                        updatedMatches.add(match);
//                    }
//                }
//            }
//        }
//        return updatedMatches;
//    }

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

    @RequestMapping(value = "get-history-matches")
    public List<Match> getHistoryMatches(@RequestParam("fromGame") int fromGame, @RequestParam("toGame") int toGame) {
        return persist.getHistoryMatches(fromGame, toGame);
    }

    @RequestMapping(value = "get-all-matches")
    public List<Match> getAllMatches() {
        return persist.getAllMatches();
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
    public boolean addBetWin(String secretNewUser, String betOnWin) {
        if (secretNewUser == null || secretNewUser.isEmpty() || betOnWin == null || betOnWin.isEmpty()) {
            return false;
        }
//        Match newMatch = persist.getMatchById(idMatch);
        User user = persist.getUserBySecret(secretNewUser);
        FootballClub team;
        if (user.getSecret().equals(secretNewUser)) {
            if (betOnWin.equals("draw")) {
                boolean draw = true;
                Bet bet = new Bet(user.getSecret(), draw);
                this.persist.createBet(bet);
                return true;
            }
            team = persist.getClubByName(betOnWin);
            Bet bet = new Bet(user.getSecret(), team);
            this.persist.createBet(bet);
            return true;
        }

        return false;
    }

    @RequestMapping(value = "last-id-match")
    public int lastIdMatch() {
        return persist.lastIdMatch();
    }

    @RequestMapping(value = "get-matches")
    public List<Match> getMatches() {
        return persist.getMatches();
    }

    @RequestMapping(value = "get-bets")
    public List<Bet> getBets() {
        return persist.getBets();
    }

    @RequestMapping(value = "get-bets-by-secret")
    public List<Bet> getBetsBySecret(String secretNewUser) {
        return persist.getBetsBySecret(secretNewUser);

    }

    @RequestMapping(value = "check-bet")
    public boolean checkBet(String secretNewUser, int idBet, String homeTeam, String awayTeam) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            Bet bet = persist.getBetById(idBet);
//            List<Match> listMatchesOfUser = persist.getMatchesBySecret(secretNewUser); // שגיאות פה
//            Match match = listMatchesOfUser.stream()// גם פה לא הולך
//                    .filter(m -> m.getHomeTeam().getName().equals(homeTeam) && m.getAwayTeam().getName().equals(awayTeam))
//                    .sorted(Comparator.comparing(Match::getIdMatch).reversed())
//                    .findFirst()
//                    .orElse(null);
            FootballClub homeTeamClub = persist.getClubByName(homeTeam);
            FootballClub awayTeamClub = persist.getClubByName(awayTeam);
            Match match = persist.getMatchByTeams(homeTeamClub, awayTeamClub, bet.getSecretUser());
            GameProgression gameProgression = new GameProgression();
            gameProgression.setResult(Map.of(match.getResult(), match.getHomeTeam().getName()));
            gameProgression.setTeam1InitialStrength(match.getHomeTeam().getName()); // homeTeam name
            gameProgression.setTeam2InitialStrength(match.getAwayTeam().getName()); // awayTeam name
            String winningTeam = gameProgression.getWinningTeamName();
            if (user != null && bet != null && user.getSecret().equals(bet.getSecretUser())) {
                if (Objects.equals(winningTeam, "draw") && bet.isDraw()) {
                    persist.updateStatus(bet, true);
                    return true;

                }
                if (match.getResult().equals(bet.getPredictedResult())
                        || bet.getPredictedWinner().getName().equals(winningTeam)
                        || (bet.isDraw() && winningTeam.equals("draw")))
                {
                    persist.updateStatus(bet, true);
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping(value = "delete-bets-user")
    public boolean deleteBetUser(String secretNewUser) {
        if (secretNewUser != null && !secretNewUser.isEmpty()) {
            User user = persist.getUserBySecret(secretNewUser);
            if (user != null) {
                return persist.deleteBetOfUser(secretNewUser);
            }
        }
        return false;

    }

    @RequestMapping(value = "get-ratio-calculation")
    public Map<String, Double> getRatioCalculation(int idMatch) {
        Match match = persist.getMatchById(idMatch);
        return bettingSystem.ratioCalculation(match);
    }

    @RequestMapping(value = "get-ratio-game" )
    public Map<String, Double> getRatioGame(String team1Name, String team2Name) {
        FootballClub team1 = persist.getClubByName(team1Name);
        FootballClub team2 = persist.getClubByName(team2Name);

        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException("One or both team names are invalid.");
        }

        Match match = new Match(team1, team2);
        Map<String, Double> ratios = bettingSystem.ratioCalculation(match);
        return ratios;
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

