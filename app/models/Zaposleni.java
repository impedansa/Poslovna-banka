package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Zaposleni extends Model{
	
	@Column(name="KORISNICKO_IME")
	public String korisnickoIme;
	
	@Column(name="LOZINKA")
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

}
