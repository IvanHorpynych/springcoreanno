package main.appannotation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
public class CacheFileEventLogger extends FileEventLogger {
    List<Event> cache;
    @Value("${cacheSize}")
    String cacheSize;

    protected CacheFileEventLogger(){
        this.cache = new ArrayList<>();
    }

    public void logEvent(Event event) {
        cache.add(event);
        Integer cacheSize = Integer.valueOf(this.cacheSize);
        if (cache.size() >= cacheSize) {
            writeEventFromCache();
            cache.clear();
        }
    }

    private void writeEventFromCache() {
        for (Event event : cache) {
            super.logEvent(event);
        }
    }

    @PreDestroy
    private void destroy() {
        if (!cache.isEmpty())
            writeEventFromCache();
    }
}
