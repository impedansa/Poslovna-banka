package controllers;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import models.Klijent;
import models.KlijentD;
import models.SifrarnikDelatnosti;
import play.data.validation.Required;
import play.mvc.Controller;





public class Klijenti extends Controller {
	
	public static void show(){
				List<SifrarnikDelatnosti> sifrarniciDelatnosti = SifrarnikDelatnosti.findAll();
				List<Klijent> klijenti = Klijent.findAll();
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
				render(klijenti, sifrarniciDelatnosti);
			}
	
	
	
	public static void filter(String jmbg, Integer pib, String naziv, String adresa, String telefon, String eMail, String fax, String tipLica, Long sifrarnikDelatnosti) {
		SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
		List<Klijent> klijenti;
		if(pib == null)
			klijenti = Klijent.find("byJmbgLikeAndNazivLikeAndAdresaLikeAndTelefonLikeAndEMailLikeAndFaxLikeAndTipLicaLikeAndSifrarnikDelatnosti", "%"+jmbg+"%", "%"+naziv+"%", "%"+adresa+"%", "%"+telefon+"%", "%"+eMail+"%", "%"+fax+"%", "%"+tipLica+"%", sd).fetch();
		else
			klijenti = Klijent.find("byJmbgLikeAndPibAndNazivLikeAndAdresaLikeAndTelefonLikeAndEMailLikeAndFaxLikeAndTipLicaLikeAndSifrarnikDelatnosti", "%"+jmbg+"%", pib, "%"+naziv+"%", "%"+adresa+"%", "%"+telefon+"%", "%"+eMail+"%", "%"+fax+"%", "%"+tipLica+"%", sd).fetch();
		session.put("mode", "edit");
		session.put("s", null);
		renderTemplate("Klijenti/show.html",klijenti);
	}
	
	
	
	public static void create(String jmbg, Integer pib, @Required String naziv, @Required String adresa, String telefon, String eMail, String fax, @Required String tipLica, Long sifrarnikDelatnosti) {
				validation.maxSize(tipLica, 1);
				if(validation.hasErrors()) {
			          validation.keep();
			          session.put("mode", "add");
			  		  session.put("s", null);
			          show();
			    }
				SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
				Klijent k = new Klijent(jmbg, pib, naziv, adresa, telefon, eMail, fax, tipLica, sd);
				k.save();
				session.put("mode", "add");
				session.put("s", k.id);
				show();
			}
	
	public static void edit(Long id, String jmbg, Integer pib, @Required String naziv, @Required String adresa, String telefon, String eMail, String fax, @Required String tipLica, Long sifrarnikDelatnosti) {
				validation.maxSize(tipLica, 1);
				if(validation.hasErrors()) {
			          validation.keep();
			          session.put("mode", "add");
			  		  session.put("s", null);
			          show();
			    }
				Long s = null;
				if(id != null) {
					Klijent k = Klijent.findById(id);
					k.jmbg = jmbg;
					k.pib = pib;
					k.naziv = naziv;
					k.adresa = adresa;
					k.telefon = telefon;
					k.eMail = eMail;
					k.fax = fax;
					k.tipLica = tipLica;
					SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
					k.sifrarnikDelatnosti = sd;
					k.save();
					s = id;
				} else if (jmbg != null){
					List<Klijent> klijenti = Klijent.findAll();
					for(Klijent k:klijenti){
						if(jmbg.equals(k.jmbg)) {
							k.jmbg = jmbg;
							k.pib = pib;
							k.naziv = naziv;
							k.adresa = adresa;
							k.telefon = telefon;
							k.eMail = eMail;
							k.fax = fax;
							k.tipLica = tipLica;
							SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
							k.sifrarnikDelatnosti = sd;
							k.save();
							s = id;
						}
					}
					
				} else if(pib != null) {
					List<Klijent> klijenti = Klijent.findAll();
					for(Klijent k:klijenti){
						if(pib.equals(k.pib)) {
							k.jmbg = jmbg;
							k.pib = pib;
							k.naziv = naziv;
							k.adresa = adresa;
							k.telefon = telefon;
							k.eMail = eMail;
							k.fax = fax;
							k.tipLica = tipLica;
							SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
							k.sifrarnikDelatnosti = sd;
							k.save();
							s = id;
						}
					}
				}
				session.put("mode", "edit");
				session.put("s", s);
				show();
			}
	
	
	
