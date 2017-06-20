package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class ZatvaranjeRacunaD {
	
	public Long id;
	
	
	public Date datumZatvaranja;
	
	
	public String prebacenoNaRacun;
	

	public Racun racun;
	
	
	public AnalitikaIzvoda analitikaIzvoda;


	public ZatvaranjeRacunaD(Long id, Date datumZatvaranja, String prebacenoNaRacun, Racun racun,
			AnalitikaIzvoda analitikaIzvoda) {
		super();
		this.id = id;
		this.datumZatvaranja = datumZatvaranja;
		this.prebacenoNaRacun = prebacenoNaRacun;
		this.racun = racun;
		this.analitikaIzvoda = analitikaIzvoda;
	}
	
	

}
