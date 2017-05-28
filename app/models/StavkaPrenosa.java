package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class StavkaPrenosa extends Model {
	
	@ManyToOne
	public AnalitikaIzvoda analitikaIzvoda;
	
	@ManyToOne
	public MedjubankarskiPrenos medjubankarskiPrenos;

	public StavkaPrenosa(AnalitikaIzvoda analitikaIzvoda,
			MedjubankarskiPrenos medjubankarskiPrenos) {
		super();
		this.analitikaIzvoda = analitikaIzvoda;
		this.medjubankarskiPrenos = medjubankarskiPrenos;
	}
	
}
