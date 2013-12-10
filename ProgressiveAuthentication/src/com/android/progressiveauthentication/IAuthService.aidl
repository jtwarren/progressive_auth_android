package com.android.progressiveauthentication;

interface IAuthService {
	/*
	* public interface
	*/
	boolean authorized(String packageName);	

	/*
	* private interface
	*/
	void setAuthLevel(int level);
}