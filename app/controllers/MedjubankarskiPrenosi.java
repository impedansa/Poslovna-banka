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
				mode = "locked search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		session.put("id", id);
		List<Banka> bankePosiljaoci = Banka.findAll();
		List<Banka> bankePrimaoci = new ArrayList<Banka>();
		System.out.println(id);
		bankePrimaoci.add(Banka.findById(Long.parseLong(id)));
		System.out.println(bankePrimaoci.get(0).getId().toString());
		List<MedjubankarskiPrenos> medjubankarskiPrenosi = MedjubankarskiPrenos.find("byBankaPrimalac", bankePrimaoci.get(0)).fetch();
		System.out.println(medjubankarskiPrenosi.get(0).getBankaPrimalac().getId().toString());
		renderTemplate("MedjubankarskiPrenosi/show.html", medjubankarskiPrenosi, bankePrimaoci, bankePosiljaoci, id);
	}
	
	public static void filter(String vrstaPoruke, Date datum, Long ukupanIznos, Long bankePosiljaoci, Long bankePrimaoci) {
		List<MedjubankarskiPrenos> medjubankarskiPrenosi = filterBasic(vrstaPoruke, datum, ukupanIznos, bankePosiljaoci, bankePrimaoci);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("MedjubankarskiPrenosi/show.html", medjubankarskiPrenosi);
	}
	
	public static void filterNext(String vrstaPoruke, Date datum, Long ukupanIznos, Long bankePosiljaoci, Long bankePrimaoci) {
		Long id = bankePrimaoci;
		List<MedjubankarskiPrenos> medjubankarskiPrenosi = filterBasic(vrstaPoruke, datum, ukupanIznos, bankePosiljaoci, bankePrimaoci);
		session.put("mode", "locked edit");
		session.put("s", null);
		session.put("id", id);
		renderTemplate("MedjubankarskiPrenosi/show.html",medjubankarskiPrenosi, id); 
	}

	
	public static List<MedjubankarskiPrenos> filterBasic(String vrstaPoruke, Date datum, Long ukupanIznos, Long bankePosiljaoci, Long bankePrimaoci) {
		Banka bpo = Banka.findById(bankePosiljaoci);
		Banka bpr = Banka.findById(bankePrimaoci);
		List<MedjubankarskiPrenos> medjubankarskiPrenosi;
		if(datum == null && ukupanIznos == null) {
			medjubankarskiPrenosi = MedjubankarskiPrenos.find("byVrstaPorukeLikeAndBankaPosiljalacAndBankaPrimalac", "%"+vrstaPoruke+"%", bpo, bpr).fetch();
		} else if (datum == null) {
			medjubankarskiPrenosi = MedjubankarskiPrenos.find("byVrstaPorukeLikeAndUkupanIznosAndBankaPosiljalacAndBankaPrimalac", "%"+vrstaPoruke+"%",  ukupanIznos, bpo, bpr).fetch();
		} else if (ukupanIznos == null) {
			medjubankarskiPrenosi = MedjubankarskiPrenos.find("byVrstaPorukeLikeAndDatumAndBankaPosiljalacAndBankaPrimalac", "%"+vrstaPoruke+"%",  datum, bpo, bpr).fetch();
		} else {
			medjubankarskiPrenosi = MedjubankarskiPrenos.find("byVrstaPorukeLikeAndDatumAndUkupanIznosAndBankaPosiljalacAndBankaPrimalac", "%"+vrstaPoruke+"%",  datum, ukupanIznos, bpo, bpr).fetch();
		}
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
