package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Banka extends Model {
	
	@Column(name="NAZIV_BANKE")
	public String nazivBanke;
	
	@Column(name="SIFRA_BANKE", length=3)
	public String sifraBanke;

	@Column(name="SWIFT_KOD", length=8)
	public String swiftKod;
	
	@Column(name="OBRACUNSKI_RACUN", length=18)
	public String obracunskiRacun;
	
	public Banka(String nazivBanke, String sifraBanke, String swiftKod, String obracunskiRacun) {
		
		this.nazivBanke = nazivBanke;
		this.sifraBanke = sifraBanke;
		this.swiftKod = swiftKod;
		this.obracunskiRacun = obracunskiRacun;

	}
}
