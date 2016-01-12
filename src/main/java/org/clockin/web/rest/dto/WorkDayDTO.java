/**
 *
 */

package org.clockin.web.rest.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.clockin.domain.Clockin;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class WorkDayDTO {

	private LocalDate date;
	private List<Clockin> clockins;


	public WorkDayDTO(LocalDate date) {
		super();
		this.date = date;
		this.clockins = new ArrayList<>();
	}

	public void addClockinValues(Clockin... clockins) {

		if (clockins != null) {
			for (Clockin Clockin : clockins) {
				this.clockins.add(Clockin);
			}
		}
	}



	public LocalDate getDate() {

		return date;
	}

	public void setDate(LocalDate date) {

		this.date = date;
	}

	public List<Clockin> getClockinValues() {

		return clockins;
	}

	public void setClockinValues(List<Clockin> clockinValues) {

		this.clockins = clockinValues;
	}
}
