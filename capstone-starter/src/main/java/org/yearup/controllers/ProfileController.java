package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;
//tells spring this class will handle rest API requests
@RestController
//
@RequestMapping("/profile")
@PreAuthorize("hasRole ('USER')")
public class ProfileController {

    private ProductDao productDao;
    private UserDao userDao;
    private ProfileDao profileDao;

        @Autowired
    public ProfileController(ProductDao productDao, UserDao userDao, ProfileDao profileDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }
        @GetMapping
    public Profile getProfile(Principal principal){
            String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        return profileDao.getByUserId(user.getId());

    }
    @PutMapping
    public void updateProfile(Profile profile, Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        profileDao.update(user.getId(), profile);
    }
}
