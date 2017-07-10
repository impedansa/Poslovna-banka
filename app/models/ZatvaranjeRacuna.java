package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class ZatvaranjeRacuna extends Model {
	
	@Column(name="DATUM_ZATVARANJA")
	public Date datumZatvaranja;
	
	@Column(name="PREBACENO_NA_RACUN", length=18)
	public String prebacenoNaRacun;
	
	@ManyToOne
	public Racun racun;
	
	@ManyToOne
	public AnalitikaIzvoda analitikaIzvoda;

	public ZatvaranjeRacuna(Date datumZatvaranja, String prebacenoNaRacun, Racun racun, AnalitikaIzvoda analitikaIzvoda) {
		super();
		this.datumZatvaranja = datumZatvaranja;
		this.prebacenoNaRacun = prebacenoNaRacun;
		this.racun = racun;
		this.analitikaIzvoda = analitikaIzvoda;
	}

}
