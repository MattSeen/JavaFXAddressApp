package com.matt.addressapp;
	
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.matt.addressapp.model.Person;
import com.matt.addressapp.util.FileUtil;
import com.thoughtworks.xstream.XStream;

public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private ObservableList<Person> personData = FXCollections.observableArrayList();
	
	public Main() 
	{
		personData.add(new Person("Hans", "Zimmerman"));
		personData.add(new Person("Joesph", "Frank"));
		personData.add(new Person("Seymor", "Butts"));
	}
	
	public ObservableList<Person> getPersonData(){
		return personData;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AddressApp");
		this.primaryStage.getIcons().add(new Image("file:resources/images/title_bar_icon.png"));

		try {
			// Load the root layout from the fxml file
			final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			final Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			
			RootLayoutController controller = loader.getController();
			controller.setMain(this);
			
			primaryStage.show();			
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		
		showPersonOverview();
		
		File file = getPersonFilePath();
		if(file != null)
		{
			loadPersonDataFromFile(file);
		}
	}
	
	public void showPersonOverview(){
		try 
		{
			final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/PersonOverview.fxml"));
			final AnchorPane overviewPage = (AnchorPane) loader.load();
			rootLayout.setCenter(overviewPage);
			
			// Give the controller access to the main app
			PersonOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public boolean showPersonEditDialog(Person person)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.getIcons().add(new Image("file:resources/images/title_bar_icon.png"));
			
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public Stage getPrimaryStage()
	{
		return primaryStage;
	}

	
	public File getPersonFilePath()
	{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		final String filePath = prefs.get("filePath", null);
		if (filePath != null)
		{
			return new File(filePath);
		}
		else
		{
			return null;
		}
	}
	
	public void setPersonFilePaths(File file)
	{
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if(file != null)
		{
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle("AddressApp - " + file.getName());
		}
		else
		{
			prefs.remove("filePath");
			primaryStage.setTitle("AddressApp");
		}
	}

	@SuppressWarnings("unchecked")	
	public void loadPersonDataFromFile(File file) {
		final XStream xstream = new XStream();
		xstream.alias("person", Person.class);
		
		try
		{
			final String xml  = FileUtil.readFile(file);
			final ArrayList<Person> personList = (ArrayList<Person>) xstream.fromXML(xml);
			
			personData.clear();
			personData.addAll(personList);
			
			setPersonFilePaths(file);
		}
		catch (Exception e)
		{
			Dialogs.showErrorDialog(primaryStage,
					"Could not load data from file" + System.lineSeparator() + file.getPath(),
					"Could not load data", 
					"Error", 
					e);
		}
	}

	public void savePersonDataToFile(File file)
	{
		XStream xstream = new XStream();
		xstream.alias("person", Person.class);
		
		ArrayList<Person> personList = new ArrayList<>(personData);
		
		String xml = xstream.toXML(personList);
		try
		{
			FileUtil.saveFile(xml, file);
			setPersonFilePaths(file);
			
		}
		catch (Exception e)
		{
			Dialogs.showErrorDialog(primaryStage, 
					"Could not save data to file:" + System.lineSeparator() + file.getPath(),
					"Could not save data",
					"Error",
					e);
		}
	}
	
	public void showBirthdayStatistics()
	{
		try{
			final FXMLLoader loader  = new FXMLLoader(Main.class.getResource("view/BirthdayStatistis.fxml"));
			final AnchorPane page = (AnchorPane) loader.load();
			final Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			final Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			final BirthdayStatisticsController controller = loader.getController();
			controller.setPersonData(personData);
			
			dialogStage.show();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
