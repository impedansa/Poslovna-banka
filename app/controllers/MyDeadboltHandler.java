package controllers;

import models.Zaposleni;
import models.deadbolt.RoleHolder;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;

public class MyDeadboltHandler extends Controller implements DeadboltHandler {
	
	public MyDeadboltHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void beforeRoleCheck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RoleHolder getRoleHolder() {
		String user = Session.current().get("user");
		Long id = Long.parseLong(user);
		Zaposleni korisnik = Zaposleni.findById(id);
		System.out.println(korisnik.korisnickoIme.toString());
		return korisnik;
	}

	@Override
	public void onAccessFailure(String controllerClassName) {
		// TODO Auto-generated method stub
		Logger.info("Zaposleni sa ID-jem: "+ session.get("user") + " pokusao neovlascenu operaciju nad: " + controllerClassName + " sa IP adrese: " + Secure.getClientIp());
		redirect("http://localhost:8080");
		
	}

	@Override
	public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestrictedResourcesHandler getRestrictedResourcesHandler() {
		// TODO Auto-generated method stub
		return null;
	}

}
