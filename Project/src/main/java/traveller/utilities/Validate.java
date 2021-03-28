package traveller.utilities;

import traveller.exception.InvalidRegistrationInputException;

public class Validate {

    public static void firstLastNames(String firstName, String lastName) {
        String lettersBulg = "[А-Я][a-я]+";
        String lettersEng = "[A-Z][a-z]+";
        if((!firstName.matches(lettersBulg) || !lastName.matches(lettersBulg)) &&
                (!firstName.matches(lettersEng) || !lastName.matches(lettersEng))){
            throw new InvalidRegistrationInputException("Names on Travergy must start with a capital letter and contain only " +
                    "letters from the same alphabet.");
        }
        if(firstName.length() < 1 || firstName.length() > 20) {
            throw new InvalidRegistrationInputException("First name on Travergy must be between one and twenty characters.");
        }
        if(lastName.length() < 1 || lastName.length() > 20) {
            throw new InvalidRegistrationInputException("Last name on Travergy must be between one and twenty characters.");
        }
    }
    public static void username(String username) {
        String lengthRegex = "^\\w{5,18}$";
        String charactersRegex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,15}[a-zA-Z0-9]$";
        if(!username.matches(lengthRegex)){
            throw new InvalidRegistrationInputException("Your username must be between 5 and 18 characters. Please try another.");
        }
        if(!username.matches(charactersRegex)){
            throw new InvalidRegistrationInputException("Your username contains an illegal character. Usernames can contain " +
                    "letters, numbers, and in the middle - a single dot, hyphen, and/or underscore. Please try another username.");
        }
    }

    public static void password(String password) {
        String capitalLetterRegex = "(.*[A-Z].*)";
        String digitRegex = "(.*\\d.*)";
        String lowerCaseRegex = "(.*[a-z].*)";
        String specialCharRegex = ".*[!@#&()–[{}]:;',?/*~$^+=<>]*.";
        if(!password.matches(capitalLetterRegex)){
            throw new InvalidRegistrationInputException("Your password must contain at least one uppercase letter. Please try another.");
        }
        if(!password.matches(digitRegex)){
            throw new InvalidRegistrationInputException("Your password must contain at least one digit. Please try another.");
        }
        if(!password.matches(lowerCaseRegex)){
            throw new InvalidRegistrationInputException("Your password must contain at least one lowercase letter. Please try another.");
        }
        if(!password.matches(specialCharRegex)){
            throw new InvalidRegistrationInputException("Your password must contain at least one special character. Please try another.");
        }
    }

    public static void email(String email) {
        String pattern =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if(!email.matches(pattern)){
            throw new InvalidRegistrationInputException("Please enter a valid email address.");
        }
    }

}
