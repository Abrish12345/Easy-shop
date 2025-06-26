package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;
@CrossOrigin
//tells spring this class will handle rest API requests
@RestController
//maps all routes in this class to start with /profile
@RequestMapping("/profile")
// Ensures only users with 'USER' role can access these endpoints
@PreAuthorize("hasRole ('USER')")
public class ProfileController {

    // Inject DAOs needed to access data
    private ProductDao productDao;
    private UserDao userDao;
    private ProfileDao profileDao;

    // Constructor-based dependency injection
        @Autowired
    public ProfileController(ProductDao productDao, UserDao userDao, ProfileDao profileDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    // GET /profile → returns the profile for the currently logged-in user
        @GetMapping
    public Profile getProfile(Principal principal){
            // Get logged-in usernam
            String userName = principal.getName();

            // Find user by username
        User user = userDao.getByUserName(userName);
            // Return profile based on user's ID
        return profileDao.getByUserId(user.getId());

    }
    // PUT /profile → updates the profile for the currently logged-in user
    @PutMapping
    public void updateProfile(@RequestBody Profile profile, Principal principal){
        // Get logged-in username
        String userName = principal.getName();
        // Find user by username
        User user = userDao.getByUserName(userName);
        // Update the profile in the DB
        profileDao.update(user.getId(), profile);
    }
}
