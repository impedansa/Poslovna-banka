package controllers;

import java.util.Date;
import java.util.List;

import models.AnalitikaIzvoda;
import models.Banka;
import models.MedjubankarskiPrenos;
import models.Racun;
import models.ZatvaranjeRacuna;
import play.mvc.Controller;

public class ZatvaranjaRacuna extends Controller{
	
	public static void show(){
		List<ZatvaranjeRacuna> zatvaranjeRacuna = ZatvaranjeRacuna.findAll();
		List<Racun> racuni = Racun.findAll();
		List<AnalitikaIzvoda> analitikeIzvoda = AnalitikaIzvoda.findAll();
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
		render(zatvaranjeRacuna, racuni, analitikeIzvoda);
	}
	
	public static void filter(Date datumZatvaranja, String prebacenoNaRacun, Long racun, Long analitikaIzvoda) {
		
		List<ZatvaranjeRacuna> zatvaranje = filterBasic(datumZatvaranja, prebacenoNaRacun, racun, analitikaIzvoda);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("ZatvaranjaRacuna/show.html", zatvaranje);
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
	
	public static List<ZatvaranjeRacuna> filterBasic(Date datumZatvaranja, String prebacenoNaRacun, Long racun, Long analitikaIzvoda) {
		
		List<ZatvaranjeRacuna> zatvaranje = ZatvaranjeRacuna.find("byDatumZatvaranjaAndPrebacenoNaRacunAndRacunAndAnalitikaIzvoda", datumZatvaranja, prebacenoNaRacun, racun, analitikaIzvoda).fetch();
		return zatvaranje;
	}

}
