package controllers;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import models.Drzava;
import models.Zaposleni;
import play.data.validation.Required;
import play.mvc.Controller;

public class Zaposlenii extends Controller{
	
	public static void show(String mode, Long s){
		List<Zaposleni> zaposleni = Zaposleni.findAll();
		if (mode == null || mode.equals(""))
			mode="show";
		render(zaposleni, mode, s);
	}
	
	public static void create(String korisnickoIme, String lozinka) {
		String hashedPassword = BCrypt.hashpw(lozinka, BCrypt.gensalt(12));
		Zaposleni z = new Zaposleni(korisnickoIme, hashedPassword);
		z.save();
		show("add", z.id);
	}
	
	public static void filter(String korisnickoIme, String lozinka) {
		List<Zaposleni> zaposleni = Zaposleni.find("byKorisnickoImeLike", "%"+korisnickoIme+"%").fetch();
		String mode = "show";
	}
	
	public static void delete(Long id) {
		Long s = null;
		List<Zaposleni> zaposleni = Zaposleni.findAll();
		for(int i=0; i< zaposleni.size(); i++) {
			if(zaposleni.get(i).getId().equals(id)){
				s = zaposleni.get(i-1).getId();
			}
		}
		Zaposleni z = Zaposleni.findById(id);
		z.delete();
		show("show",s);
	} 
	
	public static void editPassword(){
		
	}
}
