package controllers;

import java.util.ArrayList;
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
	
public static void shownextAnalitika(){
		
		String mode = "";
		String s = "";
		String a_id = "0";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
			a_id = params.get("a_id");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			a_id = session.get("a_id");
			if(mode == null || mode =="") {
				mode = "search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		session.put("a_id", a_id);
		
		List<AnalitikaIzvoda> analitikaIzvoda = new ArrayList<AnalitikaIzvoda>();
		analitikaIzvoda.add(AnalitikaIzvoda.findById(Long.parseLong(a_id)));
		List<MedjubankarskiPrenos> medjubankarskiPrenos = MedjubankarskiPrenos.findAll();
		List<StavkaPrenosa> stavkePrenosa = StavkaPrenosa.find("byMedjubankarskiPrenos", medjubankarskiPrenos.get(0)).fetch();
		renderTemplate("StavkePrenosa/show.html",stavkePrenosa, analitikaIzvoda, medjubankarskiPrenos, a_id);
	}
	
	public static void shownextMedjubankarski(){
		String mode = "";
		String s = "";
		String m_id = "0";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
			m_id = params.get("m_id");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			m_id = session.get("m_id");
			if(mode == null || mode =="") {
				mode = "search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		session.put("m_id", m_id);
		
		List<AnalitikaIzvoda> analitikaIzvoda = AnalitikaIzvoda.findAll();
		List<MedjubankarskiPrenos> medjubankarskiPrenos = new ArrayList<MedjubankarskiPrenos>();
		medjubankarskiPrenos.add(MedjubankarskiPrenos.findById(Long.parseLong(m_id)));
		List<StavkaPrenosa> stavkePrenosa = StavkaPrenosa.find("byMedjubankarskiPrenos", medjubankarskiPrenos.get(0)).fetch();
		renderTemplate("StavkePrenosa/show.html", stavkePrenosa, analitikaIzvoda, medjubankarskiPrenos, m_id);
	}
	
	public static void filter(Long medjubankarskiPrenos, Long analitikaIzvoda) {
		
		List<StavkaPrenosa> stavkePrenosa = filterBasic(medjubankarskiPrenos, analitikaIzvoda);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("StavkePrenosa/show.html", stavkePrenosa);
	}
	
	public static void filterNext(Long medjubankarskiPrenos, Long analitikaIzvoda) {
		Long a_id = analitikaIzvoda;
		Long m_id = medjubankarskiPrenos;
		List<StavkaPrenosa> stavkePrenosa = filterBasic(medjubankarskiPrenos, analitikaIzvoda);
		System.out.println(stavkePrenosa.get(0).getId().toString());
		if (session.get("mode").equals("locked search for Analitika")){
			session.put("mode", "locked search for Analitika");
			session.put("s", null);
			session.put("a_id", a_id);
			renderTemplate("StavkePrenosa/show.html", stavkePrenosa, a_id);
		} else {
			session.put("mode", "locked search for Medjubankarski");
			session.put("s", null);
			session.put("m_id", m_id);
			renderTemplate("StavkePrenosa/show.html", stavkePrenosa, m_id);
		}
	}

	
	public static List<StavkaPrenosa> filterBasic(Long medjubankarskiPrenos, Long analitikaIzvoda) {
		MedjubankarskiPrenos m = MedjubankarskiPrenos.findById(medjubankarskiPrenos);
		AnalitikaIzvoda a = AnalitikaIzvoda.findById(analitikaIzvoda);
		List<StavkaPrenosa> stavkePrenosa = StavkaPrenosa.find("byMedjubankarskiPrenosAndAnalitikaIzvoda", m, a).fetch();
		return stavkePrenosa;
	}

}
