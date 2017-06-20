package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.db.jpa.Model;

@Entity
public class Zaposleni extends Model implements RoleHolder {
	
	@Column(name="KORISNICKO_IME", unique = true) 
	public String korisnickoIme;
	
	@Column(name="LOZINKA", unique = true)
	public String lozinka;
	
	
	public Zaposleni(String korisnickoIme, String lozinka) {
		
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;

	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}
	

	@Override
	public List<? extends Role> getRoles() {
		
		String idZap = play.mvc.Scope.Session.current().get("user");
		Long id = Long.parseLong(idZap);
		
		Zaposleni zaposleni = Zaposleni.findById(id);
		
		List<UlogaZaposlenog> ulogeZaposlenog = UlogaZaposlenog.find("byZaposleni", zaposleni).fetch();
		
		List<Uloga> uloge = new ArrayList<Uloga>();
		
		for(int i=0; i<ulogeZaposlenog.size(); i++) {
			uloge.add(ulogeZaposlenog.get(i).getUloga());
		}
		
		List<PermisijaUloge> permisijeUloga = PermisijaUloge.find("byUloga", uloge).fetch();

		List<Permisija> permisije = new ArrayList<Permisija>();
		
		for(int i=0; i<permisijeUloga.size(); i++) {
			permisije.add(permisijeUloga.get(i).getPermisija());
		}
		
		System.out.println("Usao");
		for(int i=0; i<permisijeUloga.size(); i++)
			System.out.println(permisije.get(i).getRoleName());
		
		return permisije;
	}

}
