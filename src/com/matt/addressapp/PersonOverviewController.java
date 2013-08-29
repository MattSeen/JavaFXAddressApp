package com.matt.addressapp;

import com.matt.addressapp.model.Person;
import com.matt.addressapp.util.CalendarUtil;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonOverviewController {
	
	@FXML
	private TableView<Person> personTable;
	
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	
	@FXML
	private TableColumn<Person, String> lastNameColumn;
	
	@FXML
	private Label firstNameLabel;
	
	@FXML
	private Label lastNameLabel;
	
	@FXML
	private Label streetLabel;
	
	@FXML
	private Label postalCodeLabel;
	
	@FXML
	private Label cityLabel;
	
	@FXML
	private Label birthdayLabel;
	
	// Reference to the main application
	private Main main;
	
	public PersonOverviewController() {
	
	}
	
	@FXML
	private void initialize() {
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
		
		personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		showPersonDetails(null);
		
		personTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() 
				{
			@Override
			public void changed(javafx.beans.value.ObservableValue<? extends Person> observable, Person oldValue, Person newValue) 
			{
				showPersonDetails(newValue);
			};
		
		});
	}
	
	public void setMainApp(Main main) {
		this.main = main;		
		personTable.setItems(main.getPersonData());
	}
	
	private void showPersonDetails(Person person) 
	{
		if(person != null)
		{
			birthdayLabel.setText(CalendarUtil.format(person.getBirthday()));
			cityLabel.setText(person.getCity());
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			postalCodeLabel.setText(String.valueOf(person.getPostalCode()));
			streetLabel.setText(person.getStreet());
		}
	}
	
	@FXML
	private void handleDeletePerson()
	{
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0 )
		{
			personTable.getItems().remove(selectedIndex);
		}
		else
		{
			Dialogs.showWarningDialog(main.getPrimaryStage(), 
					"Please select a person in the table",
					"No person selected", "No Selection");			
		}		
	}
	
	
	@FXML
	private void handleNewPerson()
	{
		Person tempPerson = new Person();
		boolean okClicked = main.showPersonEditDialog(tempPerson);
		
		if(okClicked)
		{
			main.getPersonData().add(tempPerson);
		}
	}
	
	@FXML
	private void handleEditPerson()
	{
		Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
		
		if (selectedPerson != null){
			boolean okClicked = main.showPersonEditDialog(selectedPerson);
			if(okClicked)
			{
				refreshPersonTable();
				showPersonDetails(selectedPerson);
			}
		}
		else
		{
			Dialogs.showWarningDialog(main.getPrimaryStage(),
					"Please select a person in the table",
					"No person selected", "No selection");
		}
	}
	
	private void refreshPersonTable()
	{
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		personTable.setItems(null);
		personTable.layout();
		personTable.setItems(main.getPersonData());
		
		personTable.getSelectionModel().select(selectedIndex);
	}
}
