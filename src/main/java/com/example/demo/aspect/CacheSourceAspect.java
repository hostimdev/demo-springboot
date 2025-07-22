// src/main/java/com/example/demo/aspect/CacheSourceAspect.java
package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheSourceAspect {

    private static ThreadLocal<Boolean> fromCache = new ThreadLocal<>();

    @Around("@annotation(org.springframework.cache.annotation.Cacheable)")
    public Object trackCacheSource(ProceedingJoinPoint joinPoint) throws Throwable {
        // By default, assume data comes from database
        fromCache.set(false);

        // Get method signature for logging if needed
        String methodName = joinPoint.getSignature().toShortString();

        // Check if the cache has this value already by executing the method
        Object result = joinPoint.proceed();

        // If the method's body was actually executed, it means data came from database
        // If method's body was skipped by Spring's caching mechanism, data came from cache
        // We need to use a special trick to detect this

        // The original method will output this log when executed
        boolean methodExecuted = System.getProperty("cache.method.executed") != null;

        if (!methodExecuted) {
            // If the method wasn't executed, it means data came from cache
            fromCache.set(true);
            System.out.println(methodName + ": Data came from CACHE");
        } else {
            // Clear the flag
            System.clearProperty("cache.method.executed");
            System.out.println(methodName + ": Data came from DATABASE");
        }

        return result;
    }

    public static boolean isFromCache() {
        Boolean result = fromCache.get();
        return result != null && result;
    }

    public static void clearCacheFlag() {
        fromCache.remove();
    }
}
