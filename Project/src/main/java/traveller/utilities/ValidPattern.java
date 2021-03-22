package traveller.utilities;


public class ValidPattern {
    public static boolean name(String name) {
        /* Names cannot include TODO
        Symbols, numbers, unusual capitalization, repeating characters or punctuation.
        Characters from multiple languages.
        Titles of any kind (example: professional, religious).
        Words or phrases in place of a name.
        Offensive or suggestive words of any kind.*/
        return true;
    }
    public static boolean username(String username) {
        String userNamePattern = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
       // source :  https://mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
        return username.matches(userNamePattern);
    }

    public static boolean password(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        // source : https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
        return password.matches(pattern);
    }

    public static boolean email(String email) {
        String pattern =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(pattern);
    }
}
