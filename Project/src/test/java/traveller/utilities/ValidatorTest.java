package traveller.utilities;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import traveller.exception.BadRequestException;

public class ValidatorTest {
    @Test
    public void testValidateNames() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateNames("Jane", "Doe");
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
    public void testValidateNames4() {
        assertThrows(BadRequestException.class, () -> Validator.validateNames("Jane", "Аl"));
    }

    @Test
    public void testValidateNames5() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateNames("Аl", "Аl");
    }

    @Test
    public void testValidateUsername() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateUsername("janedoe");
    }

    @Test
    public void testValidateUsername2() {
        assertThrows(BadRequestException.class, () -> Validator.validateUsername("foo"));
    }

    @Test
    public void testValidatePassword() {
        assertThrows(BadRequestException.class, () -> Validator.validatePassword("iloveyou"));
        assertThrows(BadRequestException.class, () -> Validator.validatePassword("UUU"));
    }

    @Test
    public void testValidatePassword2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validatePassword(
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }

    @Test
    public void testValidateEmail() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateEmail("jane.doe@example.org");
    }

    @Test
    public void testValidateEmail2() {
        assertThrows(BadRequestException.class, () -> Validator.validateEmail("foo"));
    }

    @Test
    public void testValidateComment() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateComment("Text");
    }

    @Test
    public void testValidateComment2() {
        assertThrows(BadRequestException.class, () -> Validator.validateComment(""));
    }

    @Test
    public void testValidateAge() {
        assertThrows(BadRequestException.class, () -> Validator.validateAge(1));
        assertThrows(BadRequestException.class, () -> Validator.validateAge(255));
    }

    @Test
    public void testValidateAge2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateAge(2);
    }

    @Test
    public void testValidateSizeOfFile() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateSizeOfFile(new MockMultipartFile("Name", "AAAAAAAAAAAAAAAAAAAAAAAA".getBytes()));
    }

    @Test
    public void testValidateItsImage() {
        assertThrows(BadRequestException.class, () -> Validator.validateItsImage("text/plain"));
    }

    @Test
    public void testValidateItsImage2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateItsImage("image/");
    }

    @Test
    public void testValidateItsVideo() {
        assertThrows(BadRequestException.class, () -> Validator.validateItsVideo("text/plain"));
    }

    @Test
    public void testValidateItsVideo2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateItsVideo("video/");
    }

    @Test
    public void testValidateSafeImage() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateSafeImage("Json Response");
    }

    @Test
    public void testValidateSafeImage2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateSafeImage("foo");
    }

    @Test
    public void testValidateSafeImage3() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        Validator.validateSafeImage("42");
    }
}

