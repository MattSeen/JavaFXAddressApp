package com.matt.addressapp;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.matt.addressapp.model.Person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

public class BirthdayStatisticsController {

	
	@FXML
	private BarChart<String, Integer> barChart;
	
	@FXML
	private CategoryAxis xAxis;
	
	private ObservableList<String> monthNames = FXCollections.observableArrayList();
	
	@FXML
	private void initialize()
	{
		final String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
		
		monthNames.addAll(Arrays.asList(months));
		xAxis.setCategories(monthNames);
	}
	
	public void setPersonData(List<Person> persons)
	{
		int[] monthCounter = new int[12];
		for (Person person : persons) {
			int month = person.getBirthday().get(Calendar.MONTH);
			monthCounter[month]++;
		}
		
		XYChart.Series<String, Integer> series = createMonthDataSeries(monthCounter);
		barChart.getData().add(series);
	}
	
	private XYChart.Series<String, Integer> createMonthDataSeries(int[] monthCounter) {
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		
		for (int i = 0; i < monthCounter.length; i++) {
			XYChart.Data<String,Integer> monthData = new XYChart.Data<>(monthNames.get(i), monthCounter[i]);
			series.getData().add(monthData);
		}
		
		return series;
	}
}
