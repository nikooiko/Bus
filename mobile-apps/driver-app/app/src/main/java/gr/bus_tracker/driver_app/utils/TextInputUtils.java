package gr.bus_tracker.driver_app.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nick on 4/15/17.
 */

public abstract class TextInputUtils {
	public static boolean isValidEmail(String string){
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	public static boolean isValidPassword(String string){
		return true; // TODO password pattern match.
	}

	public static boolean isNullOrEmpty(String string){
		return TextUtils.isEmpty(string);
	}

	public static boolean isNumeric(String string){
		return TextUtils.isDigitsOnly(string);
	}

	public static boolean isValidUsername(String username) {
		return true; // TODO username pattern match.
	}
}
