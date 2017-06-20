package models;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class RacunD {
	
	public Long id;
	
	public String brojRacuna;
	
	
	public String statusRacuna;
	
	
	public Klijent klijent;
	
	
	public Banka banka;
	
	public Valuta valuta;

	public RacunD(Long id, String brojRacuna, String statusRacuna, Klijent klijent, Banka banka, Valuta valuta) {
		super();
		this.id = id;
		this.brojRacuna = brojRacuna;
		this.statusRacuna = statusRacuna;
		this.klijent = klijent;
		this.banka = banka;
		this.valuta = valuta;
	}
	
	
	


	
}
