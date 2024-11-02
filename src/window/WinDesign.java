package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class WinDesign extends JFrame {
	private String endline = "\r\n";
	private TextPaneProperty textProperty;
	private JScrollPane scrollPane;
	private int titlePosition, sectionPosition;
	private String contents;


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
		String line;
		boolean flag = false;
		try {
			FileInputStream fStream = new FileInputStream( filename );
			InputStreamReader streamReader = new InputStreamReader( fStream, "UTF-8" );
			@SuppressWarnings("resource")
			BufferedReader bufReader = new BufferedReader( streamReader );

			while( ( line = bufReader.readLine() ) != null ) {
				if( flag == false ) {
					if( line.indexOf( "<div id=\"Tag\">" ) != -1 ) {
						this.insertDocument( line, 0 );
					} else if( line.indexOf( "<h1>") != -1 ) {
						this.insertDocument( line, 1 );
					} else if( line.indexOf( "<h2") != -1 ) {
						this.insertDocument(line, 2);
					} else if( line.indexOf( "<p>" ) != -1 ) {
						flag = true;
					}
				} else {
					if( line.indexOf( "</p>" ) != -1 ) {
						flag = false;
					} else {
						this.insertDocument(line,  3 );
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void insertDocument( String line, int type ) {
		if( type != 3 ) {
			int index = line.indexOf( '>' );
			String tmp = line.substring( index + 1 );
			index = tmp.indexOf( '<' );
			contents = tmp.substring( 0, index );
		} else {
			contents = line;
		}
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontFamily(aSet, "Serif");
		StyleConstants.setBold(aSet,  true );
		try {
		if( type == 0 ) {
			StyleConstants.setFontSize( aSet,  15 );
			this.textProperty.getDocument().insertString( this.textProperty.getDocument().getLength(),
					"【" + contents + "】" + endline, aSet );
		} else {
			if( type == 1 ) {
				this.titlePosition = this.textProperty.getDocument().getLength();
				StyleConstants.setFontSize( aSet, 25 );
			     this.textProperty.getTextPane().setParagraphAttributes(aSet, true);
			} else if( type == 2 ) {
				StyleConstants.setFontSize( aSet, 20 );
				this.sectionPosition = this.textProperty.getDocument().getLength();
			} else if( type == 3 ) {
				StyleConstants.setFontSize( aSet, 17 );
				StyleConstants.setBold( aSet, false );
			}
			this.textProperty.getDocument().insertString( this.textProperty.getDocument().getLength(),
											contents + endline, aSet );
		}
		} catch( Exception e ) {
		}
	}
	
	public void showWindow() throws BadLocationException {
		SimpleAttributeSet rightAlign = new SimpleAttributeSet();
		StyleConstants.setAlignment( rightAlign, StyleConstants.ALIGN_RIGHT);
		StyledDocument doc = this.textProperty.getTextPane().getStyledDocument();
		doc.setParagraphAttributes(0, titlePosition, rightAlign, false);
		
		SimpleAttributeSet lineSpace = new SimpleAttributeSet();
		StyleConstants.setSpaceAbove( lineSpace, 3 );
		StyleConstants.setSpaceBelow( lineSpace, 3 );
		doc.setParagraphAttributes(titlePosition, sectionPosition - titlePosition, lineSpace, false );

		MyLabel titleLabel = new MyLabel( );
		titleLabel.setBackground( new Color( 0.0f, 0.0f, 1.0f, 0.2f ) );
		titleLabel.setOpaque( true );
		MyLabel secLabel = new MyLabel();
		secLabel.setBackground( new Color( 0.4f, 0.8f, 0.2f, 0.2f ) );
		secLabel.setOpaque( true );
		
		this.getLayeredPane().add( titleLabel, 1 );
		this.getLayeredPane().add( secLabel, 0 );
		this.getLayeredPane().add( this.scrollPane, 2 );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setVisible( true );
		this.scrollPane.setBounds( 20, 20, 800, 600 );

		JTextPane textPane = this.textProperty.getTextPane();
		JFrame frame = this;
		JScrollPane scrollPane = this.scrollPane;
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				Rectangle2D rect;
				Rectangle2D rect2;

				try {
					rect = textPane.modelToView2D( titlePosition );
						titleLabel.setSize( new Dimension( 800, ( int )rect.getHeight() + 7 ) );
						titleLabel.setLocation( ( int )rect.getX(), ( int )rect.getY() + 10 );
						
						rect2 = textPane.modelToView2D(sectionPosition);
						secLabel.setSize( new Dimension( 400, ( int ) rect2.getHeight() ) );
						secLabel.setLocation( ( int )rect2.getX(), ( int ) rect2.getY() );
						System.out.println( (int ) rect2.getY());
						
						System.out.println( secLabel.getLocation().y);	
						scrollPane.getViewport().addChangeListener( new ScrollLabelListener( titleLabel ) );
						frame.addKeyListener( new KeyDownListener( scrollPane ) );
						textPane.addMouseListener( new TextSelectedListener( titleLabel ) );

						frame.pack();

				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


	}
}

class MyLabel extends JLabel {
	private int x, y;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
	}
}


class ScrollLabelListener implements ChangeListener {
	MyLabel label;
	int x, y;
	
	public ScrollLabelListener( MyLabel label ) {
		this.label = label;
		this.x = label.getLocation().x;
		this.y = label.getLocation().y + 19;
	}

	@Override
	public void stateChanged(ChangeEvent e) {

			// TODO Auto-generated method stub
			JViewport viewport = (JViewport) e.getSource();
        	Point viewPosition = viewport.getViewPosition();
        	// ここでJLayeredPane内のコンポーネントの位置を更新
        	label.setLocation( 0, 0);
        	label.repaint();
        	label.setX( x - viewPosition.x );
        	label.setY( y - viewPosition.y );
        	label.setLocation( label.getX(), label.getY() );
        	label.repaint();
	}
}

class TextSelectedListener extends MouseAdapter {
	private MyLabel label;
	public TextSelectedListener( MyLabel label ) {
		this.label = label;
	}
	
	@Override
	public void mouseReleased( MouseEvent e ) {
		label.setLocation( 0, 0 );
		label.repaint();
		label.setLocation( label.getX(), label.getY() );
		label.repaint();
	}
}

class KeyDownListener extends KeyAdapter {
	private JScrollBar scrollBar;
	public KeyDownListener( JScrollPane scrollPane ) {
		this.scrollBar = scrollPane.getVerticalScrollBar();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		int dis;
		switch( keyCode ) {
			case KeyEvent.VK_UP:
				dis = -50;
				break;
			case KeyEvent.VK_DOWN:
				dis = 50;
				break;
			case KeyEvent.VK_SPACE:
				dis = 100;
				break;
			default:
				dis = 0;
		}
		this.scrollBar.setValue( this.scrollBar.getValue() + dis );
	}
}

class MyCaretEvent extends CaretEvent {

	public MyCaretEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getDot() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMark() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}