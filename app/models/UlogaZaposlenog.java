package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class UlogaZaposlenog extends Model {
	
	@ManyToOne
	public Zaposleni zaposleni;
	
	@ManyToOne
	public Uloga uloga;

	public UlogaZaposlenog(Zaposleni zaposleni, Uloga uloga) {
		super();
		this.zaposleni = zaposleni;
		this.uloga = uloga;
	}

	public Zaposleni getZaposleni() {
		return zaposleni;
	}

	public void setZaposleni(Zaposleni zaposleni) {
		this.zaposleni = zaposleni;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

}
