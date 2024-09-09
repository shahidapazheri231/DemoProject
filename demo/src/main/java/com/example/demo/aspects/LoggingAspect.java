package com.example.demo.aspects;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;



@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @PostConstruct
    public void init() {
        logger.info("LoggingAspect initialized");
    }
    // Log method entry
    @Before("execution(* com.example..*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Entering method: " + joinPoint.getSignature().getName());
        logger.info("Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    // Log method exit and return value
    @AfterReturning(pointcut = "execution(* com.example..*(..))", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
    	System.out.println("Aspect triggered for method: " + joinPoint.getSignature().getName());
        logger.info("Exiting method: " + joinPoint.getSignature().getName());
        logger.info("Returned: " + result);
    }
}