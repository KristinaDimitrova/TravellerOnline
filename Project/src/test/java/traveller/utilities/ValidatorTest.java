package traveller.utilities;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import traveller.exception.BadRequestException;

public class ValidatorTest {

    @Test
    public void testValidateNames1() {
        assertThrows(BadRequestException.class, () -> Validator.validateNames("Jane", "Аl"));
    }

    @Test
    public void testValidateNames2() {
        assertThrows(BadRequestException.class, () -> Validator.validateNames("foo", "foo"));
    }

    @Test
    public void testValidateNames3() {
        assertThrows(BadRequestException.class, () -> Validator.validateNames("Аl", "Doe"));
    }

    @Test
    public void testValidateUsername1() {
        assertThrows(BadRequestException.class, () -> Validator.validateUsername("foo"));
    }

    @Test
    public void testValidatePassword() {
        assertThrows(BadRequestException.class, () -> Validator.validatePassword("iloveyou"));
        assertThrows(BadRequestException.class, () -> Validator.validatePassword("UUU"));
    }

    @Test
    public void testValidateEmail() {
        assertThrows(BadRequestException.class, () -> Validator.validateEmail("foo"));
    }

    @Test
    public void testValidateComment() {
        assertThrows(BadRequestException.class, () -> Validator.validateComment(""));
    }

    @Test
    public void testValidateAge() {
        assertThrows(BadRequestException.class, () -> Validator.validateAge(1));
        assertThrows(BadRequestException.class, () -> Validator.validateAge(255));
    }


    @Test
    public void testValidateItsImage() {
        assertThrows(BadRequestException.class, () -> Validator.validateItsImage("text/plain"));
    }


    @Test
    public void testValidateItsVideo() {
        assertThrows(BadRequestException.class, () -> Validator.validateItsVideo("text/plain"));
    }

}

