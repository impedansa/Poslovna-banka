package procedures;

import java.util.Date;
import java.util.List;

import models.AnalitikaIzvoda;
import models.Banka;
import models.DnevnoStanjeRacuna;
import models.MedjubankarskiPrenos;
import models.Racun;
import models.StavkaPrenosa;

public class AnalitikaIzvodaProcedure {
	
	public static String startProcedure(AnalitikaIzvoda ai, Banka banka) {

		List<Racun> racunDuznikaList = Racun.find("byBrojRacuna", ai.racunDuznika).fetch();
		Racun racunDuznika = racunDuznikaList.size() == 0 ? null : racunDuznikaList.get(0);
		List<Racun> racunPrimaocaList = Racun.find("byBrojRacuna", ai.racunPoverioca).fetch();
		Racun racunPrimaoca = racunPrimaocaList.size() == 0 ? null : racunPrimaocaList.get(0);
		
		Integer tipNaloga = odrediTipNaloga(racunDuznika, racunPrimaoca, banka);
		System.out.println("tip naloga: " + tipNaloga);
		
		switch (tipNaloga) {
		
			case 0: 
				return "Ni jedan od racuna ne pripada " + banka.nazivBanke + ", nalog odbijen.";
			
			case 1: //Nalog za uplatu
				ai.status = "P";
				ai.tipGreske = 1;
				ai.smer = "K";
				if (racunPrimaoca.statusRacuna == true) {				
					azurirajDnevnoStanje(ai, racunPrimaoca);
					break;
				} else {
					return "Racun poverioca nije vazeci, nalog odbijen.";
				}
		
			case 2: //Nalog za isplatu
				ai.status = "P";
				ai.tipGreske = 1;
				ai.smer = "T";
				if (racunDuznika.statusRacuna == true) {
					if (proveriLikvidnostRacuna(racunDuznika, ai.iznos) != true)
						return "Racun duznika nije likvidan, nalog odbijen";
					azurirajDnevnoStanje(ai, racunDuznika);
					break;
				} else {
					return "Racun duznika nije vazeci, nalog odbijen.";
				}
				
			case 3: //Interni nalog za transfer
				ai.status = "P";
				ai.tipGreske = 1;
				ai.smer = "K";
				if (racunDuznika.statusRacuna == true && racunPrimaoca.statusRacuna == true){
					if (proveriLikvidnostRacuna(racunDuznika, ai.iznos) != true)
						return "Racun duznika nije likvidan, nalog odbijen";
			
					//azuriraj racun primaoca
					azurirajDnevnoStanje(ai, racunPrimaoca);

					// zaduzi racun duznika
					AnalitikaIzvoda aiTeret = new AnalitikaIzvoda(ai.datumPrijema, "T",
							ai.duznikNalogodavac, ai.svrhaPlacanja, ai.poverilacPrimalac, ai.datumPrijema, ai.datumValute,
							ai.racunDuznika, ai.modelZaduzenja, ai.pozivNaBrojZaduzenja, ai.racunPoverioca,
							ai.modelOdobrenja, ai.pozivNaBrojOdobrenja, ai.hitno, ai.iznos, ai.tipGreske, ai.status, null);

					azurirajDnevnoStanje(aiTeret, racunDuznika);
					break;
				} else {
				return "Nevazeci racun, nalog odbijen";
				}
			
			case 4: //Medjubankarski nalog za prenos, zaduzi racun duznika
				ai.smer = "T";
				ai.status = "P";
				ai.tipGreske = 1;
				if (racunDuznika.statusRacuna == true){
					if (proveriLikvidnostRacuna(racunDuznika, ai.iznos) != true)
						return "Racun duznika nije likvidan, nalog odbijen";
			
					//zaduzi racun duznika
					azurirajDnevnoStanje(ai, racunDuznika);

					// kreiraj stavku medjubankarskog transfera u korist poverioca iz druge banke
					AnalitikaIzvoda aiTeret = new AnalitikaIzvoda(ai.datumPrijema, "K",
							ai.duznikNalogodavac, ai.svrhaPlacanja, ai.poverilacPrimalac, ai.datumPrijema, ai.datumValute,
							ai.racunDuznika, ai.modelZaduzenja, ai.pozivNaBrojZaduzenja, ai.racunPoverioca,
							ai.modelOdobrenja, ai.pozivNaBrojOdobrenja, ai.hitno, ai.iznos, ai.tipGreske, "E", null);
					
					aiTeret.save();
					kreirajStavkuMedjubankarskogTransfera(aiTeret, racunDuznika, racunPrimaoca);
					break;
				} else {
				return "Nevazeci racun, nalog odbijen";
				}
				
			case 5: //Medjubankarski nalog za naplatu, azuriraj racun poverioca
				ai.smer = "K";
				ai.status = "P";
				ai.tipGreske = 1;
				if (racunDuznika.statusRacuna == true){
					
					//azuriraj racun primaoca
					azurirajDnevnoStanje(ai, racunPrimaoca);

					// kreiraj stavku medjubankarskog transfera na teret duznika iz druge banke
					AnalitikaIzvoda aiTeret = new AnalitikaIzvoda(ai.datumPrijema, "T",
							ai.duznikNalogodavac, ai.svrhaPlacanja, ai.poverilacPrimalac, ai.datumPrijema, ai.datumValute,
							ai.racunDuznika, ai.modelZaduzenja, ai.pozivNaBrojZaduzenja, ai.racunPoverioca,
							ai.modelOdobrenja, ai.pozivNaBrojOdobrenja, ai.hitno, ai.iznos, ai.tipGreske, "E", null);

					aiTeret.save();
					kreirajStavkuMedjubankarskogTransfera(aiTeret, racunDuznika, racunPrimaoca);
					break;
				} else {
				return "Nevazeci racun, nalog odbijen";
				}
				
		}
		return "Nalog uspesno obradjen.";	
	}

