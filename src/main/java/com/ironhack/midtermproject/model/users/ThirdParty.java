package com.ironhack.midtermproject.model.users;

import com.google.common.hash.Hashing;
import com.ironhack.midtermproject.model.security.Role;
import com.ironhack.midtermproject.model.security.User;

import javax.persistence.Entity;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Entity
public class ThirdParty extends User {

    private String hashKey;

    public ThirdParty() {
    }

    public ThirdParty(ThirdParty thirdParty) {
        super(thirdParty.getUsername(), thirdParty.getPassword());
        setHashKey(thirdParty.getHashKey());
        Role role = new Role("ROLE_THIRD_PARTY");
        setRoles(Set.of(role));
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = Hashing.sha256()
                .hashString(hashKey, StandardCharsets.UTF_8)
                .toString();
    }
}
