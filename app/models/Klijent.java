package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Klijent extends Model {
	
	@Column(name="JMBG", length=13)
	public String jmbg;
	
	@Column(name="PIB")
	public int pib;
	
	@Column(name="NAZIV")
	public String naziv;
	
	@Column(name="ADRESA")
	public String adresa;
	
	@Column(name="TELEFON")
	public String telefon;
	
	@Column(name="E_MAIL")
	public String eMail;
	
	@Column(name="FAX")
	public String fax;
	
	@Column(name="TIP_LICA", length=1)
	public String tipLica;

	@ManyToOne
	public SifrarnikDelatnosti sifrarnikDelatnosti;
	
	public Klijent(String jmbg, int pib, String naziv, String adresa,
			String telefon, String eMail, String fax, String tipLica, SifrarnikDelatnosti sifrarnikDelatnosti) {
		super();
		this.jmbg = jmbg;
		this.pib = pib;
		this.naziv = naziv;
		this.adresa = adresa;
		this.telefon = telefon;
		this.eMail = eMail;
		this.fax = fax;
		this.tipLica = tipLica;
		this.sifrarnikDelatnosti = sifrarnikDelatnosti;
	}

}
