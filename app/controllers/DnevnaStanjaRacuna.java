package controllers;

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
	
	
	
}
