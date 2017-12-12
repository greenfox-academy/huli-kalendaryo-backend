package com.greenfoxacademy.opal.kalendaryo.kalendaryo.controllers;


import com.google.api.services.calendar.model.Calendar;
import com.greenfoxacademy.opal.kalendaryo.kalendaryo.model.CalendarModel;
import com.greenfoxacademy.opal.kalendaryo.kalendaryo.service.CalendarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.greenfoxacademy.opal.kalendaryo.kalendaryo.authorization.AuthorizeKal.getCalendarService;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    CalendarModelService calendarModelService;

    @GetMapping("/show")
    public String showCalendar() throws IOException {
        com.google.api.services.calendar.Calendar service = getCalendarService();
        Calendar calendar = service.calendars().get("primary").execute();
        System.out.println(calendar.getSummary());
        return "redirect:https://calendar.google.com/calendar/b/1/r";
    }

    @PutMapping("/update")
    public String updateCalendar() throws IOException {
        com.google.api.services.calendar.Calendar service =
                getCalendarService();
       Calendar calendar =
                service.calendars().get("primary").execute();
        calendar.setSummary("calendarSummary");
        Calendar updatedCalendar =
                service.calendars().update(calendar.getId(), calendar).execute();
        System.out.println(updatedCalendar.getEtag());
        return "redirect:https://calendar.google.com/calendar/b/1/r";
    }

    @GetMapping("/insert")
    public String insertCalendar() throws IOException {
        com.google.api.services.calendar.Calendar service =
                getCalendarService();

        Calendar calendar = service.calendars().insert(new Calendar()
                .setSummary("savetobackend")
                .setTimeZone("America/Los_Angeles"))
                .execute();

        //Google Calendar
        Calendar googleCalendar = service.calendars().get("primary").execute();

        //Opal Calendar
        CalendarModel calendarModel = calendarModelService.setCalendarAttributes(googleCalendar);

        calendarModelService.saveCalendar(calendarModel);

        return "redirect:https://calendar.google.com/calendar/b/1/r";
    }

    @DeleteMapping("/delete")
    public String deleteCalendar() throws IOException {
        com.google.api.services.calendar.Calendar service =
                getCalendarService();
        service.calendars().delete("secondaryCalendarId").execute();
        return "redirect:https://calendar.google.com/calendar/b/1/r";
    }
}