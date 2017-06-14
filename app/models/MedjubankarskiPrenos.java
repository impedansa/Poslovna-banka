package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class MedjubankarskiPrenos extends Model {
	
	@Column(name="VRSTA_PORUKE", length=6)
	public String vrstaPoruke;
	
	@Column(name="DATUM_")
	public Date datum;
	
	@Column(name="UKUPAN_IZNOS")
	public Long ukupanIznos;
	
	@ManyToOne
	public Banka bankaPosiljalac;
	
	@ManyToOne
	public Banka bankaPrimalac;

	public MedjubankarskiPrenos(String vrstaPoruke, Date datum,
			Long ukupanIznos, Banka bankaPosiljalac, Banka bankaPrimalac) {
		super();
		this.vrstaPoruke = vrstaPoruke;
		this.datum = datum;
		this.ukupanIznos = ukupanIznos;
		this.bankaPosiljalac = bankaPosiljalac;
		this.bankaPrimalac = bankaPrimalac;
	}

	public String getVrstaPoruke() {
		return vrstaPoruke;
	}

	public void setVrstaPoruke(String vrstaPoruke) {
		this.vrstaPoruke = vrstaPoruke;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public Long getUkupanIznos() {
		return ukupanIznos;
	}

	public void setUkupanIznos(Long ukupanIznos) {
		this.ukupanIznos = ukupanIznos;
	}

	public Banka getBankaPosiljalac() {
		return bankaPosiljalac;
	}

	public void setBankaPosiljalac(Banka bankaPosiljalac) {
		this.bankaPosiljalac = bankaPosiljalac;
	}

	public Banka getBankaPrimalac() {
		return bankaPrimalac;
	}

	public void setBankaPrimalac(Banka bankaPrimalac) {
		this.bankaPrimalac = bankaPrimalac;
	}

}
