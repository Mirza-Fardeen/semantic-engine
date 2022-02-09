package ie.nuig.i3market.semantic.engine.common;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public final class DateUtils {

    public static final String DEFAULT_TIME_ZONE = "GMT";
    public static final String DATE_TIME_WITH_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";

    private DateUtils() {
    }

    public static DateTimeFormatter getISO8601DateFormat() {

        return DateTimeFormatter
                .ofPattern(DATE_TIME_WITH_ZONE_FORMAT)
                .withZone(ZoneId.of(DEFAULT_TIME_ZONE));
    }

    public static LocalDateTime getLocalDateTime(String datetimeString) {
        if(StringUtils.isEmpty(datetimeString))
            return null;

        Calendar calendar = javax.xml.bind.DatatypeConverter.parseDateTime(datetimeString);
        TimeZone tz = calendar.getTimeZone();
        ZoneId zoneId = tz.toZoneId();

        return LocalDateTime.ofInstant(calendar.toInstant(), zoneId);
    }

    public static Date getLocalDate(String datetimeString) {
        if(StringUtils.isEmpty(datetimeString))
            return null;

        Calendar calendar = javax.xml.bind.DatatypeConverter.parseDateTime(datetimeString);


        return calendar.getTime();
    }

}
