/**aaa
 *
 */
package org.clockin.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author Miguel Angelo Caldas Gallindo
 *
 */
public class GenerateDataMain {

	private static long sequential = 11111;
	private static DateTimeFormatter format =
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private static LocalTime B04_HORA_01 = LocalTime.of(8, 0, 0);
	private static LocalTime B04_HORA_02 = LocalTime.of(12, 0, 0);
	private static LocalTime B04_HORA_03 = LocalTime.of(13, 0, 0);
	private static LocalTime B04_HORA_04 = LocalTime.of(17, 0, 0);

	private static LocalTime B06_HORA_01 = LocalTime.of(8, 0, 0);
	private static LocalTime B06_HORA_02 = LocalTime.of(12, 0, 0);
	private static LocalTime B06_HORA_03 = LocalTime.of(13, 0, 0);
	private static LocalTime B06_HORA_04 = LocalTime.of(16, 0, 0);
	private static LocalTime B06_HORA_05 = LocalTime.of(17, 0, 0);
	private static LocalTime B06_HORA_06 = LocalTime.of(18, 0, 0);

	private static String insert =
		"INSERT INTO clockin.clockin (sequential_register_number, date_time, registry_type, employee_id) VALUES('0000%s', '%s:00.000000', 'TYPE_3', 1);%n";

	private static Random GENERATOR = new Random();

//	public static void main(String[] args) {
//
//
//		LocalDate startDate = LocalDate.of(2015, 7, 1);
//		LocalDate endDate = LocalDate.of(2015, 11, 28);
//
//		String clockin = "";
//
//		while (!startDate.isAfter(endDate)) {
//
//			if (!startDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
//				!startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
//				printHours(startDate);
//			}
//
//			startDate = startDate.plusDays(1);
//		}
//
//	}

	private static void printHours(LocalDate startDate) {

		 if(startDate.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {

			LocalDateTime b06Batida01 =
				startDate.atTime(B06_HORA_01).plusMinutes(
					GENERATOR.nextInt(60));
			LocalDateTime b06Batida02 = startDate.atTime(B06_HORA_02);
			LocalDateTime b06Batida03 =
				startDate.atTime(B06_HORA_03).plusMinutes(
					GENERATOR.nextInt(60));
			LocalDateTime b06Batida04 = startDate.atTime(B06_HORA_04);
			LocalDateTime b06Batida05 = startDate.atTime(B06_HORA_05);
			LocalDateTime b06Batida06 =
				startDate.atTime(B06_HORA_06).plusMinutes(
					GENERATOR.nextInt(60));

			printf(b06Batida01);
			printf(b06Batida02);
			printf(b06Batida03);
			printf(b06Batida04);
			printf(b06Batida05);
			printf(b06Batida06);
		}
		else {
			LocalDateTime b04Batida01 =
				startDate.atTime(B04_HORA_01).plusMinutes(
					GENERATOR.nextInt(60));
			LocalDateTime b04Batida02 = startDate.atTime(B04_HORA_02);
			LocalDateTime b04Batida03 =
				startDate.atTime(B04_HORA_03).plusMinutes(
					GENERATOR.nextInt(60));
			LocalDateTime b04Batida04 = startDate.atTime(B04_HORA_04);

			printf(b04Batida01);
			printf(b04Batida02);
			printf(b04Batida03);
			printf(b04Batida04);

		 }


	}

	private static void printf(LocalDateTime clockin) {

		String clockinString = format.format(clockin);

		System.out.printf(insert, sequential++, clockinString);
	}
}
