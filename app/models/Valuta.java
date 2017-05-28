package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Valuta extends Model {
	
	@Column(name="SIFRA_VALUTE", length=3)
	public String sifraValute;

	@Column(name="NAZIV_VALUTE", length=30)
	public String nazivValute;

	public Valuta(String sifraValute, String nazivValute) {
		super();
		this.sifraValute = sifraValute;
		this.nazivValute = nazivValute;
	}
	
	
}
