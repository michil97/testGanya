package ru.indriver;

import ru.indriver.entity.Event;
import ru.indriver.entity.OutputModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

class Sorter {

    String timeZone = null;

    Sorter(String filePath, int cityId){

        ArrayList<Event> events = getEvents(filePath, cityId);
        System.out.println("Получен список. Всего " + events.size() + " элементов");


        //Получаем интересущюю нас TimeZone
        if (timeZone != null){
            TimeZone _timeZone = TimeZone.getTimeZone(timeZone);
            TimeZone.setDefault(_timeZone);
        }

        //Пишем в базу
        EventHandler.getInstance().writeToDataBase(events);

        //Получаем вывод
        ArrayList<OutputModel> outputModels = EventHandler.getInstance().getOutputDataFor(cityId);

        String csv = outputModels.stream()
                .map(OutputModel::toCsvRow)
                .collect(Collectors.joining(System.getProperty("line.separator")));

        Write.getInstance().Write(csv);
    }

    private ArrayList<Event> getEvents(String filePath, int searchCityId) {

        ArrayList<Event> events = new ArrayList<Event>();

        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(filePath));
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] event = line.split(cvsSplitBy);

                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date createdDate;
                java.sql.Timestamp createdDateTimestamp = null;
                try {
                    createdDate = parser.parse(event[0]);
                    createdDateTimestamp = new java.sql.Timestamp(createdDate.getTime());
                } catch (ParseException | NullPointerException e) {
                    e.printStackTrace();
                }


                int cityId = -1;
                try {
                    cityId = Integer.parseInt(event[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                String eventName = event[2];
                String timeZone = event[3];

                if (cityId != -1 && cityId == searchCityId) {
                    this.timeZone = timeZone;
                }

                Event eventObject = new Event(createdDateTimestamp, cityId, eventName, timeZone);

                events.add(eventObject);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return events;

    }

}
