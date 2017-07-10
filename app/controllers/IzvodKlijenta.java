package controllers;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import models.Banka;
import models.Klijent;
import models.Zaposleni;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;

@With(Deadbolt.class)
public class IzvodKlijenta extends Controller{
	public static void show() {
		List<Banka> banke = Banka.findAll();
		List<Klijent> klijenti = Klijent.findAll();
		render(banke, klijenti);
	}
	
	public static void getReport(Long banka, Long klijent, Date pocetakIntervala, Date krajIntervala){
		Map reportParams = new HashMap(4);
		reportParams.put("id_Banke", banka);
		reportParams.put("id_Klijenta", klijent);
		reportParams.put("datumPocetka", pocetakIntervala);
		reportParams.put("datumKraja", krajIntervala);
		
	    //jrxml and jasper files should be in app/reports
		Klijent k = Klijent.findById(klijent);
	    renderBinary(reports.BaseJasperReport.generateReport("IzvodKlijenta", reportParams), "IzvodKlijenta_" + k.naziv + ".pdf");
	}
	
}
