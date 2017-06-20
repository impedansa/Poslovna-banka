package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Klijent extends Model {
	
	@Column(name="JMBG", length=13)
	public byte[] jmbg;
	
	@Column(name="PIB")
	public byte[] pib;
	
	@Column(name="NAZIV")
	public byte[] naziv;
	
	@Column(name="ADRESA")
	public byte[] adresa;
	
	@Column(name="TELEFON")
	public byte[] telefon;
	
	@Column(name="E_MAIL")
	public byte[] eMail;
	
	@Column(name="FAX")
	public byte[] fax;
	
	@Column(name="TIP_LICA", length=1)
	public byte[] tipLica;

	@ManyToOne
	public SifrarnikDelatnosti sifrarnikDelatnosti;
	
	public Klijent(byte[] jmbg, byte[] pib, byte[] naziv, byte[] adresa,
			byte[] telefon, byte[] eMail, byte[] fax, byte[] tipLica, SifrarnikDelatnosti sifrarnikDelatnosti) {
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

	public byte[] getJmbg() {
		return jmbg;
	}

	public void setJmbg(byte[] jmbg) {
		this.jmbg = jmbg;
	}

	public byte[] getPib() {
		return pib;
	}

	public void setPib(byte[] pib) {
		this.pib = pib;
	}

	public byte[] getNaziv() {
		return naziv;
	}

	public void setNaziv(byte[] naziv) {
		this.naziv = naziv;
	}

	public byte[] getAdresa() {
		return adresa;
	}

	public void setAdresa(byte[] adresa) {
		this.adresa = adresa;
	}

	public byte[] getTelefon() {
		return telefon;
	}

	public void setTelefon(byte[] telefon) {
		this.telefon = telefon;
	}

	public byte[] geteMail() {
		return eMail;
	}

	public void seteMail(byte[] eMail) {
		this.eMail = eMail;
	}

	public byte[] getFax() {
		return fax;
	}

	public void setFax(byte[] fax) {
		this.fax = fax;
	}

	public byte[] getTipLica() {
		return tipLica;
	}

	public void setTipLica(byte[] tipLica) {
		this.tipLica = tipLica;
	}

	public SifrarnikDelatnosti getSifrarnikDelatnosti() {
		return sifrarnikDelatnosti;
	}

	public void setSifrarnikDelatnosti(SifrarnikDelatnosti sifrarnikDelatnosti) {
		this.sifrarnikDelatnosti = sifrarnikDelatnosti;
	}

	


}
