package ru.indriver.entity;

import java.sql.Timestamp;

public class Event {

    private Timestamp datetime;
    private int cityId;
    private String eventName;

    //Для MySQL отсутствует формат записи TimeZone
    private String timeZone;

    public Event(Timestamp datetime, int cityId, String eventName, String timeZone) {
        this.datetime = datetime;
        this.cityId = cityId;
        this.eventName = eventName;
        this.timeZone = timeZone;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public int getCityId() {
        return cityId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
