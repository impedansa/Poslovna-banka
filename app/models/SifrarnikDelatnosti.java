package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class SifrarnikDelatnosti extends Model {
	
	@Column(name="SIFRA_DELATNOSTI", length=5)
	public int sifraDelatnosti;
	
	@Column(name="NAZIV_DELATNOSTI", length=60)
	public String nazivDelatnosti;

	public SifrarnikDelatnosti(int sifraDelatnosti, String nazivDelatnosti) {
		super();
		this.sifraDelatnosti = sifraDelatnosti;
		this.nazivDelatnosti = nazivDelatnosti;
	}

}
