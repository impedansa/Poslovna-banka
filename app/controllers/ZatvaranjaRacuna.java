package controllers;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;
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
