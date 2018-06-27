package main.appannotation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("app")
public class AppAnnotation {

    @Value("#{client}")
    private Client client;
    @Value("#{dbLogger}")
    private EventLogger defaultLogger;
    @Value("#{loggerMap}")
    Map<EventType, EventLogger> eventLoggers;


    public void logEvent(Event event, EventType eventType) {
        EventLogger logger = eventLoggers.get(eventType);
        if (logger == null)
            logger = defaultLogger;
        logger.logEvent(event);
    }

    public Client getClient() {
        return client;
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
                new ClassPathXmlApplicationContext(
                        "springAnnotation.xml");
        /*ConfigurableApplicationContext ctx =
                new AnnotationConfigApplicationContext(
                        AppConfig.class);*/

        AppAnnotation app = (AppAnnotation) ctx.getBean("app");
        Event event = (Event) ctx.getBean("event");
        event.setMessage(app.getClient().getGreeting());
        for (int i = 0; i < 10; i++)
            app.logEvent(event, null);

        ctx.close();
    }
}
