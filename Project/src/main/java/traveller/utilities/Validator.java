package traveller.utilities;

import traveller.exception.BadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static void validateNames(String firstName, String lastName) {
        String lettersBulg = "[А-Я][a-я]+";
        String lettersEng = "[A-Z][a-z]+";
        if((!firstName.matches(lettersBulg) || !lastName.matches(lettersBulg)) &&
                (!firstName.matches(lettersEng) || !lastName.matches(lettersEng))){
            throw new BadRequestException("Names on Travergy must start with a capital letter and contain only " +
                    "letters from the same alphabet.");
        }
        if(firstName.length() < 1 || firstName.length() > 20) {
            throw new BadRequestException("First name on Travergy must be between one and twenty characters.");
        }
        if(lastName.length() < 1 || lastName.length() > 20) {
            throw new BadRequestException("Last name on Travergy must be between one and twenty characters.");
        }
    }
    public static void validateUsername(String username) {
        String lengthRegex = "^\\w{5,18}$";
        String charactersRegex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,15}[a-zA-Z0-9]$";
        if(!username.matches(lengthRegex)){
            throw new BadRequestException("Your username must be between 5 and 18 characters. Please try another.");
        }
        if(!username.matches(charactersRegex)){
            throw new BadRequestException("Your username contains an illegal character. Usernames can contain " +
                    "letters, numbers, and in the middle - a single dot, hyphen, and/or underscore. Please try another username.");
        }
    }

    public static void validatePassword(String password) {
        String capitalLetterRegex = "(.*[A-Z].*)";
        String digitRegex = "(.*\\d.*)";
        String lowerCaseRegex = "(.*[a-z].*)";
        if(!password.matches(capitalLetterRegex)){
            throw new BadRequestException("Your password must contain at least one uppercase letter. Please try another.");
        }
        if(!password.matches(digitRegex)){
            throw new BadRequestException("Your password must contain at least one digit. Please try another.");
        }
        if(!password.matches(lowerCaseRegex)){
            throw new BadRequestException("Your password must contain at least one lowercase letter. Please try another.");
        }
        {
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(password);
            boolean containsASpecialCharacter = m.find();
            if(!containsASpecialCharacter){
                throw new BadRequestException("Your password must contain at least one special character.\n" +
                        " Please try another.\n Accepted special characters: ` ~ ! @ # $ % ^ & * ( ) _ - + = ] } [ { ; : ' < > ? *");
            }
        }
    }

    public static void validateComment(String text) {
        if(text.length() < 1 || text.length() > 255){
            throw new BadRequestException("Comment must be between 1 and 255 characters.");
        }
    }

    public static void validateAge(int age) {
        if(age < 2 || age > 110){
            throw new BadRequestException("Invalid age.");
        }
    }

}
