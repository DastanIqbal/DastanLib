package com.dastanapps.dastanlib.social;

public interface ISMessageResponse {

	void messageResponse(String provder, Integer statuscode);

	void error(String error);
}
