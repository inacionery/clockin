/**
 *
 */

package org.clockin.web.rest.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.clockin.domain.Clockin;
import org.clockin.domain.Employee;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class WorkDayDTO {

	private LocalDate date;
	private List<Clockin> clockins;

    private int minutesBalance;
    private int workDuration;
    private Employee employee;
    private boolean isMissing;


	public WorkDayDTO(LocalDate date) {
		super();
		this.date = date;
		this.clockins = new ArrayList<>();

	}

    public WorkDayDTO(LocalDate date, Employee employee){
        super();
        this.date = date;
        this.clockins = new ArrayList<>();

        this.employee = employee;
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


    public int getWorkDuration(){
        workDuration = 0;

        if(clockins.size() % 2 == 0){

            for(int i = 0; i< clockins.size(); i = i + 2){

               Duration period =  Duration.between(clockins.get(i).getTime(), clockins.get(i+1).getTime());
               workDuration += (int) period.toMinutes();
            }


        }else{

            for(int i = 0; i< clockins.size()-1; i = i + 2){

                Duration period =  Duration.between(clockins.get(i).getTime(), clockins.get(i+1).getTime());
                workDuration += (int) period.toMinutes();
            }

        }

        return workDuration;
    }


    public boolean getIsMissing(){

        return (clockins.size() % 2 != 0);
    }

    public int getMinutesBalance(){
        minutesBalance =  getWorkDuration() - (employee.getPlannedDailyHours()*60);
        return minutesBalance;
    }

	public void setClockinValues(List<Clockin> clockinValues) {

		this.clockins = clockinValues;
	}
}
