package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Banka;
import models.DnevnoStanjeRacuna;
import models.Klijent;
import models.Racun;
import models.Valuta;
import play.mvc.Controller;

public class DnevnaStanjaRacuna extends Controller {

	public static void show(){
		List<DnevnoStanjeRacuna> dnevnaStanjaRacuna = DnevnoStanjeRacuna.findAll();
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
		render(dnevnaStanjaRacuna, racuni);
	}
	
public static void shownext(){
		
		String mode = "";
		String s = "";
		String id = "0";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
			id = params.get("id");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			id = session.get("id");
			if(mode == null || mode =="") {
				mode = "search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		session.put("id", id);
		
		List<Racun> racuni = new ArrayList<Racun>();
		racuni.add(Racun.findById(Long.parseLong(id)));
		List<DnevnoStanjeRacuna> dnevnaStanjaRacuna = DnevnoStanjeRacuna.find("byRacun", racuni.get(0)).fetch();
		renderTemplate("DnevnaStanjaRacuna/show.html",dnevnaStanjaRacuna, racuni, id);
	}
	
	public static void filter(Date datum, Long prethodnoStanje, Long prometNaTeret, Long prometUKorist, Long novoStanje, Long racun) {
		List<DnevnoStanjeRacuna> dnevnaStanjaRacuna = filterBasic(datum, prethodnoStanje, prometNaTeret, prometUKorist, novoStanje, racun);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("DnevnaStanjaRacuna/show.html", dnevnaStanjaRacuna);
	}
	
	public static void filterNext(Date datum, Long prethodnoStanje, Long prometNaTeret, Long prometUKorist, Long novoStanje, Long racun) {
		Long id = racun;
		List<DnevnoStanjeRacuna> dnevnaStanjaRacuna = filterBasic(datum, prethodnoStanje, prometNaTeret, prometUKorist, novoStanje, racun);
		session.put("mode", "locked search");
		session.put("s", null);
		session.put("id", id);
		renderTemplate("DnevnaStanjaRacuna/show.html", dnevnaStanjaRacuna, id);
	}
	
	
	public static List<DnevnoStanjeRacuna> filterBasic(Date datum, Long prethodnoStanje, Long prometNaTeret, Long prometUKorist, Long novoStanje, Long racun) {
		Racun r = Racun.findById(racun);
		int a = 0;
		List<DnevnoStanjeRacuna> stanje = new ArrayList<DnevnoStanjeRacuna>();
		List<DnevnoStanjeRacuna> stanje1 = DnevnoStanjeRacuna.find("byDatum", datum).fetch();
		List<DnevnoStanjeRacuna> stanje2 = DnevnoStanjeRacuna.find("byPrometNaTeret", prometNaTeret).fetch();
		List<DnevnoStanjeRacuna> stanje3 = DnevnoStanjeRacuna.find("byPrometUKorist", prometUKorist).fetch();
		List<DnevnoStanjeRacuna> stanje4 = DnevnoStanjeRacuna.find("byNovoStanje", novoStanje).fetch();
		List<DnevnoStanjeRacuna> stanje5 = DnevnoStanjeRacuna.find("byRacun", r).fetch();
		List<DnevnoStanjeRacuna> stanje6 = DnevnoStanjeRacuna.find("byPrethodnoStanje", prethodnoStanje).fetch();
			//stanje = DnevnoStanjeRacuna.find("byDatumAndPrometNaTeretAndPrometUKoristAndNovoStanjeAndRacun", datum, prometNaTeret, prometUKorist, novoStanje, r).fetch();
		for(DnevnoStanjeRacuna stanja1: stanje1) {
			if(!(stanje.contains(stanja1))) {
				stanje.add(stanja1);
				}
		}
		for(DnevnoStanjeRacuna stanja2: stanje2) {
			if(!(stanje.contains(stanja2))) {
			stanje.add(stanja2);
			}
		}
		for(DnevnoStanjeRacuna stanja3: stanje3) {
			if(!(stanje.contains(stanja3))) {
				stanje.add(stanja3);
				}
		}
		for(DnevnoStanjeRacuna stanja4: stanje4) {
			if(!(stanje.contains(stanja4))) {
				stanje.add(stanja4);
				}
		}
		for(DnevnoStanjeRacuna stanja5: stanje5) {
			if(!(stanje.contains(stanja5))) {
				stanje.add(stanja5);
				}
		}
		for(DnevnoStanjeRacuna stanja6: stanje6) {
			if(!(stanje.contains(stanja6))) {
				stanje.add(stanja6);
				}
		}
		return stanje;
	}
	
	
}
