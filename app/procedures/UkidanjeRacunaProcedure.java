package procedures;

import java.util.Date;

import controllers.ZatvaranjaRacuna;
import models.AnalitikaIzvoda;
import models.DnevnoStanjeRacuna;
import models.Racun;
import models.ZatvaranjeRacuna;

public class UkidanjeRacunaProcedure {
	
	public static void ukidanjeRacuna(Racun zaUkidanje, Racun zaPrenos) {
		Date datumPrijema = new Date();
	
		if (zaUkidanje.banka.equals(zaPrenos.banka)){
			//interni prenos
			
			Date datumPoslPromRacZaUkidanje = AnalitikaIzvodaProcedure.nadjiPoslednjeDnevnoStanje(zaUkidanje);
			if (datumPoslPromRacZaUkidanje == null ) {
				// na racunu nije bilo nikakvog prometa do sad,
				System.out.println("Na racunu za ukidanje nije bilo prometa.");
			} else {
				// trazimo poslednje dnevno stanje racuna za ukidanje				
				DnevnoStanjeRacuna poslednjeDSRukidanje = (DnevnoStanjeRacuna) DnevnoStanjeRacuna
						.find("byDatumAndRacun", datumPoslPromRacZaUkidanje, zaUkidanje).fetch()
						.get(0);
				if (poslednjeDSRukidanje.novoStanje > 0L){	
					// kreiramo analitiku sa iznosom trenutnog stanja racuna za ukidanje
					AnalitikaIzvoda ai = new AnalitikaIzvoda(
							datumPrijema,
							"K", 
							zaUkidanje.klijent.naziv, 
							"Zatvaranje racuna", 
							zaPrenos.klijent.naziv, 
							datumPrijema, 
							datumPrijema, 
							zaUkidanje.brojRacuna, 
							50,
							"1234",
							zaPrenos.brojRacuna,
							50,
							"1234",
							true,
							poslednjeDSRukidanje.novoStanje,
							1,
							"P",
							null);
					
					AnalitikaIzvodaProcedure.startProcedure(ai, zaUkidanje.banka);
					
					ZatvaranjeRacuna u = new ZatvaranjeRacuna(datumPrijema, zaPrenos.brojRacuna, zaUkidanje, ai);
					u.save();
					
					zaUkidanje.statusRacuna = false;
					zaUkidanje.save();
					
				} else {
					System.out.println("Stanje racuna je u minusu");
				}				
			}		
		} else {
			
		}
		
		zaUkidanje.statusRacuna = false;
		zaUkidanje.save();
		
	}

}
