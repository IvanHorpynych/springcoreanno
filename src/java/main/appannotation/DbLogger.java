package main.appannotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class DbLogger implements EventLogger {

    @Autowired
    public JdbcTemplate jdbcTemplate;



    @Override
    public void logEvent(Event event) {
        //jdbcTemplate.execute("CREATE TABLE t_event (id INT,message VARCHAR(100))");
        jdbcTemplate.update(
                "INSERT INTO t_event (id,message) VALUES (?,?)",
                event.getId(),event.toString());
    }
}
