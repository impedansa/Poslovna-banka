package controllers;

import models.Zaposleni;

import org.mindrot.jbcrypt.BCrypt;

import play.Logger;

public class Security extends Secure.Security{
	
	static boolean authenticate(String username, String password) {
		
		Logger.info("Pokusaj logovanja sa IP adrese: "+ Logovi.getClientIp());
		
        Zaposleni zaposlen = Zaposleni.find("byKorisnickoIme", username).first();
        String lozinka = null;
        
        if(zaposlen != null) {
        	lozinka = zaposlen.getLozinka();
        } else {
        	return false;
        }
        boolean password_verified = false;
        
		if(null == lozinka)
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

		password_verified = BCrypt.checkpw(password, lozinka);
		
		if(zaposlen != null && password_verified) {
			session.put("user", (zaposlen.id).toString());
			Logger.info("Ulogovan zaposleni sa ID-jem: "+zaposlen.getId()+" sa IP adrese: "+ Logovi.getClientIp());
		}
        
		return zaposlen != null && password_verified;
    }
	

}
