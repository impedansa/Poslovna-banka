package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;

import play.db.jpa.Model;

@Entity
public class Drzava extends Model{

	@Column(name= "DR_OZNAKA", length = 3)
	public String oznaka;
	
	@Column(name= "DR_NAZIV")
	@Length(min=3, max=40)
	public String naziv;

	public Drzava(String oznaka, String naziv) {
		super();
		this.oznaka = oznaka;
		this.naziv = naziv;
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	
}
