package com.App.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Holidays")
public class Holidays extends BaseEntity{
	
	@Id
	private String date;
	
	private String reason;
	
	@Enumerated(EnumType.STRING)
	private Type type;
	
	public enum Type {
		FESTIVAL, FEDERAL;
	}

}
