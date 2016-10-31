package br.martin.germanyclock;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    /** Called when the activity is first created. */

    private static final String TAG = DrawView.class.getSimpleName();
    TimePicker timePicker;
    TextView frase;
    TextView hoursTV;
    TextView minutesTV;
    //TextView antes_depoisTV;
    String[] numberArrayString;
    String[] dezenasArrayString;
    long startSelection = 0;
    long endSelection = 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker)findViewById(R.id.chooseTime);

        frase = (TextView)findViewById(R.id.descricao);
        hoursTV = (TextView)findViewById(R.id.hoursTV);
        minutesTV = (TextView)findViewById(R.id.minutesTV);
        //antes_depoisTV = (TextView)findViewById(R.id.antes_depoisTV);
        timePicker.setIs24HourView(true);

        final DrawView surface = (DrawView)getWindow().findViewById(R.id.surfaceView4);

        numberArrayString = getResources().getStringArray(R.array.numbers);
        dezenasArrayString = getResources().getStringArray(R.array.dezenas);

        configCurrentTime(surface);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, final int i, final int i1) {
                startSelection = System.currentTimeMillis();

                if(needWait(startSelection, endSelection)){
                    Log.d(TAG, "Valor: " + i +":"+i1);
                    surface.setTime(i,i1);
                    Uhr uhr = getGermanize(i,i1, MainActivity.this);
                    hoursTV.setText(uhr.getHour());
                    minutesTV.setText(uhr.getMinute());
                    //frase.setText(getGermanize(i,i1, MainActivity.this));
                    //frase.setText(getGermanize(19,26, MainActivity.this));
                }
                endSelection = System.currentTimeMillis();



            }
        });

        Log.d(TAG, "View added");
    }

    private boolean needWait(long startSelection, long endSelection) {
        return startSelection-endSelection > 20;
    }

    private void configCurrentTime(DrawView surface) {

        Calendar calendar = Calendar.getInstance();
        surface.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        Uhr uhr = getGermanize(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), MainActivity.this);
        hoursTV.setText(uhr.getHour());
        minutesTV.setText(uhr.getMinute());

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    public Uhr getGermanize(int hour, int minute, Context context){

        //String hourString = "";
        Uhr uhr = new Uhr();

        //minute =  15;
        //hour = 23;

        //Log.i("teste", ""+hour);

        Log.i("teste", ""+hour+" : "+minute);
        if(minute != 0) {
            if (minute == 15 || minute == 45) {
                uhr.setHour("viertel");
                //hourString = getUmgangssprachlich(hour, minute, numberArrayString, hourString);
                uhr.setHour(getUmgangssprachlich(hour, minute, numberArrayString, uhr.getHour()));
            }else if (minute == 30){
                uhr.setHour(getUmgangssprachlich(hour, minute, numberArrayString, uhr.getHour()));
            } else if (minute % 10 == 0) {
                uhr.setHour("zehn ");
                uhr.setHour(getUmgangssprachlich(hour, minute, numberArrayString, uhr.getHour()));
            } else if (minute % 5 == 0) {
                uhr.setHour("fünf ");
                uhr.setHour(getUmgangssprachlich(hour, minute, numberArrayString, uhr.getHour()));
            } else if((minute > 57 && minute < 60) || (minute > 27 && minute < 30)){
                uhr.setHour("kurz ");
                uhr.setHour(getUmgangssprachlich(hour, minute, numberArrayString, uhr.getHour()));
            }else{
                //hour--;
                String minuteTxt = "";
                if(minute > 0 && minute <=12){
                    minuteTxt =  numberArrayString[minute-1];
                }else if(minute > 12 && minute <= 19){
                    minuteTxt =  getTeen(minute);
                }else if(minute >=20){
                    minuteTxt = getDezena(minute);
                }

                //hourString = getHourExpression(hour,numberArrayString,hourString) +" "+ context.getResources().getString(R.string.hour) + " " + minuteTxt;
                uhr.setHour(getHourExpression(hour,numberArrayString,uhr.getHour()) +" "+ context.getResources().getString(hour) + " ");
                uhr.setMinute(minuteTxt);
                //hoursTV.setText(getHourExpression(hour,numberArrayString,hourString) +" "+ context.getResources().getString(R.string.hour) + " ");
                //minutesTV.setText(minuteTxt);

            }
        }else{
            //hourString = getHourExpression(hour,numberArrayString,hourString) +" "+ context.getResources().getString(R.string.hour);
            uhr.setHour(getHourExpression(hour,numberArrayString,uhr.getHour()) +" "+ context.getResources().getString(hour) + " ");
            //hoursTV.setText(getHourExpression(hour,numberArrayString,hourString) +" "+ context.getResources().getString(R.string.hour));
        }

        return uhr;
    }

    @NonNull
    private String getUmgangssprachlich(int hour, int minute, String[] numberArrayString, String hourString) {

        //antes ou depois
        if((minute > 0 && minute <= 15) || (minute > 30 && minute <= 45)){
            hourString = hourString + " " + "nach";
            //antes_depoisTV.setText("nach ");
        }else if (minute > 15 && minute < 30 || (minute > 45 && minute < 60)){
            hourString = hourString + " " + "vor";
            //antes_depoisTV.setText("vor ");

        }//else{
            //antes_depoisTV.setText("");
        //}

        if(minute > 15 && minute <= 45){
            hourString = hourString + " " + "halb ";
            hour++;
            //Log.i("teste", "entrou " + hourString);
        }

        // Log.i("teste", ""+hour);

        hourString = getHourExpression(hour, numberArrayString, hourString);
        //Log.i("teste",String.valueOf(minute));
        //Log.i("teste", numberArrayString[hour]);

        return hourString;
    }

    private String getHourExpression(int hour, String[] numberArrayString, String hourString) {
        if(hour > 0 && hour <= 12){
            hourString = hourString +" "+numberArrayString[hour-1];
        }else if(hour > 12 && hour <= 19){
            hourString = hourString +" "+getTeen(hour);
        }else if(hour > 19 && hour <= 23){
            hourString = hourString +" "+getDezena(hour);
        }else if (hour == 0 || hour == 24 ){
            hourString = hourString +" "+"Mitternacht";
        }
        return hourString;
    }

    public String getDezena(int number){
        int unidade = (number%10)-1;
        if(unidade == -1){
            return dezenasArrayString[(number/10) -1];
        }else{
            return numberArrayString[unidade]+"und"+dezenasArrayString[(number/10) -1];
        }

    }
    public String getTeen(int number){
        return numberArrayString[(number-1)%10]+""+dezenasArrayString[0];
    }
}
