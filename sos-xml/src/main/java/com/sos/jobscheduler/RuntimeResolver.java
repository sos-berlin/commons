package com.sos.jobscheduler;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.transform.TransformerException;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.exception.SOSInvalidDataException;
import com.sos.joc.model.calendar.CalendarType;
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
        if (end.startsWith("24:00")) {
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
    
    public static Collection<RuntimeCalendar> getCalendarDatesFromToday(SOSXMLXPath xPath, Element curObject, String timeZone)
            throws TransformerException {
        String tzone = curObject.getAttribute("time_zone");
        if (tzone == null || tzone.isEmpty()) {
            tzone = timeZone;
        }
        NodeList dateList = xPath.selectNodeList(curObject, ".//date[@calendar]");
        NodeList holidayList = xPath.selectNodeList(curObject, ".//holiday[@calendar]/@calendar");
        String today = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.of(tzone)).format(Instant.now());
        Map<String, RuntimeCalendar> calendars = new HashMap<String, RuntimeCalendar>();
        for (int i = 0; i < dateList.getLength(); i++) {
            Element dateElem = (Element) dateList.item(i);
            String calendarPath = dateElem.getAttribute("calendar");
            if (!calendars.containsKey(calendarPath)) {
                RuntimeCalendar calendar = new RuntimeCalendar();
                calendar.setPath(calendarPath);
                calendar.setType(CalendarType.WORKING_DAYS);
                calendar.setDates(getCalendarDatesFromToday(xPath.selectNodeList(curObject, String.format(".//date[@calendar='%1$s']/@date",
                        calendarPath)), today));
                calendars.put(calendarPath, calendar);
                calendar.setPeriods(getCalendarPeriods(xPath.selectNodeList(dateElem, "period")));
            }
        }
        for (int i = 0; i < holidayList.getLength(); i++) {
            String calendarPath = holidayList.item(i).getNodeValue();
            if (!calendars.containsKey(calendarPath)) {
                RuntimeCalendar calendar = new RuntimeCalendar();
                calendar.setPath(calendarPath);
                calendar.setType(CalendarType.NON_WORKING_DAYS);
                calendar.setDates(getCalendarDatesFromToday(xPath.selectNodeList(curObject, String.format(".//holiday[@calendar='%1$s']/@date",
                        calendarPath)), today));
                calendars.put(calendarPath, calendar);
            }
        }
        return calendars.values();
    }
    
    public static TreeSet<RuntimeCalendar> getCalendarDatesFromToday(org.dom4j.Document doc, String timeZone) throws TransformerException {
        org.dom4j.Element root = doc.getRootElement();
        org.dom4j.Element runTime = null;
        if ("schedule".equals(root.getName())) {
            runTime = root;
        } else {
            runTime = root.element("run_time");
        }
        if (runTime == null) {
           return null; 
        } else {
           return getCalendarDatesFromToday(runTime, timeZone); 
        }
    }
    
    @SuppressWarnings("unchecked")
    public static TreeSet<RuntimeCalendar> getCalendarDatesFromToday(org.dom4j.Element curObject, String timeZone) throws TransformerException {
        String tzone = curObject.attributeValue("time_zone");
        if (tzone == null) {
            tzone = timeZone;  
        }
        List<org.dom4j.Element> dateList = curObject.selectNodes(".//date[@calendar]");
        List<org.dom4j.Attribute> holidayList = curObject.selectNodes(".//holiday[@calendar]/@calendar");
        String today = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.of(tzone)).format(Instant.now());
        Map<String, RuntimeCalendar> calendars = new HashMap<String, RuntimeCalendar>();
        for (org.dom4j.Element dateElem : dateList) {
            String calendarPath = dateElem.attributeValue("calendar");
            if (!calendars.containsKey(calendarPath)) {
                RuntimeCalendar calendar = new RuntimeCalendar();
                calendar.setPath(calendarPath);
                calendar.setType(CalendarType.WORKING_DAYS);
                calendar.setDates(getCalendarDatesFromToday(curObject.selectNodes(String.format(".//date[@calendar='%1$s']/@date", calendarPath)),
                        today));
                calendars.put(calendarPath, calendar);
                calendar.setPeriods(getCalendarPeriods(dateElem.selectNodes("period")));
            }
        }
        for (org.dom4j.Attribute holidayNode : holidayList) {
            String calendarPath = holidayNode.getStringValue();
            if (!calendars.containsKey(calendarPath)) {
                RuntimeCalendar calendar = new RuntimeCalendar();
                calendar.setPath(calendarPath);
                calendar.setType(CalendarType.NON_WORKING_DAYS);
                calendar.setDates(getCalendarDatesFromToday(curObject.selectNodes(String.format(".//holiday[@calendar='%1$s']/@date", calendarPath)),
                        today));
                calendars.put(calendarPath, calendar);
            }
        }
        return new TreeSet<RuntimeCalendar>(calendars.values());
    }
    
    private static Set<String> getCalendarDatesFromToday(NodeList nodeList, String today) {
        SortedSet<String> dates = new TreeSet<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String date = nodeList.item(i).getNodeValue();
            if (today.compareTo(date) < 1) {
                dates.add(date);
            }
        }
        return dates;
    }
    
    private static Set<String> getCalendarDatesFromToday(List<org.dom4j.Attribute> nodeList, String today) {
        SortedSet<String> dates = new TreeSet<String>();
        for (org.dom4j.Attribute dateNode : nodeList) {
            String date = dateNode.getStringValue();
            if (today.compareTo(date) < 1) {
                dates.add(date);
            }
        }
        return dates;
    }
    
    private static List<Period> getCalendarPeriods(NodeList nodeList) {
        List<Period> periods = new ArrayList<Period>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element period = (Element) nodeList.item(i);
            Period p = new Period();
            if (period.hasAttribute("single_start")) {
                p.setSingleStart(period.getAttribute("single_start"));
            } else if (period.hasAttribute("repeat")) {
                p.setRepeat(period.getAttribute("repeat"));
            } else if (period.hasAttribute("absolute_repeat")) {
                p.setAbsoluteRepeat(period.getAttribute("absolute_repeat"));
            }
            if (period.hasAttribute("begin")) {
                p.setBegin(period.getAttribute("begin"));
            }
            if (period.hasAttribute("end")) {
                p.setEnd(period.getAttribute("end"));
            }
            if (period.hasAttribute("when_holiday")) {
                p.setWhenHoliday(period.getAttribute("when_holiday"));
            }
            periods.add(p);
        }
        return periods;
    }
    
    private static List<Period> getCalendarPeriods(List<org.dom4j.Element> nodeList) {
        List<Period> periods = new ArrayList<Period>();
        for (org.dom4j.Element period : nodeList) {
            Period p = new Period();
            p.setSingleStart(period.attributeValue("single_start"));
            p.setRepeat(period.attributeValue("repeat"));
            p.setAbsoluteRepeat(period.attributeValue("absolute_repeat"));
            p.setBegin(period.attributeValue("begin"));
            p.setEnd(period.attributeValue("end"));
            p.setWhenHoliday(period.attributeValue("when_holiday"));
            periods.add(p);
        }
        return periods;
    }

    public static Node updateCalendarInRuntimes(SOSXMLXPath xPath, Node curObject, List<String> dates, String objectType, String path, String calendarPath, String calendarOldPath)
            throws Exception {
        NodeList dateParentList = xPath.selectNodeList(curObject, String.format(".//date[@calendar='%1$s']/parent::*", calendarOldPath));
        NodeList holidayParentList = xPath.selectNodeList(curObject, String.format(".//holiday[@calendar='%1$s']/parent::*", calendarOldPath));
        boolean runTimeIsChanged = false;

        for (int i = 0; i < dateParentList.getLength(); i++) {
            NodeList dateList = xPath.selectNodeList(dateParentList.item(i), String.format("date[@calendar='%1$s']", calendarOldPath));
            if (updateCalendarInRuntime(dateList, dates, calendarPath)) {
                runTimeIsChanged = true;
            }
        }
        for (int i = 0; i < holidayParentList.getLength(); i++) {
            NodeList holidayList = xPath.selectNodeList(holidayParentList.item(i), String.format("holiday[@calendar='%1$s']", calendarOldPath));
            if (updateCalendarInRuntime(holidayList, dates, calendarPath)) {
                runTimeIsChanged = true;
            }
        }
        for (int i = 0; i < holidayParentList.getLength(); i++) {
            NodeList children = holidayParentList.item(i).getChildNodes();
            if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                holidayParentList.item(i).removeChild(children.item(0));
            }
        }
        for (int i = 0; i < dateParentList.getLength(); i++) {
            NodeList children = dateParentList.item(i).getChildNodes();
            if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                dateParentList.item(i).removeChild(children.item(0));
            }
        }
        if (runTimeIsChanged) {
            return curObject;
        }
        return null;
    }
    
    private static boolean updateCalendarInRuntime(NodeList nodeList, List<String> dates, String calendarPath) {
        Element firstElem = null;
        Node parentOfFirstElem = null;
        Node textNode = null;
        
        if (nodeList.getLength() > 0) {
            firstElem = (Element) nodeList.item(0);
            parentOfFirstElem = firstElem.getParentNode();
            if (firstElem.getPreviousSibling().getNodeType() == Node.TEXT_NODE) {
                textNode = firstElem.getPreviousSibling(); 
            }
        }
        if (firstElem != null) {
            for (int i=1; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getPreviousSibling().getNodeType() == Node.TEXT_NODE) {
                    parentOfFirstElem.removeChild(nodeList.item(i).getPreviousSibling()); 
                }
                parentOfFirstElem.removeChild(nodeList.item(i));
            }
            if (dates.isEmpty()) {
                if (textNode != null) {
                    parentOfFirstElem.removeChild(textNode); 
                }
                parentOfFirstElem.removeChild(firstElem);
            } else {
                String lastDateOfdates = dates.remove(dates.size()-1);
                dates.add(0, lastDateOfdates);
                firstElem.setAttribute("date", dates.get(0));
                firstElem.setAttribute("calendar", calendarPath);
                for (int i=1; i < dates.size(); i++) {
                    Element newElem = (Element) firstElem.cloneNode(true);
                    newElem.setAttribute("date", dates.get(i));
                    if (textNode != null) {
                        parentOfFirstElem.insertBefore(textNode.cloneNode(false), textNode);
                        parentOfFirstElem.insertBefore(newElem, textNode);
                    } else {
                        parentOfFirstElem.insertBefore(newElem, firstElem);
                    }
                }
            }
        }
        return firstElem != null;
    }
    
    public static void updateCalendarInRuntimes(String xml, Writer writer, Collection<RuntimeCalendar> calendars) throws IOException, DocumentException {
        updateCalendarInRuntimes(DocumentHelper.parseText(xml), writer, calendars);
    }
    
    public static void updateCalendarInRuntimes(Path xmlFile, Writer writer, Collection<RuntimeCalendar> calendars) throws IOException, DocumentException {
        updateCalendarInRuntimes(new String(Files.readAllBytes(xmlFile)), writer, calendars);
    }
    
    @SuppressWarnings("unchecked")
    public static void updateCalendarInRuntimes(org.dom4j.Document doc, Writer writer, Collection<RuntimeCalendar> calendars) throws IOException, DocumentException {
        org.dom4j.Element root = doc.getRootElement();
        org.dom4j.Element runTime = null;
        if ("schedule".equals(root.getName())) {
            runTime = root;
        } else {
            runTime = root.element("run_time");
            if (runTime == null) {
                runTime = DocumentHelper.createElement("run_time");
                //insert before commands (in a Job) or xml_payload (in an Order)
                org.dom4j.Node node = root.selectSingleNode("commands|xml_payload");
                if (node != null) {
                    root.elements().add(root.elements().size()-1, runTime);
                } else {
                    root.elements().add(runTime);
                }
            }
        }
        
        //write CDATA 
        if ("job".equals(root.getName())) {
            org.dom4j.Element script = root.element("script");
            if (script != null) {
                String scriptContent = script.getText();
                if (!scriptContent.trim().isEmpty()) {
                    List<org.dom4j.Node> textNodes = script.selectNodes("text()");
                    for (org.dom4j.Node testNode : textNodes) {
                        script.remove(testNode);
                    }
                    script.add(DocumentHelper.createCDATA("\n" + scriptContent.trim() + "\n"));
                }
            }
        }
        
        org.dom4j.Element holidays = runTime.element("holidays");
        
        //remove old calendar dates
        List<org.dom4j.Node> oldRunTimeCalendar = runTime.selectNodes("date[@calendar]");
        for (org.dom4j.Node child : oldRunTimeCalendar) {
            runTime.remove(child);
        }
        if (holidays != null) {
            List<org.dom4j.Node> oldHolidaysCalendar = holidays.selectNodes("holiday[@calendar]");
            for (org.dom4j.Node child : oldHolidaysCalendar) {
                holidays.remove(child);
            }
        }
        
        //insert new calendar dates
        if (calendars != null) {
            List<org.dom4j.Element> datesList = new ArrayList<org.dom4j.Element>();
            List<org.dom4j.Element> holidaysList = new ArrayList<org.dom4j.Element>();
            for (RuntimeCalendar rc : calendars) {
                if (CalendarType.WORKING_DAYS == rc.getType()) {
                    org.dom4j.Element dateElem = DocumentHelper.createElement("date");
                    for (Period p : rc.getPeriods()) {
                        org.dom4j.Element periodElem = DocumentHelper.createElement("period");
                        periodElem.addAttribute("single_start", p.getSingleStart()).addAttribute("begin", p.getBegin()).addAttribute("end", p
                                .getEnd()).addAttribute("repeat", p.getRepeat()).addAttribute("absolute_repeat", p.getAbsoluteRepeat()).addAttribute(
                                        "when_holiday", p.getWhenHoliday());
                        dateElem.add(periodElem);
                    }
                    for (String date : rc.getDates()) {
                        datesList.add(dateElem.createCopy().addAttribute("calendar", rc.getPath()).addAttribute("date", date));
                    }
                } else {
                    org.dom4j.Element holidayElem = DocumentHelper.createElement("holiday");
                    for (String date : rc.getDates()) {
                        holidaysList.add(holidayElem.createCopy().addAttribute("calendar", rc.getPath()).addAttribute("date", date));
                    }
                }
            }
            if (!datesList.isEmpty()) {
                //insert after last element of period|at
                runTime.elements().addAll(runTime.selectNodes("period|at").size(), datesList);
            }
            if (!holidaysList.isEmpty()) {
                if (holidays == null) {
                    holidays = DocumentHelper.createElement("holidays");
                    runTime.add(holidays);
                }
                //insert after last weekdays element
                holidays.elements().addAll(holidays.selectNodes("weekdays").size(), holidaysList);
            }
        }
        //clear empty holidays
        if (holidays != null && holidays.elements().isEmpty()) {
            runTime.remove(holidays);  
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(doc.getXMLEncoding());
        format.setXHTML(true);
        format.setIndentSize(4);
        format.setExpandEmptyElements(false);
        //format.setTrimText(false);
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        xmlWriter.write(doc);
        xmlWriter.flush();
        xmlWriter.close();
    }
    
}