	public static Date nadjiPoslednjeDnevnoStanje(Racun racun) {
		List<DnevnoStanjeRacuna> dnevnoStanje = DnevnoStanjeRacuna.find("byRacunOrderByDatum", racun)
				.fetch();
		if (dnevnoStanje.size() == 0)
			return null;
		else
			return dnevnoStanje.get(dnevnoStanje.size() - 1).datum;
	}
	
	public static DnevnoStanjeRacuna dobaviPoslednjeDnevnoStanje(Racun racun) {
		List<DnevnoStanjeRacuna> dnevnoStanje = DnevnoStanjeRacuna.find("byRacunOrderByDatum", racun)
				.fetch();
		if (dnevnoStanje.size() == 0)
			return null;
		else
			return dnevnoStanje.get(dnevnoStanje.size() - 1);
	}
	
	private static void azurirajDnevnoStanje(AnalitikaIzvoda ai, Racun racun){
		
		if (ai.smer.equals("K")) { //U KORIST
			Date datumPoslednjegPrometa = nadjiPoslednjeDnevnoStanje(racun);
			if (datumPoslednjegPrometa == null) {
				// na racunu nije bilo nikakvog prometa do sad,
				// kreiramo prvo dnevno stanje za racun klijenta
				DnevnoStanjeRacuna dnevnoStanje = new DnevnoStanjeRacuna(ai.datumPrijema, 0L, ai.iznos, 0L,
						ai.iznos, racun);
				dnevnoStanje.save();
				ai.dnevnoStanjeRacuna = dnevnoStanje;
			} else if (datumPoslednjegPrometa.before(ai.datumPrijema)) {
				// datum poslednjeg dnevnog stanja (poslednjeg prometa) je manji od datuma prijema naloga
				// kreiramo novo dnevno stanje i azuriramo ga sa prethodnim dnevnim stanjem
				DnevnoStanjeRacuna prethodnoDnevnoStanje = (DnevnoStanjeRacuna) DnevnoStanjeRacuna
						.find("byDatumPrometaAndRacunPravnihLica", datumPoslednjegPrometa, racun).fetch()
						.get(0);
				DnevnoStanjeRacuna novoDnevnoStanje = new DnevnoStanjeRacuna(ai.datumPrijema,
						prethodnoDnevnoStanje.novoStanje, ai.iznos, 0L, prethodnoDnevnoStanje.novoStanje + ai.iznos,
						racun);
				novoDnevnoStanje.save();
				ai.dnevnoStanjeRacuna = novoDnevnoStanje;
			} else {
				// na datum prijema naloga je vec bilo transakcija pa postoji dnevno stanje za taj datum
				// azuriramo aktuelno dnevno stanje racuna
				DnevnoStanjeRacuna aktuelnoDnevnoStanje = (DnevnoStanjeRacuna) DnevnoStanjeRacuna
						.find("byDatumPrometaAndRacun", datumPoslednjegPrometa, racun).fetch()
						.get(0);
				aktuelnoDnevnoStanje.prometUKorist += ai.iznos;
				aktuelnoDnevnoStanje.novoStanje += ai.iznos;
				aktuelnoDnevnoStanje.save();
				ai.dnevnoStanjeRacuna = aktuelnoDnevnoStanje;
			}
			ai.save();
		} else { // NA TERET
			Date datumPoslednjegPrometaDuznika = nadjiPoslednjeDnevnoStanje(racun);
			if (datumPoslednjegPrometaDuznika == null) {
				// na racunu nije bilo nikakvog prometa do sad,
				// kreiramo prvo dnevno stanje za racun klijenta
				DnevnoStanjeRacuna dnevnoStanje = new DnevnoStanjeRacuna(ai.datumPrijema, 0L, 0L, ai.iznos, 
						-ai.iznos, racun);
				dnevnoStanje.save();
				ai.dnevnoStanjeRacuna = dnevnoStanje;
			} else if (datumPoslednjegPrometaDuznika.before(ai.datumPrijema)) {
				// datum poslednjeg dnevnog stanja (poslednjeg prometa) je
				// manji od datuma prijema naloga
				// kreiramo novo dnevno stanje i azuriramo ga sa prethodnim
				// dnevnim stanjem
				DnevnoStanjeRacuna prethodnoDnevnoStanje = (DnevnoStanjeRacuna) DnevnoStanjeRacuna
						.find("byDatumAndRacun", datumPoslednjegPrometaDuznika, racun)
						.fetch().get(0);
				DnevnoStanjeRacuna novoDnevnoStanje = new DnevnoStanjeRacuna(ai.datumPrijema,
						prethodnoDnevnoStanje.novoStanje, 0L, ai.iznos, prethodnoDnevnoStanje.novoStanje - ai.iznos,
						racun);
				novoDnevnoStanje.save();
				ai.dnevnoStanjeRacuna = novoDnevnoStanje;
			} else {
				// na datum prijema naloga je vec bilo transakcija pa
				// postoji dnevno stanje za taj datum
				// azuriramo aktuelno dnevno stanje racuna
				DnevnoStanjeRacuna aktuelnoDnevnoStanje = (DnevnoStanjeRacuna) DnevnoStanjeRacuna
						.find("byDatumAndRacun", datumPoslednjegPrometaDuznika, racun)
						.fetch().get(0);
				aktuelnoDnevnoStanje.prometNaTeret += ai.iznos;
				aktuelnoDnevnoStanje.novoStanje -= ai.iznos;
				aktuelnoDnevnoStanje.save();
				ai.dnevnoStanjeRacuna = aktuelnoDnevnoStanje;
			}
			ai.save();
		}
	}
	
