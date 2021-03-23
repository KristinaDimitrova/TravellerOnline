package traveller.utilities;


public class ValidPattern {

    public static boolean names(String firstName, String lastName) {
        String cyrPattern = "[А-Я][а-я][^#&<>\\\"~;$^%{}?]{1,20}$"; //cyrilic alphabet
        String latPattern = "[A-Z][a-z][^#&<>\\\"~;$^%{}?]{1,20}$"; //latin alphabet
        if(firstName.matches(cyrPattern) && lastName.matches(cyrPattern) ||
            firstName.matches(latPattern) && lastName.matches(latPattern)){
            return true;
        }
        return false;
    }
    public static boolean username(String username) {
        // source :  https://mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
        String userNamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        return username.matches(userNamePattern);
    }

    public static boolean password(String password) {
        // source : https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        return password.matches(pattern);
    }

    public static boolean email(String email) {
        String pattern =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(pattern);
    }
}
