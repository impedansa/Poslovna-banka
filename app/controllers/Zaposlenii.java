package controllers;

import java.util.List;

import models.Zaposleni;

import org.mindrot.jbcrypt.BCrypt;

import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;

@With(Deadbolt.class)
public class Zaposlenii extends Controller{
	
	public Zaposlenii() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Restrictions(@Restrict("zaposleni.view"))
	public static void show(String mode, Long s){
		List<Zaposleni> zaposleni = Zaposleni.findAll();
		if (mode == null || mode.equals(""))
			mode="show";
		render(zaposleni, mode, s);
	}
	
	@Restrictions(@Restrict("zaposleni.create"))
	public static void create(@Required String korisnickoIme,@Required String lozinka) {
		if(validation.hasErrors()) {
	          validation.keep(); 
	          renderTemplate("Zaposlenii/show.html");
	    }
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
	
	public static void editPassword(@Required String lozinka,@Required String ponovljenaLozinka,@Required String novaLozinka){
		validation.equals(lozinka, ponovljenaLozinka);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          renderTemplate("Zaposlenii/promenaLozinke.html");
	    }
		boolean password_verified = false;
		String hashedNewPassword = BCrypt.hashpw(novaLozinka, BCrypt.gensalt(12));
		List<Zaposleni> zaposleni = Zaposleni.findAll();
		for(Zaposleni z: zaposleni) {
			if(password_verified = BCrypt.checkpw(lozinka, z.getLozinka())){
				z.korisnickoIme = z.korisnickoIme;
				z.lozinka = hashedNewPassword;
				z.save();
				flash.remove("error");
				flash.put("success", "true");
				renderTemplate("Zaposlenii/promenaLozinke.html");
			}
		}
		flash.remove("success");
		flash.put("error", "true");
		renderTemplate("Zaposlenii/promenaLozinke.html");
		
	}
	
	public static void promenaLozinke() {
		renderTemplate("Zaposlenii/promenaLozinke.html");
	}
}