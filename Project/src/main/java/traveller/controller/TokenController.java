package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import traveller.model.dto.MessageDTO;
import traveller.service.UserService;
import traveller.service.TokenService;

@RestController
public class TokenController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @GetMapping(value = "/tokens/{token}")
    public MessageDTO confirm(@PathVariable(name ="token") String token){
        return tokenService.confrimToken(token);
    }
}
