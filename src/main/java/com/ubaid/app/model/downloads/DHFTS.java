package com.ubaid.app.model.downloads;

import java.io.File;
import java.time.LocalDate;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.constant.Constant;
import com.ubaid.app.model.downloads.url.URL;
import com.ubaid.app.model.evenHanlder.Type;
import com.ubaid.app.model.table.PropertyInfo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;

//this first download all the pages of url 1 to 7 or any things:
//then extracting anchor tags from all these pages
//then from all anchor tags, download again pages of a tags
//Pattern of Directory
//....Scrapper
//......dateTime
//........Pages
//........tags
public class DHFTS implements Runnable
{
	
	private File fullSearchDir;
	private File tagsSearchDir;
	private String url;
	private int index;
	private final Vector<String> tagCollections = new Vector<String>();
	private final ObservableList<PropertyInfo> list = FXCollections.observableArrayList();
	private final Controller controller;
	private final String baseUrl;
	private final Boolean url_present_in_database;
	private int sizeOfTable = 0;
	private int lastIndexOfTable;
	
	
	@Override
	public void run()
	{
		//after constructor is end, 
		//now run method will be executing
		
		//make a folder for format date+counter
		File file = new File(Constant.getAppDirectory() + "/" + LocalDate.now() +  Constant.getFolderNumber());

		//if folder will be created then: 
		if(file.mkdir())
		{
			//creating two folders 
			//full search folder
			//and tagSearchFolder
			File fullSearchDir = new File(file.getAbsolutePath() + "/fullSearch");
			File tagSearchDir = new File(file.getAbsolutePath() + "/tagSearch");
			
			//when both these folders created then 
			if(fullSearchDir.mkdir() & tagSearchDir.mkdir())
			{
				//presenting info
				Constant.setLabel(fullSearchDir.getAbsolutePath() + 
						" is created \n" + tagSearchDir.getAbsolutePath() +
						" is created", controller);

				//setting the paths of newly created folders 1. full search folder, and 2. tag search folder
				setFullSearchDir(fullSearchDir);
				setTagsSearchDir(tagSearchDir);
			}
			else
			{
				//if these folders can not be created then closing application
				Constant.getAlert("Cannot be made " + fullSearchDir.getAbsolutePath() + " or " + tagSearchDir.getAbsolutePath());
				System.exit(0);
			}
		}
		else
		{
			//if parent folder cannot be created then closing application
			Constant.getAlert("Cannot be made " + fullSearchDir.getAbsolutePath());
			System.exit(0);
		}
		
		//downloading the search files
		Downloader downloader = new Downloader(this);
		
		
		final ExecutorService threadPool = Executors.newFixedThreadPool(1);
		threadPool.execute(downloader);
		controller.getPool().addThreadPool(threadPool);
		threadPool.shutdown();
						
	}
	

	//constructor
	public DHFTS(String url, Controller controller)
	{
		//this is all done in the constructor
		
		//when we click on the start button, our controller will arrive here
		//starting the indicator
		controller.getRootPaneController().getProgressIndicator().setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

		//setting variables
		baseUrl = url;
		this.controller = controller;
		setUrl(url);
		setIndex(1);
		
		//emptying collections 
		tagCollections.removeAllElements();
		getList().clear();
		
		//reseting all things 
		controller.getRootPaneController().getButton().setDisable(true);
		controller.getRootPaneController().getRenameButton().setDisable(true);
		controller.getRootPaneController().getCombobox().setDisable(true);
		controller.getRootPaneController().getUlrField().setDisable(true);
		controller.getRootPaneController().getRenameField().setDisable(true);
		
		//emptying the table
		setTableEmpty();
		
		//if the the URL is present in the database
		//mean we have already scraped the data from the given url, then 
		//calling populate table method 
		if(controller.getDatabase().isURLPresentInDataBase(baseUrl))
		{
			//populating the table with base url data
			populateTable();
			url_present_in_database = true;
		}
		else
		{
			url_present_in_database = false;			
		}
		
		// if url is present in the database [mean we have paste the same url in second[and more] time]
		if(getUrl_present_in_database())
		{
			//getting size of the scraped data
			int size = getController().getRootPaneController().getTable().getItems().size();
			
			//setting last index of the table
			setLastIndexOfTable(getController().
					getRootPaneController().getTable().getItems().get(size - 1).getId());
		}

		//resetting counter label
		Constant.setCounterLabel("", controller);
		
	}

	
	
	public File getFullSearchDir() {
		return fullSearchDir;
	}

	public void setFullSearchDir(File fullSearchDir) {
		this.fullSearchDir = fullSearchDir;
	}

	public File getTagsSearchDir() {
		return tagsSearchDir;
	}

	public void setTagsSearchDir(File tagsSearchDir) {
		this.tagsSearchDir = tagsSearchDir;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public synchronized int getIndex() {
		return index;
	}

	public synchronized void setIndex(int index) {
		this.index = index;
	}

	public Vector<String> getTagCollections() {
		return tagCollections;
	}



	public Controller getController() {
		return controller;
	}



	public ObservableList<PropertyInfo> getList() {
		return list;
	}


	public String getBaseUrl() {
		return baseUrl;
	}


	public Boolean getUrl_present_in_database() {
		return url_present_in_database;
	}

	public int getSizeOfTable() {
		return sizeOfTable;
	}

	public void setSizeOfTable(int sizeOfTable) {
		this.sizeOfTable = sizeOfTable;
	}
	
	private void populateTable()
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				
				System.out.println("Table Populating start");
				
				//getting list of all property info of given uri
				ObservableList<PropertyInfo> list = getController().getDatabase().getAllScrapedData(getController().getDatabase().getURL(getBaseUrl()).toString(), getType());
				
				//setting the same info in the list(DHFTS)
				for(PropertyInfo info: list)
				{
					System.out.println(info);
					getList().add(info);
				}

				
				//setting the info in table
				getController().getRootPaneController().getTable().setItems(list);
				
				
				//storing size of the table in DHFTS
				setSizeOfTable(getController().getRootPaneController().getTable().getItems().size());
				
				System.out.println("Size of the table: " + getSizeOfTable());
				
			}
		});
	}
	
	/**
	 * the getType method was induce due 
	 * to [becuase in early version of this app
	 * the type of combobox was String
	 * so we need to find if it was a URL or name
	 * so it is the reason
	 * @return
	 */
	private Type getType()
	{
		
		URL url = null;
		url = getController().getDatabase().getURL(getBaseUrl());
		String URI = url.toString();
		
		
		if(URI.length() > 5)
		{
			if(URI.substring(0, 5).equals("https"))
				return Type.URL;
			else
				return Type.NAME;
		}
		
		return null;
	}
	
	private void setTableEmpty()
	{
		Platform.runLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				getController().getRootPaneController().getTable().setItems(null);
			}
		});
	}


	public int getLastIndexOfTable() {
		return lastIndexOfTable;
	}


	public void setLastIndexOfTable(int lastIndexOfTable) {
		this.lastIndexOfTable = lastIndexOfTable;
	}




}
