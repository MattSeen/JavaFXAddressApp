package com.matt.addressapp;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Dialogs;
import javafx.stage.FileChooser;

public class RootLayoutController {

	private Main main;
	
	public void setMain(Main main)
	{
		this.main = main;
	}
	
	@FXML
	private void handleNew()
	{
		main.getPersonData().clear();
		main.setPersonFilePaths(null);
	}
	
	@FXML
	private void handleOpen()
	{
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());
		
		if (file != null)
		{
			main.loadPersonDataFromFile(file);
		}
	}
	
	@FXML
	private void handleSave()
	{
		File personFile = main.getPersonFilePath();
		if(personFile != null)
		{
			main.savePersonDataToFile(personFile);
		}
		else
		{
			handleSaveAs();
		}
	}
	
	@FXML
	private void handleSaveAs()
	{
		FileChooser fileChooser = new FileChooser();		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files(*.xml)", "*.xml");
		
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(main.getPrimaryStage());
		
		if(file != null)
		{
			if(!file.getPath().endsWith(".xml"))
			{
				file = new File(file.getPath() + ".xml");
			}
			main.savePersonDataToFile(file);
		}
	}
	
	@FXML
	private void handleAbout()
	{
		Dialogs.showInformationDialog(main.getPrimaryStage(), "Author: Matthew Cunningham", "AddressApp", "About!");	
	}
	
	@FXML
	private void handleShowBirthdayStatistics()
	{
		main.showBirthdayStatistics();
	}
	
	@FXML
	private void handleExit()
	{
		System.exit(0);
	}
}
