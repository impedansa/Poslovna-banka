package controllers;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import models.Banka;
import models.Klijent;
import models.Racun;
import models.Valuta;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import procedures.UkidanjeRacunaProcedure;


public class Racuni extends Controller {
	
	
	public static void show(){
				List<Klijent> klijenti = Klijent.findAll();
				List<Banka> banke = Banka.findAll();
				List<Valuta> valute = Valuta.findAll();
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
				render(racuni, klijenti, banke, valute);
			}
			
	
	public static void shownextKlijent() {
		String modeN = "";
		String sN = "";
		String k_id = "0";
		if(params.get("mode") != null) {
			modeN = params.get("mode");
			sN = params.get("s");
			k_id = params.get("k_id");
		} else {
			modeN = session.get("mode");
			sN = session.get("s");
			k_id = session.get("k_id");
			if(modeN == null || modeN =="") {
				modeN = "locked edit for Klijent";
			}
		}
		session.put("mode", modeN);
		session.put("s", sN);
		session.put("k_id", k_id);
		List<Klijent> klijenti = new ArrayList<Klijent>();
		klijenti.add(Klijent.findById(Long.parseLong(k_id)));
		List<Banka> banke = Banka.findAll();
		List<Valuta> valute = Valuta.findAll();
		List<Racun> racuni = Racun.find("byKlijent", klijenti.get(0)).fetch();
		
		renderTemplate("Racuni/show.html", racuni, klijenti, banke, valute, k_id);
	}
	public static void shownextValuta() {
		String modeN = "";
		String sN = "";
		String v_id = "0";
		if(params.get("mode") != null) {
			modeN = params.get("mode");
			sN = params.get("s");
			v_id = params.get("v_id");
		} else {
			modeN = session.get("mode");
			sN = session.get("s");
			v_id = session.get("v_id");
			if(modeN == null || modeN =="") {
				modeN = "locked edit for Valuta";
			}
		}
		session.put("mode", modeN);
		session.put("s", sN);
		session.put("v_id", v_id);
		List<Klijent> klijenti = Klijent.findAll();
		List<Banka> banke = Banka.findAll();
		List<Valuta> valute = new ArrayList<Valuta>();
		valute.add(Valuta.findById(Long.parseLong(v_id)));
		List<Racun> racuni = Racun.find("byValuta", valute.get(0)).fetch();
		System.out.println(racuni.get(0).getId());
		
		renderTemplate("Racuni/show.html", racuni, klijenti, banke, valute, v_id);
		
	}
	
	public static void shownextBanka() {
		String modeN = "";
		String sN = "";
		String b_id = "0";
		if(params.get("mode") != null) {
			modeN = params.get("mode");
			sN = params.get("s");
			b_id = params.get("b_id");
		} else {
			modeN = session.get("mode");
			sN = session.get("s");
			b_id = session.get("b_id");
			if(modeN == null || modeN =="") {
				modeN = "locked edit for Banka";
			}
		}
		session.put("mode", modeN);
		session.put("s", sN);
		session.put("b_id", b_id);
		List<Klijent> klijenti = Klijent.findAll();
		List<Banka> banke = new ArrayList<Banka>();
		banke.add(Banka.findById(Long.parseLong(b_id)));
		List<Valuta> valute = Valuta.findAll();
		List<Racun> racuni = Racun.find("byBanka", banke.get(0)).fetch();
		
		renderTemplate("Racuni/show.html", racuni, klijenti, banke, valute, b_id);
	}

