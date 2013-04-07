package com.roi.controller;

import com.roi.model.AppDAO;
import com.roi.model.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IntroActivity extends Activity {
	
	private static AppDAO appDAO;
	private static String entryUser;
	private static String entryPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_layout);
		
		appDAO = AppDAO.getInstance(this);
		
		final EditText userEntryET = (EditText) findViewById(R.id.userEntryET);
		final EditText passwordEntryET = (EditText) findViewById(R.id.passwordEntryET);
		
		Button signUpButton = (Button) findViewById(R.id.signupButton);
		
		signUpButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				entryUser = userEntryET.getText().toString();
				entryPassword = passwordEntryET.getText().toString();
				signUp();
			}
		});
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				entryUser = userEntryET.getText().toString();
				entryPassword = passwordEntryET.getText().toString();
				login();
			}
		});
	}
	
	@SuppressLint("NewApi")
	private void signUp() {
		
		if (entryUser.isEmpty() || entryPassword.isEmpty()) {
			Toast.makeText(this, "Missing property", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (appDAO.getUserByName(entryUser) != null) {
			Toast.makeText(this, "User is already exists", Toast.LENGTH_SHORT).show();
			return;
		}
		
		appDAO.addUser(new User(entryUser,entryPassword));
		Toast.makeText(this, "Thanks for sighning up! please log in", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressLint("NewApi")
	private void login() {
	
		if (entryUser.isEmpty() || entryPassword.isEmpty()) {
			Toast.makeText(this, "Missing property", Toast.LENGTH_SHORT).show();
			return;
		}
		
		User user = appDAO.getUserByName(entryUser);
		
		if (user == null) {
			Toast.makeText(this, "User " + entryUser + " doesn't exists", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!user.getPassword().equals(entryPassword)) {
			Toast.makeText(this, "Incorrect password. Try again", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.USER_NAME, entryUser);
    	startActivity(intent);
	}
}
