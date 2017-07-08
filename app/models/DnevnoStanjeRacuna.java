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
	public Long prethodnoStanje;
	
	@Column(name="PROMET_NA_TERET", nullable = false)
	public Long prometNaTeret;
	
	@Column(name="PROMET_U_KORIST", nullable = false)
	public Long prometUKorist;
	
	@Column(name="NOVO_STANJE", nullable = false)
	public Long novoStanje;
	
	@ManyToOne
	public Racun racun;
	
	@OneToMany(mappedBy = "dnevnoStanjeRacuna", cascade=CascadeType.ALL)
	public List<AnalitikaIzvoda> analitikaIzvoda;
	
	public DnevnoStanjeRacuna(Date datum, Long prethodnoStanje, Long prometNaTeret, Long PrometUKorist, Long novoStanje, Racun racun) {
		
		super();
		this.datum = datum;
		this.prethodnoStanje = prethodnoStanje;
		this.prometNaTeret = prometNaTeret;
		this.prometUKorist = PrometUKorist;
		this.novoStanje = novoStanje;
		this.racun = racun;
		
	}

}
