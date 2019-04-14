package com.ubaid.app.model.export;

import java.io.File;

import com.ubaid.app.controller.Controller;
import com.ubaid.app.model.table.PropertyInfo;

import javafx.collections.ObservableList;

public interface Export
{
	public Boolean export(File file, ObservableList<PropertyInfo> list, Controller controller) throws Exception;
	
	enum Type
	{
		CSV,
		XLMS;
	}
}
