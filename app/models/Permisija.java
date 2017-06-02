package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.deadbolt.Role;
import play.db.jpa.Model;

@Entity
public class Permisija extends Model implements Role {
	
	@Column (name="NAZIV_PERMISIJE")
	public String nazivPermisije;

	@Override
	public String getRoleName() {
		return nazivPermisije;
	}

	public Permisija(String nazivPermisije) {
		super();
		this.nazivPermisije = nazivPermisije;
	}

	public Permisija() {
		super();
		// TODO Auto-generated constructor stub
	}

}