	private static void kreirajStavkuMedjubankarskogTransfera(AnalitikaIzvoda ai, Racun racunDuznika, Racun racunPrimaoca) {
		//kreiranje stavke medjubankarskog prometa za racun poverioca
		if (ai.hitno == true || ai.iznos >= 250000L) {
			// MT-103 - RTGS
			// za svaki hitan nalog se kreira nova stavka
			// medjubankarskog prometa
			MedjubankarskiPrenos mp = null;
			if (ai.smer.equals("K")) {
				mp = new MedjubankarskiPrenos("103", ai.datumPrijema, ai.iznos,
					racunDuznika.banka, racunPrimaoca.banka);
			} else {
				mp = new MedjubankarskiPrenos("103", ai.datumPrijema, -ai.iznos,
					racunPrimaoca.banka, racunDuznika.banka);
			}
			mp.save();
			StavkaPrenosa sp = new StavkaPrenosa(ai, mp);
			sp.save();

		} else {
			// MT-102 - Clearing
			List<MedjubankarskiPrenos> mp = null;
			if (ai.smer.equals("K")){
				mp = MedjubankarskiPrenos.find("byBankaPrimalacAndVrstaPoruke", racunPrimaoca.banka, "102").fetch();
			} else {
				mp = MedjubankarskiPrenos.find("byBankaPrimalacAndVrstaPoruke", racunDuznika.banka, "102").fetch();
			}				
			if (mp.size() == 0) {
				// ne postoji paket naloga na cekanju clearinga za
				// racun primaoca
				// kreiramo novi paket naloga za clearing
				MedjubankarskiPrenos novimp = null;
				if (ai.smer.equals("K"))
					novimp = new MedjubankarskiPrenos("102", ai.datumPrijema, ai.iznos,
						racunDuznika.banka, racunPrimaoca.banka);
				else {
					novimp = new MedjubankarskiPrenos("102", ai.datumPrijema, -ai.iznos,
							racunPrimaoca.banka, racunDuznika.banka);
				}
				novimp.save();
				StavkaPrenosa sp = new StavkaPrenosa(ai, novimp);
				sp.save();
			} else {
				// vec postoji paket naloga na cekanju clearinga za
				// racun banke				
				if (ai.smer.equals("K")){
					mp.get(0).ukupanIznos += ai.iznos;
					mp.get(0).save();
					StavkaPrenosa sp = new StavkaPrenosa(ai, mp.get(0));
					sp.save();
				}
				else {
					mp.get(0).ukupanIznos -= ai.iznos;
					mp.get(0).save();
					StavkaPrenosa sp = new StavkaPrenosa(ai, mp.get(0));
					sp.save();
				}	
			}
		}
	}
	
