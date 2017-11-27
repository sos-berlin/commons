package com.sos.jobscheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.exception.SOSInvalidDataException;
import com.sos.joc.model.calendar.Period;
import com.sos.joc.model.plan.RunTime;

import sos.xml.SOSXMLXPath;

public class RuntimeResolver {

    private SortedSet<Period> periods = new TreeSet<Period>(new Comparator<Period>() {

        @Override
        public int compare(Period o1, Period o2) {
            String z1 = o1.getSingleStart();
            if (z1 == null) {
                z1 = o1.getBegin();
            }
            String z2 = o2.getSingleStart();
            if (z2 == null) {
                z2 = o2.getBegin();
            }
            return z1.compareTo(z2);
        }
    });
    private SortedSet<String> holidays = new TreeSet<String>();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneOffset.UTC);
    private DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_INSTANT;
    private ZoneId runtimeTimezone = ZoneOffset.UTC;
    private String[] weekDaysMap = { "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" };
    private String[] monthsMap = { "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november",
            "december" };

    public RuntimeResolver() {
    }

    public RunTime resolveFromToday(SOSXMLXPath xpath, String to) throws Exception {
        return resolve(xpath, xpath.getRoot(), null, to, null);
    }

    public RunTime resolveFromToday(SOSXMLXPath xpath, Element runtime, String to) throws Exception {
        return resolve(xpath, runtime, null, to, null);
    }

    public RunTime resolveFromToday(SOSXMLXPath xpath, String to, String jobschedulerTimezone) throws Exception {
        return resolve(xpath, xpath.getRoot(), null, to, jobschedulerTimezone);
    }

    public RunTime resolveFromToday(SOSXMLXPath xpath, Element runtime, String to, String jobschedulerTimezone) throws Exception {
        return resolve(xpath, runtime, null, to, jobschedulerTimezone);
    }

    public RunTime resolve(SOSXMLXPath xpath, String from, String to) throws Exception {
        return resolve(xpath, xpath.getRoot(), from, to, null);
    }

    public RunTime resolve(SOSXMLXPath xpath, Element runtime, String from, String to) throws Exception {
        return resolve(xpath, runtime, from, to, null);
    }

    public RunTime resolve(SOSXMLXPath xpath, String from, String to, String jobschedulerTimezone) throws Exception {
        return resolve(xpath, xpath.getRoot(), from, to, jobschedulerTimezone);
    }

    public RunTime resolve(SOSXMLXPath xpath, Element runtime, String from, String to, String jobschedulerTimezone) throws Exception {
        setTimeZone(jobschedulerTimezone, runtime.getAttribute("time_zone"));
        if (from == null || from.isEmpty()) {
            from = dateFormatter.format(Instant.now());
        }
        if (to == null || to.isEmpty()) {
            throw new SOSInvalidDataException("parameter 'dateTo' is required.");
        }
        Calendar dateFrom = getCalendarFromString(from);
        Calendar dateTo = getCalendarFromString(to);
        
        setHolidays(xpath, runtime, dateFrom, dateTo);

        while (dateFrom.compareTo(dateTo) <= 0) {
            String date = dateFormatter.format(dateFrom.toInstant());
            int dayOfWeek = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
            int dayOfMonth = dateFrom.get(Calendar.DAY_OF_MONTH);
            int ultimoOfMonth = dateFrom.getActualMaximum(Calendar.DAY_OF_MONTH) - dayOfMonth;
            int which = (dayOfMonth / 7) + 1;
            int ultimoWhich = ((ultimoOfMonth / 7) + 1) * -1;
            int month = dateFrom.get(Calendar.MONTH);

            String xPathMonthExpr = "month[contains(@month, '" + monthsMap[month] + "')]/";
            String xPathExpr = "date[@date='" + date + "']/period";
            addPeriods(date, xpath.selectNodeList(runtime, xPathExpr));
            if (dayOfWeek == 0) {
                xPathExpr = "weekdays/day[contains(@day, '0') or contains(@day, '7')]/period";
                addPeriods(date, xpath.selectNodeList(runtime, xPathExpr));
                addPeriods(date, xpath.selectNodeList(runtime, xPathMonthExpr + xPathExpr));
            } else {
                xPathExpr = "weekdays/day[contains(@day, '" + dayOfWeek + "')]/period";
                addPeriods(date, xpath.selectNodeList(runtime, xPathExpr));
                addPeriods(date, xpath.selectNodeList(runtime, xPathMonthExpr + xPathExpr));
            }
            xPathExpr = "monthdays/day[contains(@day, '" + dayOfMonth + "')]/period";
            addPeriods(date, xpath.selectNodeList(runtime, xPathExpr));
            addPeriods(date, xpath.selectNodeList(runtime, xPathMonthExpr + xPathExpr));
            xPathExpr = "ultimos/day[contains(@day, '" + ultimoOfMonth + "')]/period";
            addPeriods(date, xpath.selectNodeList(runtime, xPathExpr));
            addPeriods(date, xpath.selectNodeList(runtime, xPathMonthExpr + xPathExpr));
            xPathExpr = "monthdays/weekday[@day='" + weekDaysMap[dayOfWeek] + "' and (@which='" + which + "' or which='" + ultimoWhich + "')]/period";
            addPeriods(date, xpath.selectNodeList(runtime, xPathExpr));
            addPeriods(date, xpath.selectNodeList(runtime, xPathMonthExpr + xPathExpr));

            dateFrom.add(Calendar.DATE, 1);
        }
        
        RunTime runTime = new RunTime(); 
        if (periods != null && !periods.isEmpty()) {
            runTime.setPeriods(new ArrayList<Period>(periods));
        }
        runTime.setTimeZone(runtimeTimezone.toString());
        runTime.setDeliveryDate(Date.from(Instant.now()));
        return runTime;
    }
    
    private void setHolidays(SOSXMLXPath xpath, Element runtime, Calendar dateFrom, Calendar dateTo) throws TransformerException,
            SOSInvalidDataException, DOMException {
        NodeList holidaysDates = xpath.selectNodeList(runtime, "holidays/holiday/@date");
        for (int i = 0; i < holidaysDates.getLength(); i++) {
            Calendar holiday = getCalendarFromString(holidaysDates.item(i).getNodeValue());
            if (holiday.before(dateFrom) || holiday.after(dateTo)) {
                continue;
            }
            holidays.add(holidaysDates.item(i).getNodeValue());
        }
        Node weekDaysInHolidays = xpath.selectSingleNode(runtime, "holidays/weekdays[day/@day]");
        if (weekDaysInHolidays != null) {
            boolean[] holidayOnEach = { 
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '0') or contains(@day, '7')]").getLength() > 0,
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '1')]").getLength() > 0, 
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '2')]").getLength() > 0, 
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '3')]").getLength() > 0, 
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '4')]").getLength() > 0, 
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '5')]").getLength() > 0, 
                    xpath.selectNodeList(weekDaysInHolidays, "day[contains(@day, '6')]").getLength() > 0 
            };
            while (dateFrom.compareTo(dateTo) <= 0) {
                String date = dateFormatter.format(dateFrom.toInstant());
                int dayOfWeek = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
                if (holidayOnEach[dayOfWeek]) {
                    holidays.add(date);
                }
                dateFrom.add(Calendar.DATE, 1);
            }
        }
    }

    private void setTimeZone(String jobschedulerTimezone, String timeZoneOfRuntime) {
        if (jobschedulerTimezone != null && !jobschedulerTimezone.isEmpty()) {
            this.runtimeTimezone = ZoneId.of(jobschedulerTimezone);
        }
        if (timeZoneOfRuntime != null && !timeZoneOfRuntime.isEmpty()) {
            this.runtimeTimezone = ZoneId.of(timeZoneOfRuntime);
        }
    }

    private void addPeriods(String date, NodeList periodList) throws Exception {
        for (int j = 0; j < periodList.getLength(); j++) {
            Period p = getPeriod((Element) periodList.item(j), date);
            if (p != null) {
                periods.add(p);
            }
        }
    }

    private Calendar getCalendarFromString(String cal) throws SOSInvalidDataException {
        if (cal != null && !cal.isEmpty()) {
            if (!cal.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                throw new SOSInvalidDataException("dates must have the format YYYY-MM-DD.");
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.parse(cal + "T00:00:00Z")));
            return calendar;
        }
        return null;
    }

    private Period getPeriod(Element periodElem, String date) throws SOSInvalidDataException {
        switch (periodElem.getAttribute("when_holiday")) {
        case "ignore_holiday":
            break;
        case "next_non_holiday":
            if (holidays.contains(date)) {
                Calendar dateCal = getCalendarFromString(date);
                dateCal.add(Calendar.DATE, 1);
                date = dateFormatter.format(dateCal.toInstant());
                while (holidays.contains(date)) {
                    dateCal.add(Calendar.DATE, 1);
                    date = dateFormatter.format(dateCal.toInstant());
                }
            }
            break;
        case "previous_non_holiday":
            if (holidays.contains(date)) {
                Calendar dateCal = getCalendarFromString(date);
                dateCal.add(Calendar.DATE, -1);
                date = dateFormatter.format(dateCal.toInstant());
                while (holidays.contains(date)) {
                    dateCal.add(Calendar.DATE, -1);
                    date = dateFormatter.format(dateCal.toInstant());
                }
            }
            break;
        default:
            if (holidays.contains(date)) {
                return null;
            }
            break;
        }

        Period p = new Period();
        if (periodElem.hasAttribute("single_start")) {
            p.setSingleStart(isoFormatter.format(ZonedDateTime.of(LocalDateTime.parse(date + "T" + periodElem.getAttribute("single_start"),
                    dateTimeFormatter), runtimeTimezone)));
            return p;
        }
        String begin = periodElem.getAttribute("begin");
        if (begin.isEmpty()) {
            begin = "00:00:00";
        }
        p.setBegin(isoFormatter.format(ZonedDateTime.of(LocalDateTime.parse(date + "T" + begin, dateTimeFormatter), runtimeTimezone)));
        String end = periodElem.getAttribute("end");
        if (end.isEmpty()) {
            end = "24:00:00";
        }
        if ("24:00:00".equals(end)) {
            p.setEnd(isoFormatter.format(ZonedDateTime.of(LocalDateTime.parse(date + "T23:59:59", dateTimeFormatter).plusSeconds(1L),
                    runtimeTimezone)));
        } else {
            p.setEnd(isoFormatter.format(ZonedDateTime.of(LocalDateTime.parse(date + "T" + end, dateTimeFormatter), runtimeTimezone)));
        }
        if (periodElem.hasAttribute("repeat")) {
            p.setRepeat(periodElem.getAttribute("repeat"));
            return p;
        }
        if (periodElem.hasAttribute("absolute_repeat")) {
            p.setRepeat(periodElem.getAttribute("absolute_repeat"));
            return p;
        }
        return p;
    }

}
