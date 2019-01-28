package com.example.akash.appliedandroidv2;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.provider.ContactsContract;
import android.util.EventLog;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.akash.appliedandroidv2.Utilities.Utilities;

public class GLRendererView extends GLSurfaceView{
    private final GLRenderer mRenderer;
    private Utilities utilities;
    public GLRendererView(Context context){
        super(context);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        utilities = new Utilities(context);

        mRenderer = new GLRenderer(context);


        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = (int)event.getRawX();
        float y = (int)event.getRawY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
               // mRenderer.setXandY(x,y);
               if(mRenderer.checkClicks(event)) {
                  // Toast m = Toast.makeText(this.getContext(), "Clicked", Toast.LENGTH_SHORT);
                  // m.show();
               }else{
                //   Toast m = Toast.makeText(this.getContext(), "Not Clicked", Toast.LENGTH_SHORT);
                  // m.show();
               }
               break;
              // invalidate();
           case MotionEvent.ACTION_UP:
                mRenderer.actionUpChanges(event);
                //Toast m = Toast.makeText(this.getContext(), "UPACT", Toast.LENGTH_SHORT);
               // m.show();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mRenderer.checkMovement(event)){

                }
                break;
           // case MotionEvent.
        }
        //invalidate();
        return true;
    }
}
