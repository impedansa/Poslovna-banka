package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Drzava;
import models.NaseljenoMesto;
import play.data.validation.Required;
import play.mvc.Controller;

public class NaseljenaMesta extends Controller {
	
	public static void show(String mode, Long s){
		List<Drzava> drzave = Drzava.findAll();
		List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.findAll();
		if (mode == null || mode.equals("")) {
			mode = "edit";
		}
		render(naseljenaMesta, mode, drzave, s);
	}
	
	public static void shownext(String mode, Long s, Long id) {
		List<Drzava> drzave = new ArrayList<Drzava>();
		drzave.add(Drzava.findById(id));
		List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.find("byDrzava", drzave.get(0)).fetch();
		if (mode == null || mode.equals("")) {
			mode = "locked edit";
		}
		renderTemplate("NaseljenaMesta/show.html", mode, naseljenaMesta, drzave, s, id);
	}
	
	public static void nextshow(Long id) {
		System.out.println("id: " + id);
		List<Drzava> drzave = new ArrayList<Drzava>();
		drzave.add(Drzava.findById(id));
		List<NaseljenoMesto> naseljenaMesta = NaseljenoMesto.find("byDrzava", drzave.get(0)).fetch();
		System.out.println(naseljenaMesta);
		String mode = "locked edit";
		renderTemplate("NaseljenaMesta/show.html", mode, naseljenaMesta, drzave, id);
	}
	
	public static void filterNormal(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		List<NaseljenoMesto> naseljenaMesta = filter(oznaka, naziv, postanskiBroj, drzava);
		String mode = "edit";
		renderTemplate("NaseljenaMesta/show.html",mode, naseljenaMesta);
	}
	
	public static void filterNext(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Long id = drzava;
		List<NaseljenoMesto> naseljenaMesta = filter(oznaka, naziv, postanskiBroj, drzava);
		String mode = "locked edit";
		renderTemplate("NaseljenaMesta/show.html",mode, naseljenaMesta, id);
	}
	
	
	public static List<NaseljenoMesto> filter(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Drzava d = Drzava.findById(drzava);
		List<NaseljenoMesto> naseljenaMesta;
		if(postanskiBroj == null){
			naseljenaMesta = NaseljenoMesto.find("byOznakaLikeAndNazivLikeAndDrzava", "%"+oznaka+"%", "%"+naziv+"%", d).fetch();
		} else {
			naseljenaMesta = NaseljenoMesto.find("byOznakaLikeAndNazivLikeAndPostanskiBrojAndDrzava", "%"+oznaka+"%", "%"+naziv+"%", postanskiBroj, d).fetch();
		} 	
		return naseljenaMesta;
	}
	
	public static void createNormal(@Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          show("add",null);
	    }
		Long s = create(oznaka, naziv, postanskiBroj, drzava);
		String mode = "add";
		show(mode, s);
	}
	
	
	public static void createNext(@Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          shownext("locked add",null, drzava);
	    }
		Long s = create(oznaka, naziv, postanskiBroj, drzava);
		String mode = "locked add";
		shownext(mode, s, drzava);
	}
	
	
	public static Long create(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Drzava d = Drzava.findById(drzava);
		NaseljenoMesto nm = new NaseljenoMesto(oznaka, naziv, postanskiBroj, d);
		nm.save();
		return nm.id;
	}
	
	public static void editNormal(Long id, @Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          show("edit",null);
	    }
		Long s = edit(id, oznaka, naziv, postanskiBroj, drzava);
		String mode = "edit";
		show(mode, s);
	}
	
	public static void editNext(Long id, @Required String oznaka, @Required String naziv, @Required Integer postanskiBroj, Long drzava) {
		validation.maxSize(oznaka, 2);
		validation.maxSize(postanskiBroj, 5);
		if(validation.hasErrors()) {
	          validation.keep();
	          shownext("locked edit",null, drzava);
	    }
		Long s = edit(id, oznaka, naziv, postanskiBroj, drzava);
		String mode = "locked edit";
		shownext(mode, s, drzava);
	}
	
	public static Long edit(Long id, String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
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
		String mode = "edit";
		show(mode, s);
	}

}
