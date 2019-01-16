package com.example.rudnev.remindme;

import android.content.Context;
import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class DateDecorator implements DayViewDecorator {

    CalendarDay date;
    private final int color;

    public DateDecorator(int color, CalendarDay date, Context context) {
        this.color = color;
        this.date = date;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.getYear()==date.getYear() && day.getMonth()==date.getMonth() && day.getDay()==date.getDay();
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, Color.rgb(27,164,171)));
        //view.addSpan(new ForegroundColorSpan(Color.rgb(27,164,171)));
    }
}
