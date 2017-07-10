package controllers;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import models.AnalitikaIzvoda;
import models.Banka;
import models.Klijent;
import models.KlijentD;
import models.MedjubankarskiPrenos;
import models.Racun;
import models.SifrarnikDelatnosti;
import models.ZatvaranjeRacuna;
import models.ZatvaranjeRacunaD;
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
	

	public static void shownextAnalitikaZR(){
		
		String mode = "";
		String s = "";
		String azr_id = "0";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
			azr_id = params.get("azr_id");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			azr_id = session.get("azr_id");
			if(mode == null || mode =="") {
				mode = "search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		session.put("azr_id", azr_id);
		
		List<Racun> racuni = Racun.findAll();
		List<AnalitikaIzvoda> analitikeIzvoda = new ArrayList<AnalitikaIzvoda> ();
		analitikeIzvoda.add(AnalitikaIzvoda.findById(Long.parseLong(azr_id)));
		List<ZatvaranjeRacuna> zatvaranjeRacuna = ZatvaranjeRacuna.find("byAnalitikaIzvoda", analitikeIzvoda.get(0)).fetch();
		renderTemplate("ZatvaranjaRacuna/show.html",zatvaranjeRacuna, racuni, analitikeIzvoda, azr_id);
	}
	
	public static void shownextRacun(){
		
		String mode = "";
		String s = "";
		String r_id = "0";
		if(params.get("mode") != null) {
			mode = params.get("mode");
			s = params.get("s");
			r_id = params.get("r_id");
		} else {
			mode = session.get("mode");
			s = session.get("s");
			r_id = session.get("r_id");
			if(mode == null || mode =="") {
				mode = "search";
			}
		}
		session.put("mode", mode);
		session.put("s", s);
		session.put("r_id", r_id);

		List<Racun> racuni = new ArrayList<Racun> ();
		racuni.add(Racun.findById(Long.parseLong(r_id)));
		List<AnalitikaIzvoda> analitikeIzvoda = AnalitikaIzvoda.findAll();
		List<ZatvaranjeRacuna> zatvaranjeRacuna = ZatvaranjeRacuna.find("byRacun", racuni.get(0)).fetch();
		renderTemplate("ZatvaranjaRacuna/show.html",zatvaranjeRacuna, racuni, analitikeIzvoda, r_id);
	}
	public static void filter(Date datumZatvaranja, String prebacenoNaRacun, Long racun, Long analitikeIzvoda) {
		List<ZatvaranjeRacuna> zatvaranjeRacuna = filterBasic(datumZatvaranja, prebacenoNaRacun, racun, analitikeIzvoda);
		session.put("mode", "search");
		session.put("s", null);
		renderTemplate("ZatvaranjaRacuna/show.html", zatvaranjeRacuna);
	}
	
	public static void filterNext(Date datumZatvaranja, String prebacenoNaRacun, Long racun, Long analitikeIzvoda) {
		Long a_id = analitikeIzvoda;
		Long r_id = racun;
		List<ZatvaranjeRacuna> zatvaranjeRacuna = filterBasic(datumZatvaranja, prebacenoNaRacun, racun, analitikeIzvoda);
		if (session.get("mode").equals("locked search for Analitika")){
			session.put("mode", "locked search for Analitika");
			session.put("s", null);
			session.put("a_id", a_id);
			renderTemplate("ZatvaranjaRacuna/show.html", zatvaranjeRacuna, a_id);
		} else {
			session.put("mode", "locked search for Racun");
			session.put("s", null);
			session.put("r_id", r_id);
			renderTemplate("ZatvaranjaRacuna/show.html", zatvaranjeRacuna, r_id);
		}
	}

	
	public static List<ZatvaranjeRacuna> filterBasic(Date datumZatvaranja, String prebacenoNaRacun, Long racun, Long analitikeIzvoda) {
		Racun r = Racun.findById(racun);
		AnalitikaIzvoda a = AnalitikaIzvoda.findById(analitikeIzvoda);
		List<ZatvaranjeRacuna> zatvaranje;
		if(datumZatvaranja == null) {
			zatvaranje = ZatvaranjeRacuna.find("byPrebacenoNaRacunLikeAndRacunAndAnalitikaIzvoda", "%"+prebacenoNaRacun+"%", r, a).fetch();
		} else {
			zatvaranje = ZatvaranjeRacuna.find("byDatumZatvaranjaAndPrebacenoNaRacunLikeAndRacunAndAnalitikaIzvoda", datumZatvaranja, "%"+prebacenoNaRacun+"%", r, a).fetch();
		} 
		System.out.println(zatvaranje.get(0).getId().toString());
		return zatvaranje;
	}
	
/*	public static ZatvaranjeRacuna encryptKlijent (Date datumZatvaranja, String prebacenoNaRacun, Long racun, Long analitikaIzvoda) throws NoSuchProviderException {
		
		String alias = "admin";
		String passKeyS = "admin";
		String pass = "admin";
		SecretKey sk = null;
	
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		KeyStoreReader ksReader = new KeyStoreReader();
		File f = new File("C:/Users/Valentina/" + alias + ".jks");
		if(f.exists() && !f.isDirectory()) { 
			sk = ksReader.readSecretKey("C:/Users/Valentina/" + alias + ".jks", passKeyS, alias, pass);
			System.out.println("jel moguce");
			
		}
		else {
			keyStoreWriter.loadKeyStore(null, passKeyS.toCharArray());
			sk = AES.generateKey();
			keyStoreWriter.write(alias, sk, passKeyS.toCharArray());
			keyStoreWriter.saveKeyStore("C:/Users/Valentina/" + alias + ".jks", passKeyS.toCharArray());
			System.out.println("kljuc "+sk);
		} 
		byte[] eprebacenoNaRacun = AES.encrypt(prebacenoNaRacun, sk);
	
		Racun r = Racun.findById(racun);
		AnalitikaIzvoda ai = AnalitikaIzvoda.findById(analitikaIzvoda);
		ZatvaranjeRacuna zr = new ZatvaranjeRacuna(datumZatvaranja, eprebacenoNaRacun, r, ai);
		return zr;
		
	}
	
	public static ZatvaranjeRacunaD decryptKlijent (Long id, Date datumZatvaranja, byte[] prebacenoNaRacun, Long racun, Long analitikaIzvoda) throws IOException, NoSuchProviderException {
		
		String alias = "admin";
		String passKeyS = "admin";
		String pass = "admin";
		SecretKey sk = null;
	
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		KeyStoreReader ksReader = new KeyStoreReader();
		File f = new File("C:/Users/Valentina/" + alias + ".jks");
		sk = ksReader.readSecretKey("C:/Users/Valentina/" + alias + ".jks", passKeyS, alias, pass);	
		String dprebacenoNaRacun = new String(AES.decrypt(prebacenoNaRacun, sk));
		 
		Racun r = Racun.findById(racun);
		AnalitikaIzvoda ai = AnalitikaIzvoda.findById(analitikaIzvoda);
		ZatvaranjeRacunaD zr = new ZatvaranjeRacunaD(id, datumZatvaranja, dprebacenoNaRacun, r, ai);
		return zr;
		
		
	}
*/	
} 
