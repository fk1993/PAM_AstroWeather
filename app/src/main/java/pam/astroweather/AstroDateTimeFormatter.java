package pam.astroweather;

import java.util.Calendar;
import com.astrocalculator.AstroDateTime;

public class AstroDateTimeFormatter {

    private Calendar calendar;

    public AstroDateTimeFormatter(AstroDateTime dateTime) {
        calendar = Calendar.getInstance();
        calendar.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(),
                dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    public String getDate(){
        return String.format("%tF", calendar);
    }

    public String getTime(){
        return String.format("%tT", calendar);
    }
}
