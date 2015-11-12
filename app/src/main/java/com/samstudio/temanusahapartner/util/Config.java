package com.samstudio.temanusahapartner.util;

/**
 * Created by aneh on 8/14/2014.
 */
public interface Config {
    // used to share GCM regId with application server - using php app server
    static final String APP_SERVER_URL = "http://192.168.253.46/gcm/gcmgcm.php";
    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "767072238494";
    static final String MESSAGE_KEY = "msg";
}