	public static void delete(Long id) throws IOException, NoSuchProviderException {
		checkAuthenticity();
		Long s = null;
		List<Klijent> klijenti = Klijent.findAll();
		for(int i=0; i< klijenti.size(); i++) {
			if(klijenti.get(i).getId().equals(id)){
				s = klijenti.get(i-1).getId();
			}
		}
		Klijent k = Klijent.findById(id);
		k.delete();
		String mode = session.get("mode");
		if(mode.equals("locked add")|| mode.equals("locked edit")||mode.equals("locked search")) {
			session.put("mode", "locked edit");
			session.put("s", s);
			session.put("id", k.sifrarnikDelatnosti.id);
			//shownext();
		} else {
			session.put("mode", "edit");
			session.put("s", s);
			show();
		}
	}
	
	 public static void shownext() {
			String modeN = "";
			String sN = "";
			String id = "0";
			if(params.get("mode") != null) {
				modeN = params.get("mode");
				sN = params.get("s");
				id = params.get("id");
			} else {
				modeN = session.get("mode");
				sN = session.get("s");
				id = session.get("id");
				if(modeN == null || modeN =="") {
					modeN = "locked edit";
				}
			}
			session.put("mode", modeN);
			session.put("s", sN);
			session.put("id", id);
			List<SifrarnikDelatnosti> sifrarniciDelatnosti = new ArrayList<SifrarnikDelatnosti>();
			sifrarniciDelatnosti.add(SifrarnikDelatnosti.findById(Long.parseLong(id)));
			List<Klijent> klijenti = Klijent.find("bySifrarnikDelatnosti", sifrarniciDelatnosti.get(0)).fetch();
			renderTemplate("Klijenti/show.html", klijenti, sifrarniciDelatnosti, id);
			
		} 
		
		public static void filterNext(String jmbg, Integer pib, String naziv, String adresa, String telefon, String eMail, String fax, String tipLica, Long sifrarnikDelatnosti) {
			SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
			List<Klijent> klijenti;
			if(pib == null)
				klijenti = Klijent.find("byJmbgLikeAndNazivLikeAndAdresaLikeAndTelefonLikeAndEMailLikeAndFaxLikeAndTipLicaLikeAndSifrarnikDelatnosti", "%"+jmbg+"%", "%"+naziv+"%", "%"+adresa+"%", "%"+telefon+"%", "%"+eMail+"%", "%"+fax+"%", "%"+tipLica+"%", sd).fetch();
			else
				klijenti = Klijent.find("byJmbgLikeAndPibAndNazivLikeAndAdresaLikeAndTelefonLikeAndEMailLikeAndFaxLikeAndTipLicaLikeAndSifrarnikDelatnosti", "%"+jmbg+"%", pib, "%"+naziv+"%", "%"+adresa+"%", "%"+telefon+"%", "%"+eMail+"%", "%"+fax+"%", "%"+tipLica+"%", sd).fetch();
			
			session.put("mode", "locked edit");
			session.put("s", null);
			session.put("id", sifrarnikDelatnosti);
			renderTemplate("Klijenti/show.html",klijenti, sifrarnikDelatnosti); 
		}
		
		public static void createNext(String jmbg, String pib, @Required String naziv, @Required String adresa, String telefon, String eMail, String fax, @Required String tipLica, Long sifrarnikDelatnosti) throws IOException {
			checkAuthenticity();
			validation.maxSize(tipLica, 1);
			if(validation.hasErrors()) {
		          validation.keep();
		          session.put("mode", "add");
		  		  session.put("s", null);
		          show();
		    }
			SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
			Integer p = Integer.parseInt(pib);
			Klijent k = new Klijent(jmbg, p, naziv, adresa, telefon, eMail, fax, tipLica, sd);
			k.save();
			session.put("mode", "locked add");
			session.put("s", k.id);
			session.put("id", sifrarnikDelatnosti);
			shownext(); 
		}
		
