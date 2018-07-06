package ru.indriver;

import ru.indriver.entity.Event;
import ru.indriver.entity.OutputModel;

import java.sql.*;
import java.util.ArrayList;

public class EventHandler {

    private static final String url = "jdbc:mysql://localhost:3306/indriver?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow";
    private static final String user = "root";
    private static final String password = "123";

    private Connection con;

    private static EventHandler ourInstance = new EventHandler();

    public static EventHandler getInstance() {
        return ourInstance;
    }

    private EventHandler() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void writeToDataBase(ArrayList<Event> events){

        System.out.println("Запись в БД...");

        for (Event event : events) {
            try {
                //Write city
                String cityQuery = "INSERT into cities (id, timezone_id)" + "VALUES (?, ?)";
                PreparedStatement cityPreparedStmt = con.prepareStatement(cityQuery);
                cityPreparedStmt.setInt (1, event.getCityId());
                cityPreparedStmt.setString (2, event.getTimeZone());
                cityPreparedStmt.executeUpdate();
            } catch (SQLException ignore) {}

            try {
                //Write event
                String eventQuery = "INSERT into events (datetime, name, city_id)" + "VALUES (?, ?, ?)";
                PreparedStatement eventPreparedStmt = con.prepareStatement(eventQuery);
                eventPreparedStmt.setObject (1, event.getDatetime());
                eventPreparedStmt.setString (2, event.getEventName());
                eventPreparedStmt.setInt (3, event.getCityId());
                eventPreparedStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public ArrayList<OutputModel> getOutputDataFor(int cityId) {
        ArrayList<OutputModel> outputList = new ArrayList<OutputModel>();

        String query = "SELECT COUNT(datetime) as event_count, DATE(datetime) as event_date from events WHERE city_id = ? GROUP BY event_date;";

        try {
            PreparedStatement stmt;
            ResultSet rs;

            stmt = con.prepareStatement(query);

            stmt.setInt(1, cityId);


            rs = stmt.executeQuery();

            while (rs.next()) {

                int eventCount = rs.getInt(1);
                java.sql.Date eventDate = rs.getDate(2);


                OutputModel model = new OutputModel(eventCount, eventDate);

                outputList.add(model);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return outputList;
    }
}
