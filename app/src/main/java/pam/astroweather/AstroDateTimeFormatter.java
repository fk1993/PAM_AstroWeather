package pam.astroweather;

import java.util.Calendar;
import com.astrocalculator.AstroDateTime;

public class AstroDateTimeFormatter {

    private Calendar calendar;

    public AstroDateTimeFormatter(AstroDateTime dateTime) {
        if (dateTime != null) {
            calendar = Calendar.getInstance();
            calendar.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(),
                    dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
        }
    }

    public String getDate(){
        if (calendar != null)
            return String.format("%tF", calendar);
        else
            return "-";
    }

    public String getTime(){
        if (calendar != null)
            return String.format("%tT", calendar);
        else
            return "-";
    }
}
