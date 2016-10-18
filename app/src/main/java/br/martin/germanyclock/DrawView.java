package br.martin.germanyclock;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.ArcShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.R.attr.rotation;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public  class DrawView extends SurfaceView implements SurfaceHolder.Callback  {

    private static final String TAG = DrawView.class.getSimpleName();

    private MainThread thread;

    private int horaPonteiro;
    private int minutoPonteiro;


    public DrawView(Context context,AttributeSet attr) {
        super(context,attr);

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
        //movePonteiroHora++;
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        Paint paintRect = new Paint();
        Rect ponteiroMinutos = new Rect(getWidth()/2-20, 100 , getWidth()/2+20, getHeight()/2);
        Rect ponteiroHoras = new Rect(getWidth()/2-20, 200 , getWidth()/2+20, getHeight()/2);

        paint.setARGB(100, 128, 128 ,100);
        paintRect.setARGB(100, 0, 128 ,100);


        //Ponteiro dos minutos
        canvas.save();
        canvas.rotate(6*minutoPonteiro,ponteiroMinutos.centerX(),ponteiroMinutos.bottom);
        canvas.drawRect(ponteiroMinutos, paintRect);
        canvas.restore();

        //Ponteiro das horas
        canvas.save();
        canvas.rotate(30*horaPonteiro,ponteiroHoras.centerX(),ponteiroHoras.bottom);
        canvas.drawRect(ponteiroHoras, paintRect);
        canvas.restore();

        //radio = 350
        canvas.drawCircle(getWidth()/2,getHeight()/2,250,paint);

    }

    public void setHour(int hora, int minuto){
        this.horaPonteiro = hora;
        this.minutoPonteiro = minuto;
    }

}
