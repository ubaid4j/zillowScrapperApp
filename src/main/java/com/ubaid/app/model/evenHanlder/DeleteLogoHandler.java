package com.ubaid.app.model.evenHanlder;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.constant.Constant;

import javafx.application.Platform;

public class DeleteLogoHandler implements Runnable
{

	protected final Controller controller;
	
	public DeleteLogoHandler(Controller controller)
	{
		this.controller = controller;
	}
	
	@Override
	public void run()
	{
		try
		{
			deleteLogo();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
	public void deleteLogo()
	{
		Platform.runLater(new Runnable() {
			
			@Override
			public void run()
			{
				Constant.setImageURL("");
			    controller.getRootPaneController().getImageView().setImage(null);
			}
		});
	}

}
