package main.appannotation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Component
@Scope("prototype")
public class Event {


    @Value("#{new java.util.Random().nextInt(T(java.lang.Integer).MAX_VALUE)}")
    int id;
    String message;
    @Value("#{new  java.util.Date()}")
    Date date;
    @Value("#{T(java.text.DateFormat).getDateTimeInstance()}")
    DateFormat df;


    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Event {\n" +
                "id: "+id+";\n"+
                "message: "+message+";\n"+
                "date: "+df.format(date)+";\n"+
                "}\n";
    }
}
