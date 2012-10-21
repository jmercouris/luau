package com.iit.luau;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private static TextView coordinatesTextView;
    private static ImageButton cameraButton;
    private static ImageButton hereButton;
    private static EditText editTextLookup;
    private static Button lookupButton;
    private LocationSearch searcher;
    final Context c = this;

    @SuppressLint({ "NewApi", "NewApi", "NewApi" }) @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) 
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        searcher = new LocationSearch();
        
        // /////////////////////////////////////////////////////////////////////////
     	// Capture UI Elements
     	// /////////////////////////////////////////////////////////////////////////
        coordinatesTextView = (TextView) findViewById(R.id.coordinates);
        coordinatesTextView.setText("0.1");
        editTextLookup = (EditText) findViewById(R.id.editText1);
        lookupButton = (Button) findViewById(R.id.button1);
        hereButton = (ImageButton) findViewById(R.id.hereButton);
        cameraButton = (ImageButton) findViewById(R.id.CameraButton);
        cameraButton.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				String appendURL = "";
				try{
				appendURL = searcher.performSearch(true);
				}catch(Exception e){}
				CalendarWebView.getAlert(c, "https://www.google.com/calendar/embed?src=jd2g7c4e3fol3k687i9n4nmdt4%40group.calendar.google.com&ctz=America/Chicago" + "&q=" + appendURL, appendURL);
			}
			});
        hereButton.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				String appendURL = "";
				try{
				appendURL = searcher.performSearch(false);
				}catch(Exception e){}
				CalendarWebView.getAlert(c, "https://www.google.com/calendar/embed?src=jd2g7c4e3fol3k687i9n4nmdt4%40group.calendar.google.com&ctz=America/Chicago" + "&q=" + appendURL, "here");
			}
        });
        lookupButton.setOnClickListener(new OnClickListener() 
        {
        	public void onClick(View v)
        	{
        		System.out.print("i'm gettin clicked on");
        		String appendURL = "";
				try{
					String lookup = (String) editTextLookup.getText().toString();
        			appendURL = searcher. performSearch(c, lookup);
				}catch(Exception e){}
				CalendarWebView.getAlert(c, "https://www.google.com/calendar/embed?src=jd2g7c4e3fol3k687i9n4nmdt4%40group.calendar.google.com&ctz=America/Chicago" + "&q=" + appendURL, appendURL);
        	}
        });
			
        // /////////////////////////////////////////////////////////////////////////
     	// Capture Location
     	// /////////////////////////////////////////////////////////////////////////        
		LocationManager mlocManager = null;
		LocationListener mlocListener;
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		mlocListener = new MyLocationListener();
		String prov = mlocManager.getBestProvider(criteria, false);
		mlocManager.requestLocationUpdates(prov, 5000, 0, mlocListener);
		Location temp = MyLocationListener.getLocation(this);
		updateCoordinatesUI(temp);
	

		// /////////////////////////////////////////////////////////////////////////
		// Create SurfaceView
		// /////////////////////////////////////////////////////////////////////////
        preview=(SurfaceView)findViewById(R.id.preview);
        previewHolder=preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
      
    }
    
	// /////////////////////////////////////////////////////////////////////////
	// Update Location UI
	// /////////////////////////////////////////////////////////////////////////
    public static void updateCoordinatesUI(double latitude, double longitude)
    {
    	coordinatesTextView.setText("Coordinates: (" + Math.abs(latitude) + "¡ "+(latitude<0 ? "S" :"N") + " : " + Math.abs(longitude) + "¡"+ (longitude<0 ? "W" :"E")+")");
    }
    
    public static void updateCoordinatesUI(Location l)
    {
    	coordinatesTextView.setText("" + l.getLatitude() + "\\" + l.getLongitude());
    }

    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	// /////////////////////////////////////////////////////////////////////////
	// On Create
	// /////////////////////////////////////////////////////////////////////////   
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	// /////////////////////////////////////////////////////////////////////////
	// On Resume
	// /////////////////////////////////////////////////////////////////////////
    public void onResume() 
    {
      super.onResume();
      camera=Camera.open();
      startPreview();
    }
	// /////////////////////////////////////////////////////////////////////////
	// On Pause
	// /////////////////////////////////////////////////////////////////////////    
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
