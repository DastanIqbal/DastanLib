package com.dastanapps.dastanlib.Social;

public interface ISMessageResponse {

	void messageResponse(String provder, Integer statuscode);

	void error(String error);
}
