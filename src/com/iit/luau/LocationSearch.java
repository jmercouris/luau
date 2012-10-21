package com.iit.luau;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Address;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;

public class LocationSearch {
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";

	private static final boolean PRINT_AS_STRING = false;

	private static final String API_KEY = "AIzaSyCYD61MU2-FtC8T0Ns8dqFiyCJWMT4EGTo";
	
	private static final HttpTransport transport = new ApacheHttpTransport();

	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {

		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("Google-Places-DemoApp");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
			}
		});
	}

	public String performSearch(Context c, int type) throws Exception {//use enum possibly
		try {
			double lat = MyLocationListener.latitude;
			double lon = MyLocationListener.longitude;
			if(type==1){
				//text lookup
				Geocoder geo = new Geocoder(c, Locale.US);
				List<Address> address = geo.getFromLocationName("lookup text", 1);//pick text from lookup box
				if(address==null || address.isEmpty()){
					//print something wrong
				}
				else{
					lat = address.get(0).getLatitude();
					lon = address.get(0).getLongitude();
				}
			}
			if(type==2){
				//compass
				float[] R=null, values = null;
				SensorManager.getOrientation(R,values);
			}
			
			//show error, please retype address
			System.out.println("Perform Search ....");
			System.out.println("-------------------");
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put(
					"location",
					lat + ","
							+ lon);
			request.getUrl().put("radius", 1);
			request.getUrl().put("sensor", "true");

			if (PRINT_AS_STRING) {
				System.out.println(request.execute().parseAsString());
			} else {

				PlacesList places = request.execute().parseAs(PlacesList.class);
				System.out.println("STATUS = " + places.status);
				for (Place place : places.results) {
					System.out.print(place);
					return place.name;					
				}
			}

		} catch (HttpResponseException e) {
			System.err.println(e.getResponse().parseAsString());
			throw e;
		}
		return "";
	}
}
