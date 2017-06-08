package controllers;

import java.util.List;

import models.AnalitikaIzvoda;
import play.mvc.Controller;

public class AnalitikeIzvoda extends Controller {
	
	public static void show(String mode){
			List<AnalitikaIzvoda> analitikeIzvoda = AnalitikaIzvoda.findAll();
			if (mode == null || mode.equals(""))
				mode = "edit";
			render(analitikeIzvoda, mode);
		}
	
	public static void filter() {
		
	}

}
