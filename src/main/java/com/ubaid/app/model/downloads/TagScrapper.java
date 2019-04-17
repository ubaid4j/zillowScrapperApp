package com.ubaid.app.model.downloads;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ubaid.app.model.constant.Constant;

public class TagScrapper implements Runnable
{

	private DHFTS dhfts;
	private File file;
	
	public TagScrapper(DHFTS dhfts, File file)
	{
		setDhfts(dhfts);
		setFile(file);		
	}
	
	
	@Override
	public void run()
	{
		try
		{
			String[] anchorTags = getTags();
			
			for(int i = 0; i < anchorTags.length; i++)
			{
				getDhfts().getTagCollections().add(anchorTags[i]);
			}
			
			
		}
		catch(IOException exp)
		{
			Constant.getAlert(Arrays.toString(exp.getStackTrace()));
		}
		
	}

	
	private String[] getTags() throws IOException
	{
		//parsing document
		Document document = Jsoup.parse(file, "UTF-8");	
	
		//getting all the article tags from the file [full search 1, full search 2 etc]
		Elements anchorTags = document.getElementsByTag("article");
		
		//creating an array
		String aTags[] = new String[anchorTags.size()];
		
		//now adding the href of each aritle in the aTags array
		for(int i = 0; i < anchorTags.size(); i++)
		{
			try
			{
				String tag = anchorTags.get(i).getElementsByTag("a").get(0).attr("href");
				aTags[i] = "https://www.zillow.com" + tag;				
			}
			catch(Exception exp)
			{
				exp.printStackTrace();
			}
		}

		
		//returning aTags
		return aTags;
	}

	public DHFTS getDhfts() {
		return dhfts;
	}


	public void setDhfts(DHFTS dhfts) {
		this.dhfts = dhfts;
	}


	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}

}
