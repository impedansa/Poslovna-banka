package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Racun extends Model {
	
	@Column(name="BROJ_RACUNA", length=18)
	public String brojRacuna;
	
	@Column(name="STATUS_RACUNA", length=1)
	public String statusRacuna;
	
	@ManyToOne
	public Klijent klijent;
	
	@ManyToOne
	public Banka banka;
	
	@ManyToOne
	public Valuta valuta;

	public Racun(String brojRacuna, String statusRacuna, Klijent klijent, Banka banka, Valuta valuta) {
		super();
		this.brojRacuna = brojRacuna;
		this.statusRacuna = statusRacuna;
		this.klijent = klijent;
		this.banka = banka;
		this.valuta = valuta;
	}

	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public String getStatusRacuna() {
		return statusRacuna;
	}

	public void setStatusRacuna(String statusRacuna) {
		this.statusRacuna = statusRacuna;
	}

	public Klijent getKlijent() {
		return klijent;
	}

	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}

	public Banka getBanka() {
		return banka;
	}

	public void setBanka(Banka banka) {
		this.banka = banka;
	}

	public Valuta getValuta() {
		return valuta;
	}

	public void setValuta(Valuta valuta) {
		this.valuta = valuta;
	}
	

}
