package com.iit.luau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XMLRequest {

    // /////////////////////////////////////////////////////////////////////////
    // Grab XML
    // /////////////////////////////////////////////////////////////////////////
    public static String getXML() throws XmlPullParserException, IOException 
    {
        // /////////////////////////////////////////////////////////////////////////
        // Build String Data
        // /////////////////////////////////////////////////////////////////////////
        URL url = new URL("https://gdata.youtube.com/feeds/api/standardfeeds/top_rated");        
        
        // /////////////////////////////////////////////////////////////////////////
        // Setup XML Parsing
        // /////////////////////////////////////////////////////////////////////////        
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        Log.d("TAG", "created Parser");
        xpp.setInput(url.openStream(), "utf-8");
        Log.d("TAG", "created streamFeed");
        int eventType = xpp.getEventType();
        Log.d("TAG", "Set Event");

        // /////////////////////////////////////////////////////////////////////////
        // XML Parsing Magic
        // /////////////////////////////////////////////////////////////////////////
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
        	Log.d("TAG", "stepped into while");
            if (eventType == XmlPullParser.START_DOCUMENT) 
            {
                Log.d("TAG", "Start Document");
            } else if (eventType == XmlPullParser.END_DOCUMENT) 
            {
                Log.d("TAG", "End Document");
            } else if (eventType == XmlPullParser.START_TAG) 
            {
                Log.d("TAG", xpp.getName());
            } else if (eventType == XmlPullParser.END_TAG) 
            {
                System.out.println("End tag " + xpp.getName());
            } else if (eventType == XmlPullParser.TEXT) 
            {
                System.out.println("Text " + xpp.getText());
            }
            eventType = xpp.next();
        }
        return "todo";
    }
}
