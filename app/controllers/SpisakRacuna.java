package controllers;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Banka;
import models.Zaposleni;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;

@With(Deadbolt.class)
public class SpisakRacuna extends Controller{
	public static void show() {
		List<Banka> banke = Banka.findAll();
		render(banke);
	}
	
	/*public static void getReport(Long banka, Date datum){
		Map reportParams = new HashMap(2);
		reportParams.put("id_Banke", banka);
		reportParams.put("datum", datum);
	    //jrxml and jasper files should be in app/reports
		Calendar cal = Calendar.getInstance();
	    cal.setTime(datum);
	    renderBinary(reports.BaseJasperReport.generateReport("SpisakRacuna", reportParams), "SpisakRacuna_" + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH)+1) + ".pdf");
	}*/
	
}
