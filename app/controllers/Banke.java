package controllers;

import java.util.List;

import models.Banka;
import play.data.validation.Required;
import play.mvc.Controller;

public class Banke extends Controller{
	public static void show(){
		List<Banka> banke = Banka.findAll();
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
		render(banke);
	}
	
	public static void create(@Required String nazivBanke, @Required String sifraBanke, @Required String swiftKod, @Required String obracunskiRacun) {
		checkAuthenticity();
		validation.maxSize(swiftKod, 8);
		validation.maxSize(obracunskiRacun, 18);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "add");
	  		  session.put("s", null);
	          show();
	    }
		Banka banka = new Banka(nazivBanke, sifraBanke, swiftKod, obracunskiRacun);
		banka.save();
		
		session.put("mode", "add");
 		session.put("s", banka.id);
		show();
	}
	
	public static void edit(Long id, String nazivBanke, String sifraBanke, String swiftKod, String obracunskiRacun) {
		checkAuthenticity();
		validation.maxSize(swiftKod, 8);
		validation.maxSize(obracunskiRacun, 18);
		if(validation.hasErrors()) {
	          validation.keep(); 
	          session.put("mode", "");
	  		  session.put("s", null);
	          show();
	    }
		Long s = null;
		if(id!=null){
			Banka banka = Banka.findById(id);
			banka.nazivBanke = nazivBanke;
			banka.sifraBanke = sifraBanke;
			banka.swiftKod = swiftKod;
			banka.obracunskiRacun = obracunskiRacun;
			banka.save();
			s = id;
		} else if (!sifraBanke.equals("0")) {
			List<Banka> banke = Banka.findAll();
			for(Banka banka: banke) {
				if(sifraBanke == banka.sifraBanke){
					banka.nazivBanke = nazivBanke;
					banka.sifraBanke = sifraBanke;
					banka.swiftKod = swiftKod;
					banka.obracunskiRacun = obracunskiRacun;
					banka.save();
					s = banka.id;
				}
			}

		}
		
		session.put("mode", "edit");
 		session.put("s", s);
		show();
	}
	
	public static void filter(String nazivBanke, int sifraBanke, String swiftKod, String obracunskiRacun) {
		List<Banka> banke;
		if(sifraBanke==0)
			banke = Banka.find("byNazivBankeLikeAndSwiftKodLikeAndObracunskiRacunLike", "%"+nazivBanke+"%", "%"+swiftKod+"%", "%"+obracunskiRacun+"%").fetch();
		else
			banke = Banka.find("byNazivBankeLikeAndSifraBankeAndSwiftKodLikeAndObracunskiRacunLike", "%"+nazivBanke+"%", sifraBanke, "%"+swiftKod+"%", "%"+obracunskiRacun+"%").fetch();
		session.put("mode", "edit");
 		session.put("s", null);
		renderTemplate("Banke/show.html", banke);
	}
	
	public static void delete(Long id) {
		checkAuthenticity();
		Long s = null;
		List<Banka> banke = Banka.findAll();
		for(int i=0; i< banke.size(); i++) {
			if(banke.get(i).getId().equals(id)){
				s = banke.get(i-1).getId();
			}
		}
		Banka banka = Banka.findById(id);
		banka.delete(); 
		
		session.put("mode", "edit");
		session.put("s", s);
		show();
	}
}
