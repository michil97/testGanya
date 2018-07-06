package ru.indriver.entity;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OutputModel {

    private int eventCount;
    private Date eventDate;

    public OutputModel(int eventCount, Date eventDate) {
        this.eventCount = eventCount;
        this.eventDate = eventDate;
    }

    public int getEventCount() {
        return eventCount;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String toCsvRow() {
        String eventCountString = String.valueOf(getEventCount());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String eventDateString = String.valueOf(df.format(getEventDate()));

        return Stream.of(eventCountString, eventDateString)
                .map(value -> value.replaceAll("\"", "\"\""))
                .map(value -> Stream.of("\"", ",").anyMatch(value::contains) ? "\"" + value + "\"" : value)
                .collect(Collectors.joining(","));
    }
}
