package window;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


@SuppressWarnings("serial")
public class View {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WinDesign winDesign = new WinDesign( "DocViewer" );
		
		TextPaneProperty textProperty = new TextPaneProperty();
		JScrollPane scrollPane = new JScrollPane( textProperty.getTextPane() );
		scrollPane.setPreferredSize( new Dimension( 800, 800 ) );
		winDesign.setScrollPane( scrollPane );
		winDesign.setTextProperty( textProperty );

		File file = new File( "C:\\Users\\SmartBrightB\\Desktop\\Java Training\\JFrame\\DocViewer\\src\\window\\DOC001.html" );
		winDesign.setDocument( file );

		try {
			winDesign.showWindow();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class TextPaneProperty {
	private JTextPane textPane;
	private AbstractDocument abstDoc;
	public TextPaneProperty() {
		this.textPane = new JTextPane();
		this.textPane.setCaretPosition( 0 );
		this.textPane.setMargin( new Insets( 20, 40, 20, 40 ) );
		this.textPane.setPreferredSize( new Dimension( 800, 600 ) );
		this.abstDoc = ( AbstractDocument ) this.textPane.getStyledDocument();
		this.abstDoc.setDocumentFilter( new DocumentFilter ( ));
		this.textPane.setEditable( false );
		this.textPane.setFont( new Font( "Meiryo", Font.LAYOUT_NO_LIMIT_CONTEXT, 25 ) );
		this.textPane.setFocusable(false);
	}
	public JTextPane getTextPane() {
		return this.textPane;
	}
	public AbstractDocument getDocument() {
		return this.abstDoc;
	}
}
