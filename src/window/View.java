package window;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;


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

		winDesign.showWindow();
	}

}

class TextPaneProperty {
	private JTextPane textPane;
	private AbstractDocument abstDoc;
	public TextPaneProperty() {
		this.textPane = new JTextPane();
		this.textPane.setCaretPosition( 0 );
		this.textPane.setMargin( new Insets( 20, 40, 20, 40 ) );
		this.abstDoc = ( AbstractDocument ) this.textPane.getStyledDocument();
		this.abstDoc.setDocumentFilter( new DocumentSizeFilter ( 300 ));
		this.textPane.setEditable( true );
		this.textPane.setFont( new Font( "Meiryo", Font.LAYOUT_NO_LIMIT_CONTEXT, 30 ) );
	}
	public JTextPane getTextPane() {
		return this.textPane;
	}
	public AbstractDocument getDocument() {
		return this.abstDoc;
	}
}

class DocumentSizeFilter extends DocumentFilter {
    int maxCharacters;
    boolean DEBUG = false;

    public DocumentSizeFilter(int maxChars) {
        maxCharacters = maxChars;
    }

    public void insertString(FilterBypass fb, int offs,
                             String str, AttributeSet a)
        throws BadLocationException {
        if (DEBUG) {
            System.out.println("in DocumentSizeFilter's insertString method");
        }

        //This rejects the entire insertion if it would make
        //the contents too long. Another option would be
        //to truncate the inserted string so the contents
        //would be exactly maxCharacters in length.
        if ((fb.getDocument().getLength() + str.length()) <= maxCharacters)
            super.insertString(fb, offs, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }
    
    public void replace(FilterBypass fb, int offs,
                        int length, 
                        String str, AttributeSet a)
        throws BadLocationException {
        if (DEBUG) {
            System.out.println("in DocumentSizeFilter's replace method");
        }
        //This rejects the entire replacement if it would make
        //the contents too long. Another option would be
        //to truncate the replacement string so the contents
        //would be exactly maxCharacters in length.
        if ((fb.getDocument().getLength() + str.length()
             - length) <= maxCharacters)
            super.replace(fb, offs, length, str, a);
        else
            Toolkit.getDefaultToolkit().beep();
    }

}
