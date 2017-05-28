package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

import play.db.jpa.Model;

@Entity
public class NaseljenoMesto extends Model{

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

	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	public Drzava getDrzava() {
		return drzava;
	}

	public void setDrzava(Drzava drzava) {
		this.drzava = drzava;
	}

	@Column(name= "NM_OZNAKA", length = 3)
	public String oznaka;
	
	@Column(name= "NM_NAZIV")
	@Length(min=3, max=40)
	public String naziv;
	
	@Column(name= "NM_PTTOZNAKA", length = 5)
	public int postanskiBroj;
	
	@ManyToOne
	public Drzava drzava;

	public NaseljenoMesto(String oznaka, String naziv, int postanskiBroj,
			Drzava drzava) {
		super();
		this.oznaka = oznaka;
		this.naziv = naziv;
		this.postanskiBroj = postanskiBroj;
		this.drzava = drzava;
	}
	
}

