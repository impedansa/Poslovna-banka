package controllers;

import java.util.Date;
import java.util.List;

import models.AnalitikaIzvoda;
import models.DnevnoStanjeRacuna;
import play.mvc.Controller;

public class AnalitikeIzvoda extends Controller {
	
	public static void show(){
			List<AnalitikaIzvoda> analitikeIzvoda = AnalitikaIzvoda.findAll();
			List<DnevnoStanjeRacuna> dnevnaStanjaRacuna = DnevnoStanjeRacuna.findAll();
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
			render(analitikeIzvoda, dnevnaStanjaRacuna);
		}
	
	public static void filter(Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, int modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, long iznos, int tipGreske, String status, DnevnoStanjeRacuna dnevnoStanjeRacuna) {
		
	}

}
