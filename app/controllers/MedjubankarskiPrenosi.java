package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import marshallers.MTMarshaller;
import models.AnalitikaIzvoda;
import models.Banka;
import models.MedjubankarskiPrenos;
import models.StavkaPrenosa;
import play.mvc.Controller;

public class MedjubankarskiPrenosi extends Controller{
	
	public static void show(){
		List<MedjubankarskiPrenos> medjubankarskiPrenosi = MedjubankarskiPrenos.findAll();
		List<Banka> bankePrimaoci = Banka.findAll();
		List<Banka> bankePosiljaoci = Banka.findAll();
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
		render(medjubankarskiPrenosi, bankePrimaoci, bankePosiljaoci);
	}
	
	public static void filter(String vrstaPoruke, Date datum, Long ukupanIznos, Long bankePosiljaoci, Long bankePrimaoci) {
		List<MedjubankarskiPrenos> medjubankarskiPrenosi = filterBasic(vrstaPoruke, datum, ukupanIznos, bankePosiljaoci, bankePrimaoci);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("MedjubankarskiPrenosi/show.html", medjubankarskiPrenosi);
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
	
	public static List<MedjubankarskiPrenos> filterBasic(String vrstaPoruke, Date datum, Long ukupanIznos, Long bankePosiljaoci, Long bankePrimaoci) {
		Banka bpo = Banka.findById(bankePosiljaoci);
		Banka bpr = Banka.findById(bankePrimaoci);
		List<MedjubankarskiPrenos> medjubankarskiPrenosi = MedjubankarskiPrenos.find("byVrstaPorukeLikeAndDatumAndBankaPrimalacAndBankaPosiljalac", "%"+vrstaPoruke+"%",  datum, bpr, bpo).fetch();
		//List<MedjubankarskiPrenos> medjubankarskiPrenosi = MedjubankarskiPrenos.find("byVrstaPorukeAndDatumAndUkupanIznosAndBankaPrimalacAndBankaPosiljalac", vrstaPoruke, datum, ukupanIznos, bpr, bpo).fetch();
		//List<MedjubankarskiPrenos> medjubankarskiPrenosi = MedjubankarskiPrenos.find("byBankaPosiljalacAndBankaPrimalac", bpo, bpr).fetch();
		return medjubankarskiPrenosi;
	}
	
	public static void xmlexport(Long id) throws ParserConfigurationException, TransformerException {
		System.out.println("USAO SAM TU");
		MedjubankarskiPrenos mp = MedjubankarskiPrenos.findById(id);
		List<StavkaPrenosa> sp = StavkaPrenosa.find("byMedjubankarskiPrenos", mp).fetch();
		List<AnalitikaIzvoda> nalozi = new ArrayList<AnalitikaIzvoda>();
		for (StavkaPrenosa stavka: sp){
			AnalitikaIzvoda ai = AnalitikaIzvoda.findById(stavka.analitikaIzvoda.id);
			nalozi.add(ai);
		}
		MTMarshaller.createXml(mp, nalozi);
		show();
	}
}
