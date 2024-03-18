package org.example.utils.Validator;
import java.util.regex.Pattern;

public class PasswordValidator {
    public static boolean isValid(String password) {
        String passwordRegex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null)
            return false;
        return pat.matcher(password).matches();
    }


}

//
//האם הסיסמה מכילה לפחות ספרה אחת (?=.*[0-9]).
//האם הסיסמה מכילה לפחות אות קטנה אחת (?=.*[a-z]).
//האם הסיסמה מכילה לפחות אות גדולה אחת (?=.*[A-Z]).
//האם הסיסמה מכילה לפחות תו מיוחד אחד (?=.*[@#$%^&+=]).
//האם הסיסמה אינה מכילה רווחים (?=\\S+$).
//האם אורך הסיסמה הוא בין 8 ל-20 תווים (.{8,20}$).
