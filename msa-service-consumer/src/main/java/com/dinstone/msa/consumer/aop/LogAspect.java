package com.dinstone.msa.consumer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
@Aspect
public class LogAspect {

	private Logger logger = LoggerFactory.getLogger(getClass());

	ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut("execution(public * com.dinstone.msa.consumer.api..*.*(..))")
	public void logPointcut() {
	}

	@Before("logPointcut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		startTime.set(System.currentTimeMillis());
	}

	@AfterReturning(returning = "ret", pointcut = "logPointcut()")
	public void doAfterReturning(JoinPoint jp, Object ret) throws Throwable {
		logger.info("SPEND TIME (return) {}:{}", jp.getSignature().getDeclaringType(),
				(System.currentTimeMillis() - startTime.get()));
	}

	@After("logPointcut()")
	public void doAfter(JoinPoint jp) throws Throwable {
		logger.info("SPEND TIME (after) {}:{}", jp.getSignature().getDeclaringType(),
				(System.currentTimeMillis() - startTime.get()));
	}

	@Around("logPointcut()")
	public Object arround(ProceedingJoinPoint pjp) throws Throwable {
		// logger.info("before aspect {}", pjp.getSignature().getDeclaringTypeName());
		long st = System.currentTimeMillis();
		try {
			Object o = pjp.proceed();
			// logger.info("after returning，result is " + o);
			return o;
		} finally {
			long et = System.currentTimeMillis();
			logger.info("SPEND TIME (arround) {}:{}", pjp.getSignature().getDeclaringTypeName(), (et - st));
		}
	}
}
