package com.ironhack.midtermproject.repository.logging;

import com.ironhack.midtermproject.model.logs.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingRepository extends MongoRepository<Log, String> {
}
