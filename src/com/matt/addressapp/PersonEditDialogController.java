package com.matt.addressapp;

import javafx.fxml.FXML;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.matt.addressapp.model.Person;
import com.matt.addressapp.util.CalendarUtil;

public class PersonEditDialogController {

	@FXML
	private TextField firstNameField;
	
	@FXML
	private TextField lastNameField;
	
	@FXML
	private TextField streetField;

	@FXML
	private TextField postalCodeField;
	
	@FXML
	private TextField cityField;
	
	@FXML
	private TextField birthdayField;


	private Stage dialogStage;
	private Person person;
	private boolean okClicked = false;
	
	@FXML
	private void initialize()
	{
		
	}
	
	public void setDialogStage(Stage dialogStage)
	{
		this.dialogStage = dialogStage;
	}
	
	
	public void setPerson(Person person)
	{
		this.person = person;
		
		firstNameField.setText(person.getFirstName());
		lastNameField.setText(person.getLastName());
		streetField.setText(person.getStreet());
		postalCodeField.setText(Integer.toString(person.getPostalCode()));
		
		cityField.setText(person.getCity());
		
		birthdayField.setText(CalendarUtil.format(person.getBirthday()));
		birthdayField.setPromptText("yyyy-MM-dd");
		
	}
	
	
	public boolean isOkClicked()
	{
		return okClicked;
	}
	
	@FXML
	private void handleOk()
	{
		if(isInputValid())
		{
			person.setFirstName(firstNameField.getText());
			person.setLastName(lastNameField.getText());
			person.setStreet(streetField.getText());
			person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
			person.setCity(cityField.getText());
			person.setBirthday(CalendarUtil.parse(birthdayField.getText()));
			
			okClicked = true;
			dialogStage.close();
		}
	}
	
	@FXML
	private void handleCancel()
	{
		dialogStage.close();
	}
	
	private boolean isInputValid()
	{
		String errorMessage = "";
		
		if (firstNameField.getText() == null || firstNameField.getText().length() == 0){
			errorMessage += "No valid first name!" + System.lineSeparator();
		}
		
		if(errorMessage.length() == 0)
		{
			return true;
		}
		else
		{
			Dialogs.showErrorDialog(dialogStage, 
					errorMessage,
					"Please correct invalid fields",
					"Invalid fields");
			return false;
		}
	}
}


