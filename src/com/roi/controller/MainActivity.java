package com.roi.controller;

import java.util.ArrayList;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.roi.model.AppDAO;
import com.roi.model.Marker;
import com.roi.model.MarkerPosition;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private LayoutInflater inflater;
	private RelativeLayout mainView;
	private EditText markerDescription;
	
	public static String userName;
	public final static String USER_NAME = "user name";
	
	private IntentIntegrator integrator;
	
	private AppDAO appDAO;
	private Marker selectedMarker;
	
	private final static MarkerPosition[] markersPosition = new MarkerPosition[10];
	
	private final static String[] BARCODES = {"shenkar_qr_code_05.0","shenkar_qr_code_05.1","shenkar_qr_code_05.2","shenkar_qr_code_05.3","shenkar_qr_code_05.4",
											  "shenkar_qr_code_05.5","shenkar_qr_code_05.6","shenkar_qr_code_05.7","shenkar_qr_code_05.8","shenkar_qr_code_05.9"};
	
	private final static float[] X_POSITIONS = {35,141,194,269,303,398,453,406,243,47};
	
	private final static float[] Y_POSITIONS = {243,229,182,190,246,241,181,310,298,300};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		userName = this.getIntent().getStringExtra(USER_NAME);
		integrator = new IntentIntegrator(this);
		appDAO = AppDAO.getInstance(this);
		
		createMarkersPosition();
		
		inflater = LayoutInflater.from(this);
		mainView = (RelativeLayout)inflater.inflate(R.layout.activity_main, null);
		
		setContentView(mainView);
		
		markerDescription = (EditText) findViewById(R.id.markerDescription);
		
		showMarkersOnMap();
		showTapPositions();
		
		Button changeButton = (Button) findViewById(R.id.changeButton);
		
		changeButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
				if (selectedMarker == null) {
					Toast.makeText(MainActivity.this, "Please select Marker to change", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (!selectedMarker.getCreatedByUser().equals(userName)) {
					Toast.makeText(MainActivity.this, "You cannot modify marker's description that created by another user", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String newDescription = markerDescription.getText().toString();
				
				if (newDescription.isEmpty()) {
					Toast.makeText(MainActivity.this, "Missing description", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (newDescription.equals(selectedMarker.getDescription())) {
					Toast.makeText(MainActivity.this, "Same description entered", Toast.LENGTH_SHORT).show();
					return;
				}
				
				selectedMarker.setDescription(newDescription);
				appDAO.updateMarker(selectedMarker);
				
				Toast.makeText(MainActivity.this, "Description has changed to: " + newDescription, Toast.LENGTH_SHORT).show();
			}
		});
		
		/*Button removeButton = (Button) findViewById(R.id.removeButton);
		
		removeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (selectedMarker == null) {
					Toast.makeText(MainActivity.this, "Please select Marker to delete", Toast.LENGTH_SHORT).show();
					return;
				}
				
				mainView.removeView(selectedMarker.getMarkerImage());
				mapMarkers.remove(selectedMarker);
				markerDescription.setText("");
				
				Toast.makeText(MainActivity.this, "Marker has been removed", Toast.LENGTH_SHORT).show();
				
				selectedMarker = null;
			}
		});*/
		
		Button scanButton = (Button) findViewById(R.id.scanButton);
		
		scanButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				integrator.initiateScan();
			}
		});
		
		
	}
	
	private void showMarkersOnMap() {
		
		ArrayList<Marker> markers = appDAO.getAllMarkers();
		
		for (Marker marker : markers) {
			
			Log.i("Marker", marker.toString());
			
			if (marker.getIsShown().equals("yes")) {
				setMarkerOnMap(marker);
			}
		}
	}
	
	private void showTapPositions() {
		
		ImageView map = (ImageView) findViewById(R.id.map);
		
		GestureDetector.OnGestureListener mapGdListener = new GestureDetector.SimpleOnGestureListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				
				Log.i("Position", "x: " + e.getX() + " y: " + e.getY());
				
				return false;
			}
		};
		
		final GestureDetector mapGd = new GestureDetector(MainActivity.this, mapGdListener);
		
		map.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				mapGd.onTouchEvent(event);
				return true;
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		
		if (scanResult == null) {
			Toast.makeText(MainActivity.this, "Scan failed", Toast.LENGTH_SHORT).show();
			return;
		}
			
		final String result = scanResult.getContents();
		Marker markerResult = appDAO.getMarkerByBarcode(result);
		
		if (markerResult == null) {
			
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Marker Description");
			alert.setMessage("Please enter where you are:");

			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
				@SuppressLint("NewApi")
				public void onClick(DialogInterface dialog, int whichButton) {
					
					  final String value = input.getText().toString();
					  
					  if (value.isEmpty()) {
						  Toast.makeText(MainActivity.this, "Missing description", Toast.LENGTH_SHORT).show();
						  return;
					  }
					  
					  Marker marker = new Marker(result, userName, value, getXpos(result), getYpos(result), "yes");
					  
					  appDAO.addMarker(marker);
					  
					  setMarkerOnMap(marker);
					  
					  markerDescription.setText(marker.getDescription());
					  selectedMarker = marker;
				}
			});
			
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
			    public void onClick(DialogInterface dialog, int whichButton) {
			     	 return;
			    }
			});

			alert.show();
		}
		else {
			
			if (markerResult.getIsShown().equals("yes")) {
				Toast.makeText(MainActivity.this, "Marker is already shown", Toast.LENGTH_SHORT).show();
				return;
			}
			
			markerResult.setIsShown("yes");
			appDAO.updateMarker(markerResult);
			
			setMarkerOnMap(markerResult);
			
			markerDescription.setText(markerResult.getDescription());
			selectedMarker = markerResult;
		}
	}	
	
	private float getXpos(String barcode) {
		
		for (int i = 0; i < markersPosition.length; ++i) {
			
			if (markersPosition[i].getBarcode().equals(barcode)) {
				return markersPosition[i].getxPos();
			}
		}
		
		return -1;
	}
	
	private float getYpos(String barcode) {
		
		for (int i = 0; i < markersPosition.length; ++i) {
			
			if (markersPosition[i].getBarcode().equals(barcode)) {
				return markersPosition[i].getyPos();
			}
		}
		
		return -1;
	}

	private void createMarkersPosition() {
		
		for (int i = 0; i < BARCODES.length; ++i) {
			Log.i("X_POSITION", String.valueOf(X_POSITIONS[i]));
			markersPosition[i] = new MarkerPosition(BARCODES[i], X_POSITIONS[i], Y_POSITIONS[i]);
		}
	}
	
	// MotionEvent e.getX() - Float.parseFloat("30")
	@SuppressLint("NewApi")
	private void setMarkerOnMap(final Marker marker) {
		
		  final ImageButton ib = new ImageButton(MainActivity.this);
	  	  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
	   	  ib.setImageResource(R.drawable.marker);
		  ib.setLayoutParams(lp);
		  ib.setX(marker.getxPos() - Float.parseFloat("30"));
		  ib.setY(marker.getyPos() - Float.parseFloat("30"));
		  
		  GestureDetector.OnGestureListener markerDescriptionGdListener = new GestureDetector.SimpleOnGestureListener() {

				@SuppressLint("NewApi")
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					
					for (Marker currMarker : appDAO.getAllMarkers()) {
						if(currMarker.getBarcode().equals(marker.getBarcode())) {
							selectedMarker = currMarker;
						}
					}
					
					markerDescription.setText(selectedMarker.getDescription());
					
					return false;
				}
			};
			
			final GestureDetector markerDescriptionGd = new GestureDetector(MainActivity.this, markerDescriptionGdListener);
			
			ib.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					markerDescriptionGd.onTouchEvent(event);
					return true;
				}
			});
			
		  mainView.addView(ib);
	}
}