	private static boolean proveriLikvidnostRacuna(Racun racun, Long iznos) {
		Date datumPoslednjegPrometa = nadjiPoslednjeDnevnoStanje(racun);
		if (datumPoslednjegPrometa == null) {
			return false;
		} 
		else {
			DnevnoStanjeRacuna aktuelnoDnevnoStanje = (DnevnoStanjeRacuna) DnevnoStanjeRacuna
					.find("byDatumAndRacun", datumPoslednjegPrometa, racun)
					.fetch().get(0);
			if (aktuelnoDnevnoStanje.novoStanje >= iznos)
				return true;
			else 
				return false;
		}
		
	}
	
	
	private static Integer odrediTipNaloga(Racun racunDuznika, Racun racunPrimaoca, Banka banka) {
		Integer result = 0;
		if (racunDuznika == null){
			if (racunPrimaoca.brojRacuna.startsWith(banka.sifraBanke))
				result = 1; //nalog za uplatu
			return result;
		} else if (racunPrimaoca == null) {
			if (racunDuznika.brojRacuna.startsWith(banka.sifraBanke))
				result = 2; //nalog za isplatu
			return result;
		} else if (racunDuznika.brojRacuna.startsWith(banka.sifraBanke) && racunPrimaoca.brojRacuna.startsWith(banka.sifraBanke)) {
			result = 3; //interni nalog za prenos/naplatu
			return result;
	    } else if (racunDuznika.brojRacuna.startsWith(banka.sifraBanke) && !racunPrimaoca.brojRacuna.startsWith(banka.sifraBanke)) {
			result = 4; //medjubankarski nalog, zaduzi racun duznika
			return result;
	    } else if (!racunDuznika.brojRacuna.startsWith(banka.sifraBanke) && racunPrimaoca.brojRacuna.startsWith(banka.sifraBanke)) {
			result = 5;//medjubankarski nalog, azuriraj racun primaoca
			return result;
	    }
		return result;
	}

}
