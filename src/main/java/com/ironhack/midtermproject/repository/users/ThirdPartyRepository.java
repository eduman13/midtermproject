package com.ironhack.midtermproject.repository.users;

import com.ironhack.midtermproject.model.users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Integer> {

    ThirdParty findByUsername(String username);

}
