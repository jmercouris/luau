package com.iit.luau;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;


///////////////////////////////////////////////////////////////////////////
// Main Activity
///////////////////////////////////////////////////////////////////////////
public class MainActivity extends Activity 
{
	private SurfaceView preview=null;
    private SurfaceHolder previewHolder=null;
    private Camera camera=null;
    private boolean inPreview=false;
    private boolean cameraConfigured=false;

    @SuppressLint({ "NewApi", "NewApi", "NewApi" }) @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
          }

		// /////////////////////////////////////////////////////////////////////////
		// Capture XML
		// /////////////////////////////////////////////////////////////////////////
        try 
        {
        XMLRequest.getXML();	
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
		// /////////////////////////////////////////////////////////////////////////
		// Create SurfaceViewd
		// /////////////////////////////////////////////////////////////////////////
        
        preview=(SurfaceView)findViewById(R.id.preview);
        previewHolder=preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

	// /////////////////////////////////////////////////////////////////////////
	// On Create
	// /////////////////////////////////////////////////////////////////////////
         
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	// /////////////////////////////////////////////////////////////////////////
	// On Resume
	// /////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() 
    {
      super.onResume();
      camera=Camera.open();
      startPreview();
    }

	// /////////////////////////////////////////////////////////////////////////
	// On Pause
	// /////////////////////////////////////////////////////////////////////////    
    @Override
    public void onPause() 
    {
      if (inPreview) 
      {
        camera.stopPreview();
      }
      camera.release();
      camera=null;
      inPreview=false;
      super.onPause();
    }
    

	// /////////////////////////////////////////////////////////////////////////
	// Grab Preview Size
	// /////////////////////////////////////////////////////////////////////////
    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) 
    {
      Camera.Size result=null;  
      for (Camera.Size size : parameters.getSupportedPreviewSizes()) 
      {
        if (size.width<=width && size.height<=height) 
        {
          if (result==null) 
          {
            result=size;
          }
          else 
          {
            int resultArea=result.width*result.height;
            int newArea=size.width*size.height;
            
            if (newArea>resultArea) 
            {
              result=size;
            }
          }
        }
      }
      
      return(result);
    }
    

	// /////////////////////////////////////////////////////////////////////////
	// Initialize Preview
	// /////////////////////////////////////////////////////////////////////////
    private void initPreview(int width, int height) 
    {
      if (camera!=null && previewHolder.getSurface()!=null) 
      {
        try 
        {
          camera.setPreviewDisplay(previewHolder);
        }
        catch (Throwable t) 
        {}

        if (!cameraConfigured) 
        {
          Camera.Parameters parameters=camera.getParameters();
          Camera.Size size=getBestPreviewSize(width, height, parameters);
          
          if (size!=null) 
          {
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
            cameraConfigured=true;
          }
        }
      }
    }
    

	// /////////////////////////////////////////////////////////////////////////
	// Begin Preview
	// /////////////////////////////////////////////////////////////////////////
    private void startPreview() 
    {
      if (cameraConfigured && camera!=null) 
      {
        camera.startPreview();
        inPreview=true;
      }
    }
    
    SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() 
    {

    // /////////////////////////////////////////////////////////////////////////
    // Surface Created
    // /////////////////////////////////////////////////////////////////////////
      public void surfaceCreated(SurfaceHolder holder) 
      {
    	camera.setDisplayOrientation(90);
        // no-op -- wait until surfaceChanged()
      }

  	// /////////////////////////////////////////////////////////////////////////
  	// Surface Changed
  	// /////////////////////////////////////////////////////////////////////////
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
      {
        initPreview(width, height);
        startPreview();
      }

  	// /////////////////////////////////////////////////////////////////////////
  	// Surface Destroyed
  	// /////////////////////////////////////////////////////////////////////////
      public void surfaceDestroyed(SurfaceHolder holder) 
      {}
    };
}