	public static void filter(String brojRacuna, boolean statusRacuna, Long klijent, Long banka, Long valuta) {
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		List<Racun> racuni = Racun.find("byBrojRacunaLikeAndStatusRacunaAndKlijentAndBankaAndValuta",  "%"+brojRacuna+"%", statusRacuna, k, b, v).fetch();
		session.put("mode", "edit");
		session.put("s", null);
		renderTemplate("Racuni/show.html",racuni);
	}
	
	
	public static void filterNext(String brojRacuna, boolean statusRacuna, Long klijent, Long banka, Long valuta) {
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		List<Racun> racuni = Racun.find("byBrojRacunaLikeAndStatusRacunaAndKlijentAndBankaAndValuta",  "%"+brojRacuna+"%", statusRacuna, k, b, v).fetch();
		if (session.get("mode").equals("locked search for Banka")) {
			session.put("mode", "locked edit for Banka");
			session.put("s", null);
			session.put("b_id", banka);
			renderTemplate("Racuni/show.html",racuni, banka); 
		} else if (session.get("mode").equals("locked search for Klijent")) {
			session.put("mode", "locked edit for Klijent");
			session.put("s", null);
			session.put("k_id", klijent);
			renderTemplate("Racuni/show.html",racuni, klijent); 
		} else {
			session.put("mode", "locked edit for Valuta");
			session.put("s", null);
			session.put("v_id", valuta);
			renderTemplate("Racuni/show.html",racuni, valuta); 
		}
		
	}
	
	
	public static void create(@Required String brojRacuna,@Required boolean statusRacuna, Long klijent, Long banka, Long valuta) throws NoSuchProviderException, IOException {
		Logger.info("Zaposleni sa ID-jem: "+session.get("user")+" pokusao kreiranje racuna sa IP adrese: "+ Logovi.getClientIp());
		checkAuthenticity();
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		Racun r = new Racun(brojRacuna, statusRacuna, k, b, v);
		r.save();
		Logger.info("Zaposleni sa ID-jem: "+session.get("user")+" kreirao racun sa IP adrese: "+ Logovi.getClientIp());
		session.put("mode", "add");
		session.put("s", r.id);
		show();
	}
	
	
	public static void createNext(@Required String brojRacuna,@Required boolean statusRacuna, Long klijent, Long banka, Long valuta) throws NoSuchProviderException, IOException {
		checkAuthenticity();
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		Racun r = new Racun(brojRacuna, statusRacuna, k, b, v);
		r.save();
		if (session.get("mode").equals("locked add for Banka")) {
			session.put("mode", "locked add for Banka");
			session.put("s", r.id);
			session.put("b_id", banka);
			shownextBanka(); 
		} else if (session.get("mode").equals("locked add for Klijent")) {
			session.put("mode", "locked add for Klijent");
			session.put("s", r.id);
			session.put("k_id", klijent);
			shownextKlijent(); 
		} else {
			session.put("mode", "locked add for Valuta");
			session.put("s", r.id);
			session.put("v_id", valuta);
			shownextValuta(); 
		}
	}
	
	
	
	public static void edit(Long id,String brojRacuna, boolean statusRacuna, Long klijent, Long banka, Long valuta) {
				validation.minSize(brojRacuna, 18);
				validation.maxSize(brojRacuna, 18);
				validation.maxSize(statusRacuna, 1);
				if(validation.hasErrors()) {
			          validation.keep();
			          session.put("mode", "add");
			  		  session.put("s", null);
			          show();
			    }
				Long s = null;
				if(id != null) {
					Racun r = Racun.findById(id);
					r.brojRacuna = brojRacuna;
					r.statusRacuna = statusRacuna;
					Klijent k = Klijent.findById(klijent);
					r.klijent = k;
					Banka b = Banka.findById(banka);
					r.banka = b;
					Valuta v = Valuta.findById(valuta);
					r.valuta = v;
					r.save();
					s = id;
				} else if (brojRacuna != null){
					List<Racun> racuni = Racun.findAll();
					for(Racun r:racuni){
						if(brojRacuna.equals(r.brojRacuna)) {
							r.brojRacuna = brojRacuna;
							r.statusRacuna = statusRacuna;
							Klijent k = Klijent.findById(klijent);
							r.klijent = k;
							Banka b = Banka.findById(banka);
							r.banka = b;
							Valuta v = Valuta.findById(valuta);
							r.valuta = v;
							r.save();
							s = id;
						}
					}
					
				} 
				session.put("mode", "edit");
				session.put("s", s);
				show();
			}
	
