package com.dastanapps.dastanlib.social;

import android.os.Bundle;


public interface ISocialResponse {

	String PROVIDER="provider";
	String NAME="name";
	String ID="id";
	String EMAIL_ID="email_id";
    String ACCESS_TOKEN ="access_token";
    String PROFILE_PIC="profile_pic";
    String FIRST_NAME="first_name";
    String LAST_NAME="last_name";
    String LINK="link";
    String GENDER="gender";

    /**
	 * Handling Social Response Facebook,Twitter,LinkedIn and then hiting server
	 * with social email or username to server
	 */
	void socialResponse(Bundle b);

	/**
	 * Stop Cancel
	 */
	void stopLoading();

	/**
	 * Error
	 */
	void error(String error);
}
