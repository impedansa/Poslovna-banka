package controllers;

import java.util.List;

import models.Drzava;
import models.NaseljenoMesto;
import play.data.validation.Required;
import play.mvc.Controller;

public class Drzave extends Controller{
	public static void show(String mode, Long s){
		List<Drzava> drzave = Drzava.findAll();
		if (mode == null || mode.equals(""))
			mode="edit";
		render(drzave, mode, s);
	}
	
	public static void create(@Required String oznaka, @Required String naziv) {
		validation.maxSize(oznaka, 3);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          show("add",null);
	    }
		Drzava d = new Drzava(oznaka, naziv);
		d.save();
		show("add", d.id);
	}
	
	public static void edit(Long id,@Required String oznaka,@Required String naziv) {
		validation.maxSize(oznaka, 3);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          show("",null);
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
		String mode = "edit";
		show(mode, s);
	}
	
	public static void filter(String oznaka, String naziv) {
		List<Drzava> drzave = Drzava.find("byOznakaLikeAndNazivLike", "%"+oznaka+"%", "%"+naziv+"%" ).fetch();
		String mode = "edit";
		renderTemplate("Drzave/show.html", mode, drzave);
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
		show("edit",s);
	} 
}
