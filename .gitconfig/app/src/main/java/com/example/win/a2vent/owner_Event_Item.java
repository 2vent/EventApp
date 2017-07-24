package com.example.win.a2vent;

/**
 * Created by win on 2017-07-03.
 */

public class owner_Event_Item {
    String test;
    int event_number=0;
    int event_type=0;
    int event_stats=0;
    String event_URI=null;
    int event_price=0;
    int event_dis_price=0;
    int event_people=0;
    String event_startday=null;
    String event_endday=null;
    String event_starttime=null;
    String event_endtime=null;
    int event_payment=0;
    int event_target=0;
    int event_minage=0;
    int event_maxage=0;
    int event_sex=0;
    String event_area=null;
    String com_number=null;
    String id=null;
    String event_name=null;
    String event_main_URI=null;

    owner_Event_Item(int event_number, int event_type, int event_stats, String event_URI, int event_price, int event_dis_price, int event_people, String event_startday, String event_endday, String event_starttime, String event_endtime, int event_payment,
                     int event_target, int event_minage, int event_maxage, int event_sex, String event_area, String com_number, String id, String event_name, String event_main_URI){
        this.event_number=event_number;
        this.event_type=event_type;
        this.event_stats=event_stats;
        this.event_URI=event_URI;
        this.event_price=event_price;
        this.event_dis_price=event_dis_price;
        this.event_people=event_people;
        this.event_startday=event_startday;
        this.event_endday=event_endday;
        this.event_starttime=event_endtime;
        this.event_endtime=event_endtime;
        this.event_payment=event_payment;
        this.event_target=event_target;
        this.event_minage=event_minage;
        this.event_maxage=event_maxage;
        this.event_sex=event_sex;
        this.event_area=event_area;
        this.com_number=com_number;
        this.id=id;
        this.event_name=event_name;
        this.event_main_URI=event_main_URI;
    }



    public int getEvent_number(){
        return event_number;
    }

    public int getEvent_type() {
        return event_type;
    }

    public int getEvent_stats() {
        return event_stats;
    }

    public String getEvent_URI() {
        return event_URI;
    }

    public int getEvent_price() {
        return event_price;
    }

    public int getEvent_dis_price() {
        return event_dis_price;
    }

    public int getEvent_people() {
        return event_people;
    }

    public String getEvent_startday() {
        return event_startday;
    }

    public String getEvent_endday() {
        return event_endday;
    }

    public String getEvent_starttime() {
        return event_starttime;
    }

    public String getEvent_endtime() {
        return event_endtime;
    }

    public int getEvent_payment() {
        return event_payment;
    }

    public int getEvent_target() {
        return event_target;
    }

    public int getEvent_minage() {
        return event_minage;
    }

    public int getEvent_maxage() {
        return event_maxage;
    }

    public int getEvent_sex() {
        return event_sex;
    }

    public String getEvent_area() {
        return event_area;
    }

    public String getCom_number() {
        return com_number;
    }

    public String getId() {
        return id;
    }

    public String getEvent_name(){
        return event_name;
    }
    public String getEvent_main_URI(){
        return event_main_URI;
    }

}
