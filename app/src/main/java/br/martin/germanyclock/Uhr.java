package br.martin.germanyclock;

/**
 * Created by Martin on 17/10/2016.
 */

public class Uhr {

    private String hour;
    private String minute;


    public Uhr(){
        this.hour = "";
        this.minute = "";
    }


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}
