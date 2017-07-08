package controllers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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
	
	public static void filter(Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, int modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, long iznos, int tipGreske, String status, DnevnoStanjeRacuna dnevnoStanjeRacuna) {
		
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

}
