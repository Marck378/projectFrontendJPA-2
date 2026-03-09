package com.betacom.jpaFrontend.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

	public static LocalDate stringToDate(String date) {
		log.debug("Date:" + date);
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
		return LocalDate.parse(date, formater);
	}
	
	public static String dateToString(LocalDate date) {
		log.debug("Date:" + date);
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");		
		return date.format(formater);
			
	}
}
