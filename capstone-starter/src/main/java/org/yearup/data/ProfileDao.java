package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{

 //inserting a new profile
    Profile create(Profile profile);

// get a profile by user ID
    Profile getByUserId(int userId);

//UPDATE EXISTING PROFILE
    void update(int userId, Profile profile);


}
