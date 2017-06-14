package controllers;

import java.util.Date;
import java.util.List;

import models.AnalitikaIzvoda;
import models.Banka;
import models.MedjubankarskiPrenos;
import models.StavkaPrenosa;
import play.mvc.Controller;

public class StavkePrenosa extends Controller{
	
	public static void show(){
		List<MedjubankarskiPrenos> medjubankarskiPrenos = MedjubankarskiPrenos.findAll();
		List<StavkaPrenosa> stavkePrenosa = StavkaPrenosa.findAll();
		List<AnalitikaIzvoda> analitikaIzvoda = AnalitikaIzvoda.findAll();
		String mode = "";
		String s = "";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			if(mode == null || mode =="") {
				mode = "search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		render(stavkePrenosa, analitikaIzvoda, medjubankarskiPrenos);
	}
	
	public static void filter(Long medjubankarskiPrenos, Long analitikaIzvoda) {
		
		List<StavkaPrenosa> stavkePrenosa = filterBasic(medjubankarskiPrenos, analitikaIzvoda);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("StavkePrenosa/show.html", stavkePrenosa);
	}
	
/*	public static void filterNext(String oznaka, String naziv, Integer postanskiBroj, Long drzava) {
		Long id = drzava;
		List<NaseljenoMesto> naseljenaMesta = filterBasic(oznaka, naziv, postanskiBroj, drzava);
		session.put("mode", "locked edit");
		session.put("s", null);
		session.put("id", drzava);
		renderTemplate("NaseljenaMesta/show.html",naseljenaMesta, id);
	}
*/
	
	public static List<StavkaPrenosa> filterBasic(Long medjubankarskiPrenos, Long analitikaIzvoda) {
		
		List<StavkaPrenosa> stavkePrenosa = StavkaPrenosa.find("byMedjubankarskiPrenosAndAnalitikaIzvoda", medjubankarskiPrenos, analitikaIzvoda).fetch();
		return stavkePrenosa;
	}

}
