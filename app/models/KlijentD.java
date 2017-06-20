package models;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class KlijentD {
	
	public Long id;
	
	public String jmbg;
	

	public String pib;
	
	
	public String naziv;
	
	
	public String adresa;
	
	
	public String telefon;
	
	public String eMail;
	
	public String fax;
	
	public String tipLica;

	public SifrarnikDelatnosti sifrarnikDelatnosti;

	public KlijentD(Long id, String jmbg, String pib, String naziv, String adresa, String telefon, String eMail,
			String fax, String tipLica, SifrarnikDelatnosti sifrarnikDelatnosti) {
		super();
		this.id = id;
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
