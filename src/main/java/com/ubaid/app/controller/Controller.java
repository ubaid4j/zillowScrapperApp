package com.ubaid.app.controller;

import com.ubaid.app.model.Threads.Pool;
import com.ubaid.app.model.Threads.PoolOfThread;
import com.ubaid.app.model.database.Database;
import com.ubaid.app.model.evenHanlder.ActionListener;
import com.ubaid.app.view.RootPane;

import javafx.stage.Stage;

public class Controller
{
	private RootPane rootPaneController;
	private ActionListener actionListener = new ActionListener(this);
	private final Database database = new Database(this);
	private final Stage stage;
	private final PoolOfThread pool;

	public Controller(Stage stage)
	{
		this.stage = stage;
		pool = new Pool();
	}
	
	public RootPane getRootPaneController()
	{
		return rootPaneController;
	}

	public void setRootPaneController(RootPane rootPaneController)
	{
		this.rootPaneController = rootPaneController;
	}
	
	public void doRegisterComponents()
	{
		getRootPaneController().getButton().setOnAction(actionListener);
		getRootPaneController().getCombobox().setOnAction(actionListener);
		getRootPaneController().getRenameButton().setOnAction(actionListener);
		getRootPaneController().getExportCSVMenuItem().setOnAction(actionListener);
		getRootPaneController().getExportXLMSMenuItem().setOnAction(actionListener);
		getRootPaneController().getUploadLogoItem().setOnAction(actionListener);
		getRootPaneController().getAboutMenuItem().setOnAction(actionListener);
		getRootPaneController().getDeleteButton().setOnAction(actionListener);
		getRootPaneController().getCloseMenuItem().setOnAction(actionListener);
		getRootPaneController().getDeleteTableButton().setOnAction(actionListener);
		getRootPaneController().getDeleteLogoItem().setOnAction(actionListener);
	}

	public Database getDatabase() {
		return database;
	}
	

	public Stage getStage() {
		return stage;
	}

	public PoolOfThread getPool() {
		return pool;
	}


}
