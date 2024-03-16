package ru.kulik.registration.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {
    @Pointcut(value = "execution(* ru.kulik.registration.*.*.*(..))")
    public void allMethods(){};

    @After("allMethods()")
    public void start(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.info("Конец выполнения метода: {} в классе: {}", methodName, className);
    }

    @Before("allMethods()")
    public void end(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.info("Начало выполнения метода: {} в классе: {}", methodName, className);
    }

    @AfterThrowing(pointcut = "allMethods()")
    public void exception(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.error("При выполнении метода: {} в классе: {}, возникло исключение", methodName, className);
    }
}
