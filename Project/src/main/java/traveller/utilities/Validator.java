package traveller.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;
import traveller.exception.BadRequestException;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class Validator {

    private static final double MAX_FILE_SIZE_MB = 512;

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

    public static void validateEmail(String email) {
        String pattern =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if(!email.matches(pattern)){
            throw new BadRequestException("Please enter a valid email address.");
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

    public static void validateSizeOfFile(MultipartFile file){
        long sizeBinary = file.getSize();
        double sizeMB = sizeBinary * 0.00000095367432;
        if(sizeMB > MAX_FILE_SIZE_MB){
            throw new BadRequestException("an uploaded file must not exceed " + MAX_FILE_SIZE_MB + " MB");
        }
    }

    public static void validateItsImage(final String contentType) {
        if(!contentType.startsWith("image/")){
            throw new BadRequestException("An image file is required.");
        }
    }

    public static void validateImageContent(InputStream inputStream) throws IOException {
        String credentialsToEncode = "acc_ad2de633ec13d13" + ":" + "1d608a8150329cd7fb909f4cbb821953";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint = "/categories/nsfw_beta";

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "Image Upload";

        URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + "testImage_" + System.nanoTime() + "\"" + crlf);
        request.writeBytes(crlf);

        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            request.write(dataBuffer, 0, bytesRead);
        }

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();

        InputStream responseStream = new BufferedInputStream(connection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();
        responseStream.close();
        connection.disconnect();
        System.out.println(response);
        validateSafeImage(response);
    }


    public static void validateItsVideo(String contentType) {
        if(!contentType.startsWith("video/")){
            throw new BadRequestException("A video file is required.");
        }
    }

    public static void validateSafeImage(String jsonResponse){
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject result = obj.get("result").getAsJsonObject();
        JsonArray array = result.get("categories").getAsJsonArray();
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JsonObject category = array.get(i).getAsJsonObject();
            double confidence = category.get("confidence").getAsDouble();
            JsonObject name = category.get("name").getAsJsonObject();
            String categoryName = name.get("en").getAsString();
            if(categoryName.equals("underwear") || categoryName.equals("nsfw")){
                if(confidence > 50){
                    throw new BadRequestException("Nudity and explicit sexual content is not allowed.");
                }
            }
        }
    }
}
