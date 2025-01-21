package Notepad;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class Notepad {
	
	JFrame frame = null ;
	JTextArea textArea = null ;
	
    JMenuBar menubar = null;
    
    JMenu command_Prompt, file, format, theme, font, sizeSubMenu, stylesubMenu, langauge ;
    JMenuItem New , newWindow , save , open, wrap, saveAs, exit, fontSize, dark, light, timesNewRoman, bold, italic, sizeItem, itemarial, itemtimesNewRoman, itemconsolas ;
	
    String openPath = null ;
	String openFile = null ;

    boolean isWordWrapOn = false ;
    Font arial, newRoman, consolas ;
    String fontStyle = "Arial" ;
    int fontSized=25;
   
	Notepad()
	{
		createFrame() ;
		createTextField() ;
		createScrollBars() ;
		createMenuBar() ;
		frame.setVisible( true ) ;
	}
	
	public void createFrame()
	{
		frame=new JFrame("Notepad") ;
		frame.setSize(800,800) ;
		frame.setLocation(300, 00) ;
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE) ;
	}
	
	public void createTextField()
	{
		textArea=new JTextArea() ;
		textArea.setFont(new Font("Consolas",Font.PLAIN,20)) ;
		frame.add(textArea) ;
	}
	
	public void createScrollBars( )
	{
		JScrollPane scroll = new JScrollPane( textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;
		frame.add(scroll) ;
	}
	
	public void createMenuBar()
	{
		menubar =new JMenuBar() ;
		createMenuBarOptions() ;
		frame.setJMenuBar(menubar) ;
	}
	
	public void createMenuBarOptions()
	{
		file=new JMenu("File") ;
		createFileMenuItems() ;
		
		langauge=new JMenu("Language") ;
		createLanguageitems() ;
		
		format=new JMenu("Format") ;
		createFormatMenu();
		
		theme=new JMenu("Theme") ;
		createThemeOption();
		
		command_Prompt = new JMenu("Command Prompt");
		JMenuItem openCmd = new JMenuItem("Open CMD");
		command_Prompt.add(openCmd);
		
		openCmd.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Check if openPath is valid
		        if (openPath == null || openPath.isEmpty() || frame.getTitle().equalsIgnoreCase("Notepad") || frame.getTitle().equalsIgnoreCase("Untitle")) {
		            JOptionPane.showMessageDialog(frame, "No valid path found. Please open or save a file first.", "Error", JOptionPane.ERROR_MESSAGE);
		            return; 
		            // Exit the action listener to prevent further execution
		        }

		        // If openPath is valid, execute the command
		        try 
		        {
		            Runtime.getRuntime().exec(new String[]{"cmd", "/k", "start"}, null, new File(openPath));
		        } 
		        catch (IOException e1) 
		        {
		            JOptionPane.showMessageDialog(frame, "Could not launch CMD due to an error.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		
		menubar.add(file) ;
		menubar.add(langauge) ;
		menubar.add(format) ;
		menubar.add(command_Prompt) ;
		menubar.add(theme) ;
	}
	
	
	public void createFileMenuItems()
	{ 	
		/*new option code */
		New=new JMenuItem("New");
		New.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				frame.setTitle("Untitle");
				openPath = null;
				openFile = null;
			}
		});
		
		/*new Window option code */
		newWindow=new JMenuItem("new Window");
		newWindow.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Notepad n1=new Notepad(); 
				n1.frame.setTitle("Untitle");
			}
		});
		
		/*open option code */
		open=new JMenuItem("open");
		open.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd=new FileDialog(frame,"Open",FileDialog.LOAD);
				fd.setVisible(true);
				String file=fd.getFile();
				String[] fileName=file.split("\\.");
				System.out.println(fileName[0]);
				String path=fd.getDirectory();
				
				if(file!=null)
				{
					frame.setTitle(fileName[0]);
					openFile=file;
					openPath=path;
				}
				
				BufferedReader reader = null;
				try {
					
					reader= new BufferedReader(new FileReader(path+file));
					String sentence=reader.readLine();
					textArea.setText("");
					while(sentence!=null)
					{
						textArea.append(sentence +"\n");
						sentence=reader.readLine();
					}					
				}
				catch (FileNotFoundException e1) 
				{
						System.out.println("File Not Found");
				} 
				catch (IOException e1) 
				{
					System.out.println("Data Could Not be Read");	
				} 
				catch (NullPointerException e1) 
				{
					System.out.println("Not be Read");
				}
				finally 
				{
					try 
					{
						reader.close();
						System.out.println("File Closed ");
						
					} catch (IOException e1) 
					{
						System.out.println("File Could Not Close ");
					}
					catch (NullPointerException e1) 
					{
						System.out.println("File is null");
					}
				}
				
			}
	
		});
		
		
		/*save option code */
		save=new JMenuItem("save");
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(openPath != null && openFile != null)
				{
					writeDataToFile(openFile,openPath);
				}
				else
				{
					FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE) ;
					fd.setVisible(true) ;
					String path = fd.getDirectory() ;
					String fileName = fd.getFile() ;
					
					BufferedWriter bw = null;
					
					if( fileName != null && path != null )
					{
						try
						{
							bw=new BufferedWriter(new FileWriter(path + fileName)) ;
							String text=textArea.getText() ;
							bw.write(text) ;
						} 
						catch (IOException e1) 
						{
							System.out.println("Data Cannot Be Wriiten") ;
						}
						finally 
						{
							try 
							{
								bw.close();
								System.out.println("closed sucessfully") ;
							} catch (IOException e1) 
							{
								System.out.println("Could not be closed") ;
							}
							catch (NullPointerException e1) 
							{
								System.out.println("File Not Found To Close!") ;
							}
						}
					}
				}
			}
		});
		
		
		
		/* Save As Functionality  */
		saveAs=new JMenuItem("save As") ;
		saveAs.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE) ;
				fd.setVisible(true) ;
				
				String path = fd.getDirectory() ;
				String fileName = fd.getFile() ;
				
				BufferedWriter bw = null ;
				if(fileName != null && path != null)
				{
					try {
						 	bw=new BufferedWriter(new FileWriter(path+fileName)) ;	
						 	String text=textArea.getText() ;
						 	bw.write(text);
						 	
						 	openPath = path;
				            openFile = fileName;
				            
				            frame.setTitle(fileName);

			                System.out.println("File saved successfully");
					
					} 
					catch (IOException e1) 
					{
						System.out.println("Data Cannot Be Wriiten")  ;
					}
					finally 
					{
						try 
						{	
							 if (bw != null) {
						            bw.close();
						            System.out.println("closed successfully");
						        }
						} 
						catch (IOException e1) 
						{
							System.out.println("Could not be closed");
						}
						catch (NullPointerException e1)
						{
							System.out.println("File Not Found To Close!");
						}
					}
				}
			}
		});
	
		exit=new JMenuItem("exit");
		exit.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						 if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						        System.exit(0);
						    }
					}
				}) ;
		
		file.add(New);
		file.add(newWindow);
		file.addSeparator();
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(exit);
	}
	
	public void writeDataToFile( String fileName , String path)
	{
		BufferedWriter bw = null ;
		if(fileName != null && path != null)
		{
			try 
			{
				bw = new BufferedWriter(new FileWriter(path + fileName));						
				String text = textArea.getText() ;
				bw.write( text ) ;
			} 
			catch (IOException e1) 
			{
				System.out.println("Data Cannot Be Wriiten");
			} 
			finally 
			{
				try 
				{
					bw.close();
					System.out.println("closed sucessfully");			
				} 
				catch (IOException e1) 
				{	
					System.out.println("Could not be closed");			
				}
				catch (NullPointerException e1) 
				{	
					System.out.println("File Not Found To Close!");
				}
			}
		}
	}
	
	/*create language sub-options that add in menu bar*/
	public void createLanguageitems()
	{
		 JMenuItem itemJava=new JMenuItem("Java");
		 itemJava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setLanguage("java") ;
			}
		});
		 
		JMenuItem itemC=new JMenuItem("C") ;
		itemC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setLanguage("C") ;
			}
		});
		 
		JMenuItem itemCPP=new JMenuItem("CPP");
		itemCPP.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setLanguage("CPP") ;	
			}
		});
		 
		 JMenuItem itemHTML=new JMenuItem("HTML");
		 itemHTML.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					setLanguage("HTML") ;	
			}
		});
		 
		 langauge.add(itemJava);
		 langauge.add(itemC);
		 langauge.add(itemCPP);
		 langauge.add(itemHTML);
	}
	
	public void setLanguage(String lang)
	{
		String path = "D:\\";
		BufferedReader reader = null;
		try {
			
			reader= new BufferedReader(new FileReader(path + lang + ".txt"));
			String sentence=reader.readLine();
			textArea.setText("");
			while(sentence!=null)
			{
				textArea.append(sentence +"\n");
				sentence=reader.readLine();
			}
		} 
		catch (FileNotFoundException e1) 
		{
			System.out.println("File Not Found");
			
		} catch (IOException e1) {
			
			System.out.println("Data Could Not be Read");
			
		} catch (NullPointerException e1) {
			System.out.println("Not be Read");
			
		}
		finally 
		{
			try 
			{
				reader.close() ;
				System.out.println("File Closed ") ;
			} 
			catch (IOException e1) 
			{
				System.out.println("File Could Not Close ") ;
			}
			catch (NullPointerException e1) 
			{
				System.out.println("File is null") ;
			}
		}
	}

	/*format menu options like wrap off ,Font*/
	public void createFormatMenu()
	{
		wrap=new JMenuItem("wrap Off") ;
		
		wrap.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isWordWrapOn)
				{
					textArea.setLineWrap(false);
					textArea.setWrapStyleWord(false);
					isWordWrapOn=false;
					wrap.setText("Word wrap on");
					
				}else {
					
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					isWordWrapOn=true;
					wrap.setText("Word wrap off");
				}
			}
		});
		
		font=new JMenu("Font") ;
		
		itemarial= new JMenuItem("Arial") ;
		itemtimesNewRoman= new JMenuItem("Times New Roman") ;
		itemconsolas= new JMenuItem("Consolas") ;
		
		itemarial.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setFont("Arial");
			}
		});
		
		itemtimesNewRoman.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFont("NewRoman");
			}
		});
		
		itemconsolas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setFont("Consolas");
			}
		});
		
		font.add(itemarial) ;
		font.add(itemtimesNewRoman) ;
		font.add(itemconsolas);
		
		sizeSubMenu= new JMenu("Size") ;
		
		JMenuItem size16 = new JMenuItem("16");
		size16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(18);
			}
		});
		
		JMenuItem size20 = new JMenuItem("20");
		size20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(20);
			}
		});
		
		JMenuItem size22 = new JMenuItem("22");
		size22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(22);
			}
		});
		
		JMenuItem size24 = new JMenuItem("24");
		size24.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(24);
			}
		});
		
		JMenuItem size26 = new JMenuItem("26");
		size26.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(26);
			}
		});
		
		JMenuItem size28= new JMenuItem("28");
		size28.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(28);
			}
		});
		
		JMenuItem size30= new JMenuItem("30");
		size30.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(30);
			}
		});
		
		JMenuItem size32= new JMenuItem("32");
		size32.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(32);
			}
		});
		
		JMenuItem size34= new JMenuItem("34");
		size34.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFontSize(32);
			}
		});
		
        sizeSubMenu.add(size16);
        sizeSubMenu.add(size20);
        sizeSubMenu.add(size22);
        sizeSubMenu.add(size24);
        sizeSubMenu.add(size26);
        sizeSubMenu.add(size28);
        sizeSubMenu.add(size30);
        sizeSubMenu.add(size32);
        sizeSubMenu.add(size34);

		format.add(wrap);
		format.addSeparator();
		format.add(font);
		format.add(sizeSubMenu);
	}
	
	/*set font size */
	public void setFontSize(int size)
	{
		 arial=new Font("Arial",Font.PLAIN,size);
		 newRoman=new Font("TimesNewRoman",Font.PLAIN,size);
		 consolas=new Font("Consolas",Font.PLAIN,size);
		 fontSized=size;
		 setFont(fontStyle);
	}
	
	/*set font style along with size */
	public void setFont(String fontName)
	{
		
		fontStyle=fontName;
		arial=new Font("Arial",Font.PLAIN,fontSized);
		newRoman=new Font("TimesNewRoman",Font.PLAIN,fontSized);
		consolas=new Font("Consolas",Font.PLAIN,fontSized);
		 
		switch(fontName)
		{
		case "Arial":
			
			textArea.setFont(arial);
			break;
		case "NewRoman":
			textArea.setFont(newRoman);
			break;
		case "Consolas":
			textArea.setFont(consolas);
			break;
		default:
			break;
			
		}
	}
	
	
	/* create theme dark and light */
	public void createThemeOption()
	{
		dark=new JMenuItem("dark");
		light=new JMenuItem("light");
		
		theme.add(dark);
		theme.add(light);
		
		dark.addActionListener( new ActionListener()
		{

				@Override
				public void actionPerformed(ActionEvent e) {
					menubar.setBackground(Color.GRAY) ;
					command_Prompt.setForeground(Color.WHITE) ;
					file.setForeground(Color.WHITE) ;
					theme.setForeground(Color.WHITE) ;
					format.setForeground(Color.WHITE) ;
					langauge.setForeground(Color.WHITE) ;
					textArea.setBackground(Color.BLACK) ;
					textArea.setForeground(Color.WHITE) ;
			}
		});
		
		light.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				
				  textArea.setFont(new Font("Arial", Font.PLAIN, 16));
				  menubar.setBackground(Color.white);
				  command_Prompt.setForeground(Color.black);
				  file.setForeground(Color.black);
				  theme.setForeground(Color.black);
				  format.setForeground(Color.black);
				  langauge.setForeground(Color.black);
				  textArea.setBackground(Color.WHITE);
			      textArea.setForeground(Color.BLACK);
			}
		});
	}

}
