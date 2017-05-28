package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class DnevnoStanjeRacuna extends Model {
	
	@Column(name="DATUM")
	public Date datum;
	
	@Column(name="PRETHODNO_STANJE")
	public float prethodnoStanje;
	
	@Column(name="PROMET_NA_TERET")
	public float prometNaTeret;
	
	@Column(name="PROMET_U_KORIST")
	public float prometUKorist;
	
	@Column(name="NOVO_STANJE")
	public float novoStanje;
	
	@ManyToOne
	public Racun racun;
	
	DnevnoStanjeRacuna(Date datum, float prometNaTeret, float PrometUKorist, float novoStanje, Racun racun) {
		
		super();
		this.datum = datum;
		this.prometNaTeret = prometNaTeret;
		this.prometUKorist = PrometUKorist;
		this.novoStanje = novoStanje;
		this.racun = racun;
		
	}

}
