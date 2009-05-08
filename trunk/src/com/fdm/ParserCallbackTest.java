package com.fdm;

import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
 
public class ParserCallbackTest extends HTMLEditorKit.ParserCallback
{
	private int tabLevel = 1;
 
	public void handleComment(char[] data, int pos)
	{
		displayData(new String(data));
	}
 
	public void handleEndOfLineString(String eol)
	{
	}
 
	public void handleEndTag(HTML.Tag tag, int pos)
	{
		tabLevel--;
		displayData("/" + tag);
	}
 
	public void handleError(String errorMsg, int pos)
	{
		displayData(pos + ":" + errorMsg);
	}
 
	public void handleMutableTag(HTML.Tag tag, MutableAttributeSet a, int pos)
	{
		displayData("mutable:" + tag + ": " + pos + ": " + a);
	}
 
	public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet a, int pos)
	{
		displayData( tag + "::" + a );
//		tabLevel++;
	}
 
	public void handleStartTag(HTML.Tag tag, MutableAttributeSet a, int pos)
	{
		displayData( tag + ":" + a );
		tabLevel++;
	}
 
	public void handleText(char[] data, int pos)
	{
		displayData( new String(data) );
	}
 
	private void displayData(String text)
	{
		for (int i = 0; i < tabLevel; i++)
			System.out.print("\t");
 
		System.out.println(text);
	}
 
	public static void main(String[] args)
	throws IOException
	{
		ParserCallbackTest parser = new ParserCallbackTest();
 
		// args[0] is the file to parse
 
		Reader reader = new FileReader(args[0]);
//		URLConnection conn = new URL(args[0]).openConnection();
//		Reader reader = new InputStreamReader(conn.getInputStream());
 
		try
		{
			new ParserDelegator().parse(reader, parser, true);
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
}