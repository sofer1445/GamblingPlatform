package org.example.controllers;

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
        if (username != null && username.length() > 0) {
            if (password != null && password.length() > 0) {
                if (mail != null && mail.length() > 0) {
                    if (EmailValidator.isValid(mail)) {
                        List<User> users = persist.getUsers();
                        for (User user : users) {
                            if (user.getMail().equals(mail)) {
                                return false;
                            }
                        }
                        if (PasswordValidator.isValid(password)) {
                            for (User user : users) {
                                if (user.getUsername().equals(username)) {
                                    return false;
                                }
                            }
                            User user = new User(username, password, mail);
                            persist.addUser(user);
                            return true;
                        }
                    }
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

    @RequestMapping (value = "generate-result")
    public String generateResult (String secretNewUser , FootballClub team1Name, FootballClub team2Name) {
        if (secretNewUser != null && secretNewUser.length() > 0) {
            List<User> users = persist.getUsers();
            for (User user : users) {
                if (user.getSecret().equals(secretNewUser)) {
                    GameResultGenerator gameResultGenerator = new GameResultGenerator();
                    return gameResultGenerator.generateResult(team1Name, team2Name).getResult();
                }
            }
        }
        return null;

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
