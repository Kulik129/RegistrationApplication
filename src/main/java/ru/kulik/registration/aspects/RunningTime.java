package ru.kulik.registration.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RunningTime {
    @Pointcut(value = "execution(* ru.kulik.registration.*.*.*(..))")
    public void allMethods(){};

    @Around("allMethods()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Метод {} выполняется за {} миллисекунд", joinPoint.getSignature().getName(), elapsedTime);
        return result;
    }
}
