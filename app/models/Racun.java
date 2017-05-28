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

}
