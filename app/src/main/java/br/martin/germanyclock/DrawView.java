package br.martin.germanyclock;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public  class DrawView extends SurfaceView implements SurfaceHolder.Callback  {

    private static final String TAG = DrawView.class.getSimpleName();

    private MainThread thread;
    Paint paintRelogioCor;
    Paint paintHour;
    Paint paintMinutos;
    Paint paintBackgroundColor;
    Rect ponteiroMinutos;
    Rect ponteiroHoras;

    RectF primeiroQuadrante;
    Paint quadranteCor;

    int raioRelogio;

    int xRelogio;
    int yRelogio;

    private int horaPonteiro;
    private int minutoPonteiro;


    public DrawView(Context context,AttributeSet attr) {
        super(context,attr);

        paintRelogioCor = new Paint();
        paintHour = new Paint();
        paintMinutos = new Paint();
        paintBackgroundColor = new Paint();

        raioRelogio = 250;

        quadranteCor = new Paint();

        ponteiroHoras = new Rect();
        ponteiroMinutos = new Rect();
        primeiroQuadrante = new RectF();

        quadranteCor.setColor(ContextCompat.getColor(context, R.color.redQuadrant));
        paintRelogioCor.setColor(ContextCompat.getColor(context,R.color.grayClock));
        paintHour.setColor(ContextCompat.getColor(context, R.color.colorHours));
        paintMinutos.setColor(ContextCompat.getColor(context, R.color.colorMinutes));
        paintBackgroundColor.setColor(ContextCompat.getColor(context, R.color.colorWhite));

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create droid and load bitmap
        // create the game loop thread
        thread = new MainThread(getHolder(), this);


        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }
    //6wd2krxw
//n3ac4cf2
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // fills the canvas with black
        xRelogio = getWidth()/2;
        yRelogio = getHeight()/2;

        canvas.drawColor(Color.WHITE);

        //ponteiroMinutos = new Rect(getWidth()/2-20, getHeight()/2-200 , getWidth()/2+20, getHeight()/2);
        //ponteiroHoras = new Rect(getWidth()/2-20, getHeight()/2-150 , getWidth()/2+20, getHeight()/2);

        ponteiroMinutos.set(xRelogio-20, yRelogio-200, xRelogio+20, yRelogio);
        ponteiroHoras.set(xRelogio-20, yRelogio-150, xRelogio+20, yRelogio);


        desenharQuadrante(canvas,minutoPonteiro, quadranteCor);
        canvas.drawCircle(xRelogio,yRelogio, raioRelogio, paintRelogioCor);
        canvas.drawCircle(xRelogio,yRelogio, raioRelogio*0.95f, paintBackgroundColor);
        //primeiroQuadrante = new RectF(242, 178, 740, 693);

        //0,-90
        //0,90
        //90,90
        //180,90

        //Ponteiro dos minutos
        canvas.save();
        canvas.rotate(6*minutoPonteiro,ponteiroMinutos.centerX(),ponteiroMinutos.bottom);
        canvas.drawRect(ponteiroMinutos, paintMinutos);
        canvas.restore();

        //Ponteiro das horas
        canvas.save();
        canvas.rotate(30*horaPonteiro,ponteiroHoras.centerX(),ponteiroHoras.bottom);
        canvas.drawRect(ponteiroHoras, paintHour);
        canvas.restore();

        //radio = 350

    }

    private void desenharQuadrante(Canvas canvas,int minutos, Paint quadranteCor) {

        //primeiroQuadrante.set(raioRelogio-25, getHeight()/2-250-25, getWidth()/2+raioRelogio+25, getHeight()/2+250+25);
        float tamanhoQuadrante = raioRelogio * 2.1f;
        float xQuadrante = xRelogio - tamanhoQuadrante/2;
        float yQuadrante = yRelogio - tamanhoQuadrante/2;

        primeiroQuadrante.set(xQuadrante, yQuadrante, xQuadrante+tamanhoQuadrante, yQuadrante+tamanhoQuadrante);

        //1ºquadrante 0,-90
        //2ºquadrante 0,90
        //3ºquadrante 90,90
        //4ºquadrante 180,90
        int anguloInicial = 0;
        int anguloFinal = 0;

        if(minutos>=0 && minutos <=15){
            anguloInicial = 0;
            anguloFinal = -90;
        }else if(minutos > 15 && minutos <=30){
            anguloInicial = 0;
            anguloFinal = 90;
        }else if(minutos > 30 && minutos <=45){
            anguloInicial = 90;
            anguloFinal = 90;
        }else if(minutos>45 && minutos <=60){
            anguloInicial = 180;
            anguloFinal = 90;
        }

        canvas.drawArc (primeiroQuadrante, anguloInicial, anguloFinal, true, quadranteCor);

    }

    public void setTime(int hora, int minuto){
        this.horaPonteiro = hora;
        this.minutoPonteiro = minuto;
    }



}
