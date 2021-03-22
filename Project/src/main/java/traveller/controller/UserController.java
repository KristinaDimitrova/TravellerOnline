package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.UserDao;


@RestController
public class UserController { //todo Moni

    @Autowired
    private UserDao userDao;



}
