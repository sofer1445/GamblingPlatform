package org.example.utils.Validator;

import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

//    public static void main(String[] args) {
//        String email = "sofer1445@gmail.com";
//        System.out.println(isValid(email));
//
//
//
//    }
}



