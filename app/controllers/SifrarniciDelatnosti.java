package controllers;

import java.util.List;

import models.SifrarnikDelatnosti;
import play.data.validation.Required;
import play.mvc.Controller;

public class SifrarniciDelatnosti extends Controller{
	public static void show(){
		List<SifrarnikDelatnosti> sifrarniciDelatnosti = SifrarnikDelatnosti.findAll();
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
		render(sifrarniciDelatnosti);
	}
	
	public static void create(@Required int sifraDelatnosti, @Required String nazivDelatnosti) {
		checkAuthenticity();
		validation.maxSize(nazivDelatnosti, 60);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		SifrarnikDelatnosti sifrarnikDelatnosti = new SifrarnikDelatnosti(sifraDelatnosti, nazivDelatnosti);
		sifrarnikDelatnosti.save();
		
		session.put("mode", "add");
 		session.put("s", sifrarnikDelatnosti.id);
		show();
	}
	
	public static void edit(Long id,@Required int sifraDelatnosti,@Required String nazivDelatnosti) {
		checkAuthenticity();
		validation.maxSize(nazivDelatnosti, 60);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id!=null){
			SifrarnikDelatnosti sifrarnikDelatnosti = SifrarnikDelatnosti.findById(id);
			sifrarnikDelatnosti.sifraDelatnosti = sifraDelatnosti;
			sifrarnikDelatnosti.nazivDelatnosti = nazivDelatnosti;
			sifrarnikDelatnosti.save();
			s = id;
		} else if (sifraDelatnosti != 0) {
			List<SifrarnikDelatnosti> sifrarniciDelatnosti = SifrarnikDelatnosti.findAll();
			for(SifrarnikDelatnosti sifrarnikDelatnosti: sifrarniciDelatnosti) {
				if(sifraDelatnosti == sifrarnikDelatnosti.sifraDelatnosti){
					sifrarnikDelatnosti.sifraDelatnosti = sifraDelatnosti;
					sifrarnikDelatnosti.nazivDelatnosti = nazivDelatnosti;
					sifrarnikDelatnosti.save();
					s = sifrarnikDelatnosti.id;
				}
			}

		}
		
		session.put("mode", "edit");
 		session.put("s", s);
		show();
	}
	
	public static void filter(Integer sifraDelatnosti, String nazivDelatnosti) {
		List<SifrarnikDelatnosti> sifrarniciDelatnosti;
		if(sifraDelatnosti == null)
			sifrarniciDelatnosti = SifrarnikDelatnosti.find("byNazivDelatnostiLike", "%"+nazivDelatnosti+"%").fetch();
		else
			sifrarniciDelatnosti = SifrarnikDelatnosti.find("bySifraDelatnostiAndNazivDelatnostiLike", sifraDelatnosti, "%"+nazivDelatnosti+"%" ).fetch();
		session.put("mode", "edit");
 		session.put("s", null);
		renderTemplate("SifrarniciDelatnosti/show.html", sifrarniciDelatnosti);
	}
	
	public static void delete(Long id) {
		checkAuthenticity();
		Long s = null;
		List<SifrarnikDelatnosti> sifrarniciDelatnosti = SifrarnikDelatnosti.findAll();
		for(int i=0; i< sifrarniciDelatnosti.size(); i++) {
			if(sifrarniciDelatnosti.get(i).getId().equals(id)){
				s = sifrarniciDelatnosti.get(i-1).getId();
			}
		}
		SifrarnikDelatnosti sifrarnikDelatnosti = SifrarnikDelatnosti.findById(id);
		sifrarnikDelatnosti.delete(); 
		
		session.put("mode", "edit");
		session.put("s", s);
		show();
	}
}
