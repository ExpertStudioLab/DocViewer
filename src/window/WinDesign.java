package window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class WinDesign extends JFrame {
	private String endline = "\r\n";
	private TextPaneProperty textProperty;
	private JScrollPane scrollPane;
	private FileInputStream fileIn;
	private final byte buffer[] = new byte[ 1024 ];

	public WinDesign( String title ) {
		super( title );
		this.setPreferredSize( new Dimension( 800, 600 ) );
	}
	
	public void setScrollPane( JScrollPane scrollPane ) {
		this.scrollPane = scrollPane;
	}
	
	public void setTextProperty( TextPaneProperty textProperty ) {
		this.textProperty = textProperty;
	}
	
	public void setDocument( File filename ) {
		try {
//			FileReader fileReader = new FileReader( filename );
			FileInputStream fStream = new FileInputStream( filename );
			InputStreamReader stream = new InputStreamReader( fStream, "UTF-8" );
			BufferedReader bufReader = new BufferedReader( stream );
			String line;
			while( ( line = bufReader.readLine() ) != null ) {
				if( line.indexOf( "<div id=\"Tag\">" ) != -1 ) {
					this.insertDocument( line );
				}
				if( line.indexOf( "<h1>") != -1 ) {
					this.insertDocument( line );
				}
				if( line.indexOf( "<h2") != -1 ) {
					this.insertDocument(line);
				}
			}
			bufReader.close();
			stream.close();
			fStream.close();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void insertDocument( String line ) throws BadLocationException {
		int index = line.indexOf( '>' );
		String tmp = line.substring( index + 1 );
		index = tmp.indexOf( '<' );
		String contents = tmp.substring( 0, index );
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(aSet, "Serif");
		this.textProperty.getDocument().insertString( this.textProperty.getDocument().getLength(),
											contents + endline, aSet );
	}
	
	public void showWindow() {
		this.getContentPane().add( this.scrollPane, BorderLayout.CENTER );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.pack();
		this.setVisible( true );
	}
}
