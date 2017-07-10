package controllers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import marshallers.NalogXmlParser;
import models.AnalitikaIzvoda;
import models.Banka;
import models.DnevnoStanjeRacuna;

import org.xml.sax.SAXException;

import play.mvc.Controller;
import procedures.AnalitikaIzvodaProcedure;

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
	
	public static void uploadxml(File[] stavkeplacanja, String nalogodavac){
		System.out.println("zove");
		for (int i = 0; i<stavkeplacanja.length; i++){
			AnalitikaIzvoda ai;
			try {
				ai = NalogXmlParser.parsexml(stavkeplacanja[i]);
				String result = AnalitikaIzvodaProcedure.startProcedure(ai, (Banka)Banka.findAll().get(0));
				System.out.println(result);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		show();
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
	
		List<DnevnoStanjeRacuna> dnevnaStanjaRacuna = new ArrayList<DnevnoStanjeRacuna> ();
		dnevnaStanjaRacuna.add(DnevnoStanjeRacuna.findById(Long.parseLong(id)));
		List<AnalitikaIzvoda> analitikeIzvoda = AnalitikaIzvoda.find("byDnevnoStanjeRacuna", dnevnaStanjaRacuna.get(0)).fetch();
		renderTemplate("AnalitikeIzvoda/show.html", analitikeIzvoda, dnevnaStanjaRacuna, id);
	}
	
	public static void filter(Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, int modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, long iznos, int tipGreske, String status, Long dnevnoStanjeRacuna) {
		List<AnalitikaIzvoda> analitikeIzvoda = filterBasic(datumAnalitike, smer, duznikNalogodavac, svrhaPlacanja, poverilacPrimalac, datumPrijema, datumValute, racunDuznika, modelZaduzenja, pozivNaBrojZaduzenja, racunPoverioca,  modelOdobrenja, pozivNaBrojOdobrenja, hitno, iznos, tipGreske, status, dnevnoStanjeRacuna);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("AnalitikeIzvoda/show.html", analitikeIzvoda);
	}
	public static void filterNext(Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, int modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, long iznos, int tipGreske, String status, Long dnevnoStanjeRacuna) {
		Long id = dnevnoStanjeRacuna;
		List<AnalitikaIzvoda> analitikeIzvoda = filterBasic(datumAnalitike, smer, duznikNalogodavac, svrhaPlacanja, poverilacPrimalac, datumPrijema, datumValute, racunDuznika, modelZaduzenja, pozivNaBrojZaduzenja, racunPoverioca,  modelOdobrenja, pozivNaBrojOdobrenja, hitno, iznos, tipGreske, status, dnevnoStanjeRacuna);
		session.put("mode", "locked search");
		session.put("s", null);
		session.put("id", id);
		renderTemplate("AnalitikeIzvoda/show.html", analitikeIzvoda, id);
	}
	public static List<AnalitikaIzvoda> filterBasic(Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, int modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, long iznos, int tipGreske, String status, Long dnevnoStanjeRacuna) {
		DnevnoStanjeRacuna dsr = DnevnoStanjeRacuna.findById(dnevnoStanjeRacuna);
		List<AnalitikaIzvoda> analitika = new ArrayList<AnalitikaIzvoda>();;
		List<AnalitikaIzvoda> analitika1 = AnalitikaIzvoda.find("byDatumAnalitike", datumAnalitike).fetch();
		List<AnalitikaIzvoda> analitika2 = AnalitikaIzvoda.find("bySmerLike", "%"+smer+"%").fetch();
		List<AnalitikaIzvoda> analitika3 = AnalitikaIzvoda.find("byDuznikNalogodavacLike", "%"+duznikNalogodavac+"%").fetch();
		List<AnalitikaIzvoda> analitika4 = AnalitikaIzvoda.find("bySvrhaPlacanjaLike", "%"+svrhaPlacanja+"%").fetch();
		List<AnalitikaIzvoda> analitika5 = AnalitikaIzvoda.find("byPoverilacPrimalacLike", "%"+poverilacPrimalac+"%").fetch();
		List<AnalitikaIzvoda> analitika6 = AnalitikaIzvoda.find("byDatumPrijema", datumPrijema).fetch();
		List<AnalitikaIzvoda> analitika7 = AnalitikaIzvoda.find("byDatumValute", datumValute).fetch();
		List<AnalitikaIzvoda> analitika8 = AnalitikaIzvoda.find("byRacunDuznikaLike", "%"+racunDuznika+"%").fetch();
		List<AnalitikaIzvoda> analitika9 = AnalitikaIzvoda.find("byModelZaduzenja", modelZaduzenja).fetch();
		List<AnalitikaIzvoda> analitika10 = AnalitikaIzvoda.find("byPozivNaBrojZaduzenjaLike", "%"+pozivNaBrojZaduzenja+"%").fetch();
		List<AnalitikaIzvoda> analitika11 = AnalitikaIzvoda.find("byRacunPoveriocaLike", "%"+racunPoverioca+"%").fetch();
		List<AnalitikaIzvoda> analitika12 = AnalitikaIzvoda.find("byModelOdobrenja", modelOdobrenja).fetch();
		List<AnalitikaIzvoda> analitika13 = AnalitikaIzvoda.find("byPozivNaBrojOdobrenjaLike", "%"+pozivNaBrojOdobrenja+"%").fetch();
		List<AnalitikaIzvoda> analitika14 = AnalitikaIzvoda.find("byHitno", hitno).fetch();
		List<AnalitikaIzvoda> analitika15 = AnalitikaIzvoda.find("byIznos", iznos).fetch();
		List<AnalitikaIzvoda> analitika16 = AnalitikaIzvoda.find("byTipGreske", tipGreske).fetch();
		List<AnalitikaIzvoda> analitika17 = AnalitikaIzvoda.find("byStatusLike", "%"+status+"%").fetch();
		List<AnalitikaIzvoda> analitika18 = AnalitikaIzvoda.find("byDnevnoStanjeRacuna", dsr).fetch();
		
		for(AnalitikaIzvoda a1: analitika1) {
			if(!(analitika.contains(a1))) {
				analitika.add(a1);
			}
		}
		for(AnalitikaIzvoda a2: analitika2) {
			if(!(analitika.contains(a2))) {
				analitika.add(a2);
			}
		}
		for(AnalitikaIzvoda a3: analitika3) {
			if(!(analitika.contains(a3))) {
				analitika.add(a3);
			}
		}
		for(AnalitikaIzvoda a4: analitika4) {
			if(!(analitika.contains(a4))) {
				analitika.add(a4);
			}
		}
		for(AnalitikaIzvoda a5: analitika5) {
			if(!(analitika.contains(a5))) {
				analitika.add(a5);
			}
		}
		for(AnalitikaIzvoda a6: analitika6) {
			if(!(analitika.contains(a6))) {
				analitika.add(a6);
			}
		}
		for(AnalitikaIzvoda a7: analitika7) {
			if(!(analitika.contains(a7))) {
				analitika.add(a7);
			}
		}
		for(AnalitikaIzvoda a8: analitika8) {
			if(!(analitika.contains(a8))) {
				analitika.add(a8);
			}
		}
		for(AnalitikaIzvoda a9: analitika9) {
			if(!(analitika.contains(a9))) {
				analitika.add(a9);
			}
		}
		for(AnalitikaIzvoda a10: analitika10) {
			if(!(analitika.contains(a10))) {
				analitika.add(a10);
			}
		}
		for(AnalitikaIzvoda a11: analitika11) {
			if(!(analitika.contains(a11))) {
				analitika.add(a11);
			}
		}
		for(AnalitikaIzvoda a12: analitika12) {
			if(!(analitika.contains(a12))) {
				analitika.add(a12);
			}
		}
		for(AnalitikaIzvoda a13: analitika13) {
			if(!(analitika.contains(a13))) {
				analitika.add(a13);
			}
		}
		for(AnalitikaIzvoda a14: analitika14) {
			if(!(analitika.contains(a14))) {
				analitika.add(a14);
			}
		}
		for(AnalitikaIzvoda a15: analitika15) {
			if(!(analitika.contains(a15))) {
				analitika.add(a15);
			}
		}
		for(AnalitikaIzvoda a16: analitika16) {
			if(!(analitika.contains(a16))) {
				analitika.add(a16);
			}
		}
		for(AnalitikaIzvoda a17: analitika17) {
			if(!(analitika.contains(a17))) {
				analitika.add(a17);
			}
		}
		for(AnalitikaIzvoda a18: analitika18) {
			if(!(analitika.contains(a18))) {
				analitika.add(a18);
			}
		}
		//analitika = AnalitikaIzvoda.find("byDatumAnalitikeAndSmerLikeAndDuznikNalogodavacLikeAndSvrhaPlacanjaLikeAndPoverilacPrimalacLikeAndDatumPrijemaAndDatumValuteAndRacunDuznikaLikeAndModelZaduzenjaAndPozivNaBrojZaduzenjaLikeAndRacunPoveriocaLikeAndModelOdobrenjaAndPozivNaBrojOdobrenjaLikeAndHitnoAndIznosAndTipGreskeAndStatusLikeAndDnevnoStanjeRacuna", datumAnalitike, "%"+ smer + "%", "%" +duznikNalogodavac+ "%", "%" +svrhaPlacanja+ "%", "%" +poverilacPrimalac+ "%", datumPrijema, datumValute, "%"+racunDuznika+"%", modelZaduzenja, "%"+pozivNaBrojZaduzenja+ "%", "%"+racunPoverioca+"%",  modelOdobrenja, "%" +pozivNaBrojOdobrenja+ "%", hitno, iznos, tipGreske, "%"+status+"%", dsr).fetch();
		return analitika;
	}


}
