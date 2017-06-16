package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class DnevnoStanjeRacuna extends Model {
	
	@Column(name="DATUM", nullable = false)
	public Date datum;
	
	@Column(name="PRETHODNO_STANJE", nullable = false)
	public long prethodnoStanje;
	
	@Column(name="PROMET_NA_TERET", nullable = false)
	public long prometNaTeret;
	
	@Column(name="PROMET_U_KORIST", nullable = false)
	public long prometUKorist;
	
	@Column(name="NOVO_STANJE", nullable = false)
	public long novoStanje;
	
	@ManyToOne
	public Racun racun;
	
	@OneToMany(mappedBy = "dnevnoStanjeRacuna", cascade=CascadeType.ALL)
	public List<AnalitikaIzvoda> analitikaIzvoda;
	
	DnevnoStanjeRacuna(Date datum, long prometNaTeret, long PrometUKorist, long novoStanje, Racun racun) {
		
		super();
		this.datum = datum;
		this.prometNaTeret = prometNaTeret;
		this.prometUKorist = PrometUKorist;
		this.novoStanje = novoStanje;
		this.racun = racun;
		
	}

}