		public static void editNext(Long id, String jmbg, String pib, @Required String naziv, @Required String adresa, String telefon, String eMail, String fax, @Required String tipLica, Long sifrarnikDelatnosti) throws IOException {
			checkAuthenticity();
			validation.maxSize(tipLica, 1);
			if(validation.hasErrors()) {
		          validation.keep();
		          session.put("mode", "add");
		  		  session.put("s", null);
		          show();
		    }
			Integer p = Integer.parseInt(pib);
			Long s = null;
			if(id != null) {
				Klijent k = Klijent.findById(id);
				k.jmbg = jmbg;
				k.pib = p;
				k.naziv = naziv;
				k.adresa = adresa;
				k.telefon = telefon;
				k.eMail = eMail;
				k.fax = fax;
				k.tipLica = tipLica;
				SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
				k.sifrarnikDelatnosti = sd;
				k.save();
				s = id;
			} else if (jmbg != null){
				List<Klijent> klijenti = Klijent.findAll();
				for(Klijent k:klijenti){
					if(jmbg.equals(k.jmbg)) {
						k.jmbg = jmbg;
						k.pib = p;
						k.naziv = naziv;
						k.adresa = adresa;
						k.telefon = telefon;
						k.eMail = eMail;
						k.fax = fax;
						k.tipLica = tipLica;
						SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
						k.sifrarnikDelatnosti = sd;
						k.save();
						s = id;
					}
				}
				
			} else if(pib != null) {
				List<Klijent> klijenti = Klijent.findAll();
				for(Klijent k:klijenti){
					if(pib.equals(k.pib)) {
						k.jmbg = jmbg;
						k.pib = p;
						k.naziv = naziv;
						k.adresa = adresa;
						k.telefon = telefon;
						k.eMail = eMail;
						k.fax = fax;
						k.tipLica = tipLica;
						SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
						k.sifrarnikDelatnosti = sd;
						k.save();
						s = id;
					}
				}
			}
			session.put("mode", "locked edit");
			session.put("s", s);
			session.put("id", sifrarnikDelatnosti);
			shownext(); 
		}
	
/*	public static Klijent encryptKlijent (String jmbg, String pib, String naziv, String adresa, String telefon, String eMail, String fax, String tipLica, Long sifrarnikDelatnosti) throws NoSuchProviderException {
		
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
		byte[] ejmbg = AES.encrypt(jmbg, sk);
		byte[] epib = AES.encrypt(pib, sk);
		byte[] enaziv = AES.encrypt(naziv, sk);
		byte[] eadresa = AES.encrypt(adresa, sk);
		byte[] etelefon = AES.encrypt(telefon, sk);
		byte[] eeMail = AES.encrypt(eMail, sk);
		byte[] efax = AES.encrypt(fax, sk);
		byte[] etipLica = AES.encrypt(tipLica, sk);
	
		SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
		Klijent k = new Klijent(ejmbg, epib, enaziv, eadresa, etelefon, eeMail, efax, etipLica, sd);
		return k;
		
	}
	
	public static KlijentD decryptKlijent (Long klijentId, byte[] jmbg, byte[] pib, byte[] naziv, byte[] adresa, byte[] telefon, byte[] eMail, byte[] fax, byte[] tipLica, Long sifrarnikDelatnosti) throws IOException, NoSuchProviderException {
		
		String alias = "admin";
		String passKeyS = "admin";
		String pass = "admin";
		SecretKey sk = null;
	
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		KeyStoreReader ksReader = new KeyStoreReader();
		File f = new File("C:/Users/Valentina/" + alias + ".jks");
		sk = ksReader.readSecretKey("C:/Users/Valentina/" + alias + ".jks", passKeyS, alias, pass);	
		String djmbg = new String(AES.decrypt(jmbg, sk));
		String dpib = new String(AES.decrypt(pib, sk));
		String dnaziv = new String(AES.decrypt(naziv, sk));
		String dadresa = new String(AES.decrypt(adresa, sk));
		String dtelefon = new String(AES.decrypt(telefon, sk));
		String deMail = new String(AES.decrypt(eMail, sk));
		String dfax = new String(AES.decrypt(fax, sk));
		String dtipLica = new String(AES.decrypt(tipLica, sk)); 
		SifrarnikDelatnosti sd = SifrarnikDelatnosti.findById(sifrarnikDelatnosti);
		KlijentD k = new KlijentD(klijentId, djmbg, dpib, dnaziv, dadresa, dtelefon, deMail, dfax, dtipLica, sd);
		return k;
		
		
	}
*/
	
	
	
}

