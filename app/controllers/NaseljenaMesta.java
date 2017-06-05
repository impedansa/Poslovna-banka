package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Drzava;
import models.NaseljenoMesto;
import play.data.validation.Required;
import play.mvc.Controller;

public class NaseljenaMesta extends Controller {
	
	public static void show(){
		List<Drzava> drzave = Drzava.findAll();
		List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.findAll();
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
		render(naseljenaMesta, drzave);
	}
	
	public static void shownext() {
		String modeN = "";
		String sN = "";
		String id = "0";
		if(params.get("mode") != null) {
			modeN = params.get("mode");
			sN = params.get("s");
			id = params.get("id");
		} else {
			modeN = session.get("mode");
			sN = session.get("s");
			id = session.get("id");
			if(modeN == null || modeN =="") {
				modeN = "locked edit";
			}
		}
		session.put("mode", modeN);
		session.put("s", sN);
		session.put("id", id);
		List<Drzava> drzave = new ArrayList<Drzava>();
		drzave.add(Drzava.findById(Long.parseLong(id)));
		List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.find("byDrzava", drzave.get(0)).fetch();
		renderTemplate("NaseljenaMesta/show.html", naseljenaMesta, drzave, id);
	}
	
	public static void filter(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		List<NaseljenoMesto> naseljenaMesta = filterBasic(oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "edit");
		session.put("s", null);
		renderTemplate("NaseljenaMesta/show.html",naseljenaMesta);
	}
	
	public static void filterNext(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Long id = drzava;
		List<NaseljenoMesto> naseljenaMesta = filterBasic(oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "locked edit");
		session.put("s", null);
		session.put("id", drzava);
		renderTemplate("NaseljenaMesta/show.html",naseljenaMesta, id);
	}
	
	
	public static List<NaseljenoMesto> filterBasic(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Drzava d = Drzava.findById(drzava);
		List<NaseljenoMesto> naseljenaMesta;
		if(postanskiBroj == null){
			naseljenaMesta = NaseljenoMesto.find("byOznakaLikeAndNazivLikeAndDrzava", "%"+oznaka+"%", "%"+naziv+"%", d).fetch();
		} else {
			naseljenaMesta = NaseljenoMesto.find("byOznakaLikeAndNazivLikeAndPostanskiBrojAndDrzava", "%"+oznaka+"%", "%"+naziv+"%", postanskiBroj, d).fetch();
		} 	
		return naseljenaMesta;
	}
	
	public static void create(@Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Long s = createBasic(oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "add");
		session.put("s", s);
		show();
	}
	
	
	public static void createNext(@Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "locked add");
	  		  session.put("s", null);
	  		  session.put("id", drzava);
	          shownext();
	    }
		Long s = createBasic(oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "locked add");
		session.put("s", s);
		session.put("id", drzava);
		shownext();
	}
	
	
	public static Long createBasic(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Drzava d = Drzava.findById(drzava);
		NaseljenoMesto nm = new NaseljenoMesto(oznaka, naziv, postanskiBroj, d);
		nm.save();
		return nm.id;
	}
	
	public static void edit(Long id, @Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "edit");
	  		  session.put("s", null);
	          show();
	    }
		Long s = editBasic(id, oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "edit");
		session.put("s", s);
		show();
	}
	
	public static void editNext(Long id, @Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "locked edit");
	  		  session.put("s", null);
	  		  session.put("id", drzava);
	          shownext();
	    }
		Long s = editBasic(id, oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "locked edit");
		session.put("s", s);
		session.put("id", drzava);
		shownext();
	}
	
	public static Long editBasic(Long id, String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Long s = null;
		if(id != null) {
			NaseljenoMesto nm = NaseljenoMesto.findById(id);
			nm.oznaka = oznaka;
			nm.naziv = naziv;
			nm.postanskiBroj = postanskiBroj;
			Drzava d = Drzava.findById(drzava);
			nm.drzava = d;
			nm.save();
			s = id;
		} else if (oznaka != null){
			List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.findAll();
			for(NaseljenoMesto nm:naseljenaMesta){
				if(oznaka.equals(nm.getOznaka())) {
					nm.oznaka = oznaka;
					nm.naziv = naziv;
					nm.postanskiBroj = postanskiBroj;
					Drzava d = Drzava.findById(drzava);
					nm.drzava = d;
					nm.save();
					s = nm.id;
				}
			}
			
		}
		return s;
		
	}
	
	public static void delete(Long id) {
		Long s = null;
		List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.findAll();
		for(int i=0; i< naseljenaMesta.size(); i++) {
			if(naseljenaMesta.get(i).getId().equals(id)){
				s = naseljenaMesta.get(i-1).getId();
			}
		}
		NaseljenoMesto nm = NaseljenoMesto.findById(id);
		nm.delete();
		String mode = session.get("mode");
		if(mode.equals("locked add")|| mode.equals("locked edit")||mode.equals("locked search")) {
			session.put("mode", "locked edit");
			session.put("s", s);
			session.put("id", nm.getDrzava().getId());
			shownext();
		} else {
			session.put("mode", "edit");
			session.put("s", s);
			show();
		}
	}


}
