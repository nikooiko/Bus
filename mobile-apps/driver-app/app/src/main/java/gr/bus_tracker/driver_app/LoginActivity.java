package gr.bus_tracker.driver_app;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.strongloop.android.loopback.AccessToken;

import java.util.ArrayList;

import gr.bus_tracker.driver_app.models.AppUser;
import gr.bus_tracker.driver_app.models.AppUserRepository;

public class LoginActivity extends AppCompatActivity {
	// Resources
	private String DRIVER_ROLE;
	private String TOAST_MESSAGE;

	// Properties
	private AppUserRepository appUserRepo;
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private TextView tvRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Resources res = getResources();
		// get needed models
		final DriverApplication app = (DriverApplication)getApplication();
		appUserRepo = app.getAppUserRepository();

		// get all needed resources
		DRIVER_ROLE = res.getString(R.string.driverRole);
		TOAST_MESSAGE = res.getString(R.string.toastMessage);

		// init properties
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvRegister = (TextView) findViewById(R.id.tvRegister);

		// check if intent contains username and password
		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		etUsername.setText(username);
		String password = intent.getStringExtra("password");
		etPassword.setText(password);

		// check if intent contains toast message
		String toastMsg = intent.getStringExtra(TOAST_MESSAGE);
		Toast.makeText(LoginActivity.this, toastMsg, Toast.LENGTH_SHORT).show();

		setupGotoRegister();
		setupLogin();
	}

	private void setupLogin() {
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginActivity.this.sendLoginRequest();
			}
		});
	}

	private void setupGotoRegister() {
		tvRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
				LoginActivity.this.startActivity(registerIntent);
			}
		});
	}

	private void sendLoginRequest() {
		// Get input fields
		final String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();
		// Send the request
		appUserRepo.loginUser(username, password, loginCallback(username));
	}

	private AppUserRepository.LoginCallback<AppUser> loginCallback(final String username) {
		return new AppUserRepository.LoginCallback<AppUser>() {
			@Override
			public void onSuccess(AccessToken token, AppUser currentUser) {
				ArrayList<String> roles = (ArrayList<String>) token.getCreationParameters().get("roles");
				currentUser.setRoles(roles);
				if (currentUser.hasRole(DRIVER_ROLE)) {
					Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
					intent.putExtra("username", username);
					LoginActivity.this.startActivity(intent);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					builder.setMessage("Login Failed. You are not a driver!")
							.setNegativeButton("Retry", null)
							.create()
							.show();
				}
			}

			@Override
			public void onError(Throwable t) {
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setMessage("Login Failed, Throwable t: " + t)
						.setNegativeButton("Retry", null)
						.create()
						.show();
			}
		};
	}
}