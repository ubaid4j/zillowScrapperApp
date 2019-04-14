package com.ubaid.app.model.evenHanlder;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.constant.Constant;

public class RenameButtonHanlder implements Runnable
{

	final Controller controller;
	final String URI;
	final String name;
	final int index;
	
	public RenameButtonHanlder(Controller controller, String URI, String name, int index)
	{
		this.controller = controller;
		this.URI = URI;
		this.name = name;
		this.index = index;
	}
	
	
	@Override
	public void run()
	{
		controller.getDatabase().updateDocumentsName(URI, name);
		Constant.setInComboBox(controller.getDatabase().getUniqueBaseURLs(), controller);	
		Constant.showFieldOfComboBoxOnIndex(index, controller);
	}

}
