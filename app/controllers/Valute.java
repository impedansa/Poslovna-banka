package controllers;

import java.util.List;

import models.Valuta;
import play.data.validation.Required;
import play.mvc.Controller;

public class Valute extends Controller{
	public static void show(){
		List<Valuta> valute = Valuta.findAll();
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
		render(valute);
	}
	
	public static void create(@Required String sifraValute, @Required String nazivValute) {
		validation.maxSize(sifraValute, 3);
		validation.minSize(sifraValute, 3);
		validation.maxSize(nazivValute, 30);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Valuta valuta = new Valuta(sifraValute, nazivValute);
		valuta.save();
		
		session.put("mode", "add");
 		session.put("s", valuta.id);
		show();
	}
	
	public static void edit(Long id,@Required String sifraValute,@Required String nazivValute) {
		validation.maxSize(sifraValute, 3);
		validation.minSize(sifraValute, 3);
		validation.maxSize(nazivValute, 30);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id!=null){
			Valuta valuta = Valuta.findById(id);
			valuta.sifraValute = sifraValute;
			valuta.nazivValute = nazivValute;
			valuta.save();
			s = id;
		} else if (sifraValute != null) {
			List<Valuta> valute = Valuta.findAll();
			for(Valuta valuta: valute) {
				if(sifraValute.equals(valuta.sifraValute)){
					valuta.sifraValute = sifraValute;
					valuta.nazivValute = nazivValute;
					valuta.save();
					s = valuta.id;
				}
			}

		}
		
		session.put("mode", "edit");
 		session.put("s", s);
		show();
	}
	
	public static void filter(String sifraValute, String nazivValute) {
		List<Valuta> valute = Valuta.find("bySifraValuteLikeAndNazivValuteLike", "%"+sifraValute+"%", "%"+nazivValute+"%" ).fetch();
		session.put("mode", "edit");
 		session.put("s", null);
		renderTemplate("Valute/show.html", valute);
	}
	
	public static void delete(Long id) {
		Long s = null;
		List<Valuta> valute = Valuta.findAll();
		for(int i=0; i< valute.size(); i++) {
			if(valute.get(i).getId().equals(id)){
				s = valute.get(i-1).getId();
			}
		}
		Valuta valuta = Valuta.findById(id);
		valuta.delete(); 
		
		session.put("mode", "edit");
		session.put("s", s);
		show();
	}
}
