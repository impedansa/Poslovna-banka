package controllers;

import play.mvc.Controller;

public class Logovi extends Controller{
	
	public static String getClientIp() {
		
        String remoteAddr = "";
        if (request.headers.get("x-forwarded-for") != null) {
            remoteAddr = request.headers.get("x-forwarded-for").toString();
        } else {
                remoteAddr = request.remoteAddress;
            }
        return remoteAddr;
    }

}
