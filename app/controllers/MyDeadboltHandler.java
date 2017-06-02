package controllers;

import models.Zaposleni;
import models.deadbolt.RoleHolder;
import play.mvc.Scope.Session;
import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;

public class MyDeadboltHandler implements DeadboltHandler {
	
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
