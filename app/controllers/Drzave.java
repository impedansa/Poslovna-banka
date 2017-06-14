package controllers;

import java.util.List;

import models.Drzava;
import models.NaseljenoMesto;
import play.data.validation.Required;
import play.mvc.Controller;

public class Drzave extends Controller{
	public static void show(){
		List<Drzava> drzave = Drzava.findAll();
		String mode = "";
		String s = "";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			if(mode == null || mode =="") {
				mode = "edit";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		render(drzave);
	}
	
	public static void create(@Required String oznaka, @Required String naziv) {
		validation.maxSize(oznaka, 3);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Drzava d = new Drzava(oznaka, naziv);
		d.save();
		
		session.put("mode", "add");
 		session.put("s", d.id);
		show();
	}
	
	public static void edit(Long id,@Required String oznaka,@Required String naziv) {
		validation.maxSize(oznaka, 3);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id!=null){
			Drzava d = Drzava.findById(id);
			d.oznaka = oznaka;
			d.naziv = naziv;
			d.save();
			s = id;
		} else if (oznaka != null) {
			List<Drzava> drzave = Drzava.findAll();
			for(Drzava d: drzave) {
				if(oznaka.equals(d.getOznaka())){
					d.oznaka = oznaka;
					d.naziv = naziv;
					d.save();
					s = d.id;
				}
			}

		}
		
		session.put("mode", "edit");
 		session.put("s", s);
		show();
	}
	
	public static void filter(String oznaka, String naziv) {
		List<Drzava> drzave = Drzava.find("byOznakaLikeAndNazivLike", "%"+oznaka+"%", "%"+naziv+"%" ).fetch();
		session.put("mode", "edit");
 		session.put("s", null);
		renderTemplate("Drzave/show.html", drzave);
	}
	
	public static void delete(Long id) {
		Long s = null;
		List<Drzava> drzave = Drzava.findAll();
		for(int i=0; i< drzave.size(); i++) {
			if(drzave.get(i).getId().equals(id)){
				s = drzave.get(i-1).getId();
			}
		}
		Drzava d = Drzava.findById(id);
		d.delete(); 
		
		session.put("mode", "edit");
		session.put("s", s);
		//show();
	}
}
