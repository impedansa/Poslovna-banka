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
	public String ukupanIznos;
	
	@ManyToOne
	public Banka bankaPosiljalac;
	
	@ManyToOne
	public Banka bankaPrimalac;

	public MedjubankarskiPrenos(String vrstaPoruke, Date datum,
			String ukupanIznos, Banka bankaPosiljalac, Banka bankaPrimalac) {
		super();
		this.vrstaPoruke = vrstaPoruke;
		this.datum = datum;
		this.ukupanIznos = ukupanIznos;
		this.bankaPosiljalac = bankaPosiljalac;
		this.bankaPrimalac = bankaPrimalac;
	}

}
