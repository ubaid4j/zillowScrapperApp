package com.ubaid.app.model.evenHanlder;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.constant.Constant;
import com.ubaid.app.model.table.PropertyInfo;

import javafx.application.Platform;

public class DeleteButtonHandler implements Runnable
{
	final Controller controller;
	
	public DeleteButtonHandler(Controller controller)
	{
		this.controller = controller;
	}

	@Override
	public void run()
	{
		try
		{
			PropertyInfo propertyInfo = controller.getRootPaneController().getTable().getSelectionModel().getSelectedItem();
			String base_url = propertyInfo.getBaseURL().getUrl();
			String ad_url	= propertyInfo.getUrl().getUrl();
			
			controller.getDatabase().deleteRecord(base_url, ad_url);
			Platform.runLater(new Runnable()
			{
				
				@Override
				public void run()
				{
					controller.getRootPaneController().getTable().setItems(controller.getDatabase().getAllScrapedData(base_url, Type.URL));				
				}
			});
			
		}
		catch(Exception exp)
		{
			Constant.getAlert("Select a record");
		}
	}
}
