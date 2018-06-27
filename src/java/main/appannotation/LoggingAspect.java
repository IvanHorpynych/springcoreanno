package main.appannotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggingAspect {
    private Map<Class<?>, Integer> counter;
    private EventLogger fileEventLogger;
    private Event event;


    LoggingAspect(EventLogger fileEventLogger, Event event) {
        this.fileEventLogger = fileEventLogger;
        this.event = event;
        counter = new HashMap<>();
    }

    @Pointcut("execution(* *.logEvent(..))")
    public void eventPointCut() {
    }

    /*@Before("eventPointCut()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(joinPoint.getTarget().getClass().getSimpleName());
    }*/

    @AfterReturning("eventPointCut()")
    public void count(JoinPoint joinPoint) {
        Class<?> currClass = joinPoint.getTarget().getClass();
        if (!counter.containsKey(currClass))
            counter.put(currClass, 0);
        counter.put(currClass, counter.get(currClass) + 1);
    }

    @PreDestroy
    public void logExecuting() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Class<?>, Integer> set : counter.entrySet())
            sb.append(set.getKey().getSimpleName() + ": " + set.getValue() + "\n");
        event.setMessage(sb.toString());
        fileEventLogger.logEvent(event);
    }

    @Around("eventPointCut() && within(ConsoleEventLogger) && args(evt)")
    private Object consLoggers(ProceedingJoinPoint point, Object evt) {
        if (!(counter.get(point.getTarget().getClass()) == null))
            if (counter.get(point.getTarget().getClass()) < 3) {
                try {
                    point.proceed(new Object[]{evt});
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        return evt;
    }
}
