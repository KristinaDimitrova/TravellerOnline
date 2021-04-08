package traveller.utilities;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class PasswordEncoder extends BCryptPasswordEncoder{
//BCryptPasswordEncoder does not have @Component, yet we want to inject the PasswordEncoder
//otherwise it underlines it (in User Service) and says, 'Could not autowire. No beans of 'BCryptPasswordEncoder' type found.'
//Thanks for pointing this out, Yordan! :)
}
