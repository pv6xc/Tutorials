package com.umkc.googleChart;




import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnSeekBarChangeListener {
	 private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
	    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
	    private static final LatLng PERTH = new LatLng(-31.95285, 115.85734);

	    private static final LatLng LHR = new LatLng(51.471547, -0.460052);
	    private static final LatLng LAX = new LatLng(33.936524, -118.377686);
	    private static final LatLng JFK = new LatLng(40.641051, -73.777485);
	    private static final LatLng AKL = new LatLng(-37.006254, 174.783018);
	    
	    private static final LatLng WalkStart = new LatLng(39.022241,-94.567823);
	    private static final LatLng WalkEnd = new LatLng(39.030775,-94.564304); 
	    private static final LatLng RunStart = new LatLng(39.044376,-94.555893);
	    private static final LatLng RunEnd = new LatLng(39.031909,-94.552116);
	   
	    private static final LatLng CyclingStart = new LatLng(39.023374,-94.576664);
	    private static final LatLng Cycling1 = new LatLng(39.028575,-94.571943);
	    private static final LatLng Cycling2 = new LatLng(39.027908,-94.563532);
	    private static final LatLng Cycling3 = new LatLng(39.030575,-94.556665);
	    private static final LatLng CyclingEnd = new LatLng(39.038976,-94.562159);
	    private static final int WIDTH_MAX = 50;
	    private static final int HUE_MAX = 360;
	    private static final int ALPHA_MAX = 255;

	    private GoogleMap mMap;
	    private Polyline mMutablePolyline;
	    //private SeekBar mColorBar;
	    //private SeekBar mAlphaBar;
	    //private SeekBar mWidthBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
        mColorBar.setMax(HUE_MAX);
        mColorBar.setProgress(0);

        mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        mAlphaBar.setMax(ALPHA_MAX);
        mAlphaBar.setProgress(255);

        mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
        mWidthBar.setMax(WIDTH_MAX);
        mWidthBar.setProgress(10);*/
        setUpMapIfNeeded();
        WebView webview = (WebView) findViewById(R.id.webView1);
        String path = "raw/Data.txt";
        File text = new File(path);
        Scanner scan;
        
        String data = "";
        
        InputStream is = getResources().openRawResource(R.raw.data);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
 
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] vals = line.split("\t");
				
				data = data + "['"+vals[0]+"',"+vals[1]+","+vals[2]+","+vals[3]+"], ";
				System.out.println("data " + data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        String content = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                
                +"          ['Genre', 'Walking', 'Running', 'Sitting', 'Standing','Cycling'],"
                + "['9AM', 5, 24, 20, 32, 18],"
                + "['NOON', 6, 22, 23, 30, 16],"
                + "['9PM', 28, 9, 29, 30, 2]"
                + "        ]);"
                + "        var options = {"
                
				+ "width: 600,"
				+ "height: 400,"
				+ "legend: { position: 'top', maxLines: 3 },"
				+ "bar: { groupWidth: '75%' },"
				+ "isStacked: true,"
				+ "};"
                + "        var chart = new google.visualization.BarChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: 1500px; height: 1200px;\"></div>"
                + "       <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
                + "  </body>" + "</html>";
 
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setInitialScale(150);
        webview.requestFocusFromTouch();
        webview.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
        //webview.loadUrl("file:///android_asset/Code.html"); // Can be used in this way too.
        
        
        
        
        
        
       
    }
 
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            	Log.i("map started", "yes");
                setUpMap();
            }
        }
    }
    private void setUpMap() {

        // A simple polyline with the default options from Melbourne-Adelaide-Perth.
        mMap.addPolyline((new PolylineOptions())
                .add(MELBOURNE, ADELAIDE, PERTH));

        // A geodesic polyline that goes around the world.
        /*mMap.addPolyline((new PolylineOptions())
                .add(LHR, AKL, LAX, JFK, LHR)
                .width(5)
                .color(Color.BLUE)
                .geodesic(true));*/
        mMap.addPolyline((new PolylineOptions())
        		.add(CyclingStart,Cycling1,Cycling2,Cycling3,CyclingEnd)
        		.width(10)
        		.color(Color.MAGENTA)
        		.geodesic(true));
        mMap.addPolyline((new PolylineOptions())
        		.add(RunStart,RunEnd)
        		.width(10)
        		.color(Color.RED)
        		.geodesic(true));
        mMap.addPolyline((new PolylineOptions())
        		.add(WalkStart,WalkEnd)
        		.width(10)
        		.color(Color.BLUE)
        		.geodesic(true));
        mMap.addMarker((new MarkerOptions())
        		.position(new LatLng(39.034976,-94.545937))
        		.title("Sitting")
        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        		);
        mMap.addMarker((new MarkerOptions())
        		.position(new LatLng(39.025041,-94.559412))
        		.title("Sitting")
        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        		);
        
        
        // Rectangle centered at Sydney.  This polyline will be mutable.
        int radius = 5;
        PolylineOptions options = new PolylineOptions()
                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude + radius))
                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude - radius))
                .add(new LatLng(SYDNEY.latitude - radius, SYDNEY.longitude - radius))
                .add(new LatLng(SYDNEY.latitude - radius, SYDNEY.longitude + radius))
                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude + radius));
        //int color = Color.HSVToColor(
          //      mAlphaBar.getProgress(), new float[] {mColorBar.getProgress(), 1, 1});
        mMutablePolyline = mMap.addPolyline(options
                .color(getTitleColor())
                .width(WIDTH_MAX)
                );
        //.color(color)
        //.width(mWidthBar.getProgress())
        //mColorBar.setOnSeekBarChangeListener(this);
        //mAlphaBar.setOnSeekBarChangeListener(this);
        //mWidthBar.setOnSeekBarChangeListener(this);

        // Move the map so that it is centered on the mutable polyline.
        mMap.moveCamera(CameraUpdateFactory
        		
        		.newLatLng(WalkStart));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        /*switch (item.getItemId()) {
        case R.id.action_to_image:
            Intent intent = new Intent(MainActivity.this, GoogleImageGraphActivity.class);
            startActivity(intent);
            return true;
            
 
        default:
            return super.onOptionsItemSelected(item);
        }*/
    	return true;
    }


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (mMutablePolyline == null) {
            return;
        }

        //if (seekBar == mColorBar) {
            mMutablePolyline.setColor(Color.HSVToColor(
                    Color.alpha(mMutablePolyline.getColor()), new float[] {progress, 1, 1}));
        //} else if (seekBar == mAlphaBar) {
          /*  float[] prevHSV = new float[3];
            Color.colorToHSV(mMutablePolyline.getColor(), prevHSV);
            mMutablePolyline.setColor(Color.HSVToColor(progress, prevHSV));*/
        //} else if (seekBar == mWidthBar) {
            mMutablePolyline.setWidth(progress);
        //}
		
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


}