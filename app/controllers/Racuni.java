package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Banka;
import models.Klijent;
import models.Racun;
import models.Valuta;
import play.data.validation.Required;
import play.mvc.Controller;

public class Racuni extends Controller {
	
	public static void show(){
		List<Klijent> klijenti = Klijent.findAll();
		List<Banka> banke = Banka.findAll();
		List<Valuta> valute = Valuta.findAll();
		List<Racun> racuni = Racun.findAll();
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
		render(racuni, klijenti, banke, valute);
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
		List<Klijent> klijenti = new ArrayList<Klijent>();
		klijenti.add(Klijent.findById(Long.parseLong(id)));
		List<Banka> banke = new ArrayList<Banka>();
		banke.add(Banka.findById(Long.parseLong(id)));
		List<Valuta> valute = new ArrayList<Valuta>();
		valute.add(Valuta.findById(Long.parseLong(id)));
		List<Racun> racuni = Racun.find("byKlijentAndBankaAndValuta", klijenti.get(0), banke.get(0), valute.get(0)).fetch();
		renderTemplate("Racuni/show.html", racuni, klijenti, banke, valute, id);
	}
	
	public static void filter(String brojRacuna, String statusRacuna, Long klijent, Long banka, Long valuta) {
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		List<Racun> racuni = Racun.find("byBrojRacunaLikeAndNazivRacunaLikeAndKlijentAndBankaAndValuta",  "%"+brojRacuna+"%", "%"+statusRacuna+"%", k, b, v).fetch();
		session.put("mode", "edit");
		session.put("s", null);
		renderTemplate("Racuni/show.html",racuni);
	}
	
	public static void filterNext(String brojRacuna, String statusRacuna, Long klijent, Long banka, Long valuta) {
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		List<Racun> racuni = Racun.find("byBrojRacunaLikeAndNazivRacunaLikeAndKlijentAndBankaAndValuta",  "%"+brojRacuna+"%", "%"+statusRacuna+"%", k, b, v).fetch();
		session.put("mode", "locked edit");
		session.put("s", null);
		session.put("id", klijent);
		renderTemplate("Racuni/show.html",racuni, klijent);
	}
	
	public static void create(@Required String brojRacuna,@Required String statusRacuna, Long klijent, Long banka, Long valuta) {
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		Racun r = new Racun(brojRacuna, statusRacuna, k, b, v);
		r.save();
		session.put("mode", "add");
		session.put("s", r.id);
		show();
	}
	
	
	public static void createNext(@Required String brojRacuna,@Required String statusRacuna, Long klijent, Long banka, Long valuta) {
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		Racun r = new Racun(brojRacuna, statusRacuna, k, b, v);
		r.save();
		session.put("mode", "locked add");
		session.put("s", r.id);
		session.put("id", klijent);
		shownext();
	}
	
	public static void edit(Long id,String brojRacuna,String statusRacuna, Long klijent, Long banka, Long valuta) {
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id != null) {
			Racun r = Racun.findById(id);
			r.brojRacuna = brojRacuna;
			r.statusRacuna = statusRacuna;
			Klijent k = Klijent.findById(klijent);
			r.klijent = k;
			Banka b = Banka.findById(banka);
			r.banka = b;
			Valuta v = Valuta.findById(valuta);
			r.valuta = v;
			r.save();
			s = id;
		} else if (brojRacuna != null){
			List<Racun> racuni = Racun.findAll();
			for(Racun r:racuni){
				if(brojRacuna.equals(r.brojRacuna)) {
					r.brojRacuna = brojRacuna;
					r.statusRacuna = statusRacuna;
					Klijent k = Klijent.findById(klijent);
					r.klijent = k;
					Banka b = Banka.findById(banka);
					r.banka = b;
					Valuta v = Valuta.findById(valuta);
					r.valuta = v;
					r.save();
					s = id;
				}
			}
			
		} 
		session.put("mode", "edit");
		session.put("s", s);
		show();
	}
	
	public static void editNext(Long id,String brojRacuna,String statusRacuna, Long klijent, Long banka, Long valuta) {
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id != null) {
			Racun r = Racun.findById(id);
			r.brojRacuna = brojRacuna;
			r.statusRacuna = statusRacuna;
			Klijent k = Klijent.findById(klijent);
			r.klijent = k;
			Banka b = Banka.findById(banka);
			r.banka = b;
			Valuta v = Valuta.findById(valuta);
			r.valuta = v;
			r.save();
			s = id;
		} else if (brojRacuna != null){
			List<Racun> racuni = Racun.findAll();
			for(Racun r:racuni){
				if(brojRacuna.equals(r.brojRacuna)) {
					r.brojRacuna = brojRacuna;
					r.statusRacuna = statusRacuna;
					Klijent k = Klijent.findById(klijent);
					r.klijent = k;
					Banka b = Banka.findById(banka);
					r.banka = b;
					Valuta v = Valuta.findById(valuta);
					r.valuta = v;
					r.save();
					s = id;
				}
			}
			
		} 
		session.put("mode", "locked edit");
		session.put("s", s);
		session.put("id", klijent);
		shownext();
	}
	
	public static void delete(Long id) {
		Long s = null;
		List<Racun> racuni = Racun.findAll();
		for(int i=0; i< racuni.size(); i++) {
			if(racuni.get(i).getId().equals(id)){
				s = racuni.get(i-1).getId();
			}
		}
		Racun r = Racun.findById(id);
		r.delete();
		String mode = session.get("mode");
		if(mode.equals("locked add")|| mode.equals("locked edit")||mode.equals("locked search")) {
			session.put("mode", "locked edit");
			session.put("s", s);
			session.put("id", r.klijent.id);
			shownext();
		} else {
			session.put("mode", "edit");
			session.put("s", s);
			show();
		}
	}


}