	public static void editNext(Long id,String brojRacuna, boolean statusRacuna, Long klijent, Long banka, Long valuta) throws NoSuchProviderException, IOException {
		checkAuthenticity();
		validation.minSize(brojRacuna, 18);
		validation.maxSize(brojRacuna, 18);
		validation.maxSize(statusRacuna, 1);
		if(validation.hasErrors()) {
	          validation.keep();
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id != null) {
			Racun r = Racun.findById(id);
			r.brojRacuna = brojRacuna;
			r.statusRacuna = statusRacuna;
			Klijent k = Klijent.findById(klijent);
			r.klijent = k;
			Banka b = Banka.findById(banka);
			r.banka = b;
			Valuta v = Valuta.findById(valuta);
			r.valuta = v;
			r.save();
			s = id;
		} else if (brojRacuna != null){
			List<Racun> racuni = Racun.findAll();
			for(Racun r:racuni){
				if(brojRacuna.equals(r.brojRacuna)) {
					r.brojRacuna = brojRacuna;
					r.statusRacuna = statusRacuna;
					Klijent k = Klijent.findById(klijent);
					r.klijent = k;
					Banka b = Banka.findById(banka);
					r.banka = b;
					Valuta v = Valuta.findById(valuta);
					r.valuta = v;
					r.save();
					s = id;
				}
			}
			
		} 
		
		if (session.get("mode").equals("locked edit for Banka")) {
			session.put("mode", "locked edit for Banka");
			session.put("s", s);
			session.put("b_id", banka);
			shownextBanka(); 
		} else if (session.get("mode").equals("locked edit for Klijent")) {
			session.put("mode", "locked edit for Klijent");
			session.put("s", s);
			session.put("k_id", klijent);
			shownextKlijent(); 
		} else {
			session.put("mode", "locked edit for Valuta");
			session.put("s", s);
			session.put("v_id", valuta);
			shownextValuta(); 
		}
	}
	
	
	public static void delete(Long id) {
		checkAuthenticity();
	}
	
/* public static Racun encryptRacuna (String brojRacuna, String statusRacuna, Long klijent, Long banka, Long valuta) throws NoSuchProviderException {
		
		String alias = "admin";
		String passKeyS = "admin";
		String pass = "admin";
		SecretKey sk = null;
	
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		KeyStoreReader ksReader = new KeyStoreReader();
		File f = new File("C:/Users/Valentina/" + alias + ".jks");
		if(f.exists() && !f.isDirectory()) { 
			sk = ksReader.readSecretKey("C:/Users/Valentina/" + alias + ".jks", passKeyS, alias, pass);	
		}
		else {
			keyStoreWriter.loadKeyStore(null, passKeyS.toCharArray());
			sk = AES.generateKey();
			keyStoreWriter.write(alias, sk, passKeyS.toCharArray());
			keyStoreWriter.saveKeyStore("C:/Users/Valentina/" + alias + ".jks", passKeyS.toCharArray());
		}
		byte[] ebrojRacuna = AES.encrypt(brojRacuna, sk);
		byte[] estatusRacuna = AES.encrypt(statusRacuna, sk);
		
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		Racun r = new Racun(ebrojRacuna, estatusRacuna, k, b, v);
	
		return r;
		
	}
	
public static RacunD decryptRacuna (Long id, byte[] brojRacuna, byte[] statusRacuna, Long klijent, Long banka, Long valuta) throws IOException, NoSuchProviderException {
		
		String alias = "admin";
		String passKeyS = "admin";
		String pass = "admin";
		SecretKey sk = null;
	
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		KeyStoreReader ksReader = new KeyStoreReader();
		File f = new File("C:/Users/Valentina/" + alias + ".jks");
		sk = ksReader.readSecretKey("C:/Users/Valentina/" + alias + ".jks", passKeyS, alias, pass);	
		String dbrojRacuna = new String(AES.decrypt(brojRacuna, sk));
		String dstatusRacuna = new String(AES.decrypt(statusRacuna, sk));
		
		Klijent k = Klijent.findById(klijent);
		Banka b = Banka.findById(banka);
		Valuta v = Valuta.findById(valuta);
		RacunD r = new RacunD(id, dbrojRacuna, dstatusRacuna, k, b, v);
		
		return r;
		
		
	} 
*/
	public static void ukidanjeracuna(Long id, Long idPrenosa) {
		Racun zaBrisanje = Racun.findById(id);
		
		if (idPrenosa != null){
			Racun zaPrenos = Racun.findById(idPrenosa);			
			UkidanjeRacunaProcedure.ukidanjeRacuna(zaBrisanje, zaPrenos);
			show();
			
		} else {
			
		}
		
		
	}

}