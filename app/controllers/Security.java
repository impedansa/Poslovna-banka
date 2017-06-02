package controllers;

import org.mindrot.jbcrypt.BCrypt;

import models.Zaposleni;

public class Security extends Secure.Security{
	
	static boolean authenticate(String username, String password) {
        Zaposleni zaposlen = Zaposleni.find("byKorisnickoIme", username).first();
        String lozinka = zaposlen.getLozinka();
        boolean password_verified = false;
        
		if(null == lozinka)
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

		password_verified = BCrypt.checkpw(password, lozinka);
		
		if(zaposlen != null && password_verified) {
			session.put("user", (zaposlen.id).toString());
		}
        
		return zaposlen != null && password_verified;
    }

}
