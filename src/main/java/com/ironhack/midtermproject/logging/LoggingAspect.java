package com.ironhack.midtermproject.logging;

import com.ironhack.midtermproject.exception.Error;
import com.ironhack.midtermproject.model.enums.StatusLog;
import com.ironhack.midtermproject.model.logs.Log;
import com.ironhack.midtermproject.repository.logging.LoggingRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    LoggingRepository loggingRepository;

    @AfterReturning(
            pointcut="execution(* com.ironhack.midtermproject.controller.AdminClController.*(..)) ||" +
                    "execution(* com.ironhack.midtermproject.controller.AccountHolderController.*(..)) ||" +
                    "execution(* com.ironhack.midtermproject.controller.ThirdPartyController.*(..))",
            returning="result"
    )
    public void loggingOk(JoinPoint joinPoint, Object result) {
        Log log = new Log();
            log.setFunction(joinPoint.getSignature().toShortString());
            log.setTimestamp(LocalDateTime.now().toString());
            log.setStatus(StatusLog.OK);
        loggingRepository.save(log);
    }

    @AfterThrowing(
            pointcut="execution(* com.ironhack.midtermproject.controller.AdminClController.*(..)) ||" +
                    "execution(* com.ironhack.midtermproject.controller.AccountHolderController.*(..)) ||" +
                    "execution(* com.ironhack.midtermproject.controller.ThirdPartyController.*(..))",
            throwing="exception"
    )
    public void loggingFail(JoinPoint joinPoint, Throwable exception) {
        Error error = new Error();
            error.setMessage(exception.getMessage());
        Log log = new Log();
            log.setFunction(joinPoint.getSignature().toShortString());
            log.setTimestamp(LocalDateTime.now().toString());
            log.setStatus(StatusLog.FAIL);
            log.setError(error);
        loggingRepository.save(log);
    }
}
