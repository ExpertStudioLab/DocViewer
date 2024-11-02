package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
			StyledDocument doc = this.textProperty.getTextPane().getStyledDocument();
			SimpleAttributeSet rightAlign = new SimpleAttributeSet();
			StyleConstants.setFontFamily(rightAlign, "Serif");
			StyleConstants.setBold( rightAlign, true );
			StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT );
			StyleConstants.setFontSize( rightAlign,  15 );
			this.textProperty.getDocument().insertString( this.textProperty.getDocument().getLength(),
					"【" + contents + "】" + endline, rightAlign );
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					doc.setParagraphAttributes(0, titlePosition, rightAlign, false);
				}
			});
		} else {
			if( type == 1 ) {
				this.titlePosition = this.textProperty.getDocument().getLength();
				StyledDocument doc = this.textProperty.getTextPane().getStyledDocument();
				SimpleAttributeSet lineSpace = new SimpleAttributeSet();
				StyleConstants.setFontFamily(lineSpace,  "Serif" );
				StyleConstants.setFontSize( lineSpace, 25 );
				StyleConstants.setBold( lineSpace, true );
				StyleConstants.setSpaceAbove( lineSpace, 15 );
				StyleConstants.setSpaceBelow( lineSpace, 10 );
				this.textProperty.getDocument().insertString( this.textProperty.getDocument().getLength(),
						contents + endline, lineSpace );
//			    this.textProperty.getTextPane().setParagraphAttributes( lineSpace, true);
				SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						doc.setParagraphAttributes(titlePosition, sectionPosition - titlePosition, lineSpace, false );						
					}
				});

			} else {
				if( type == 2 ) {
			
					StyleConstants.setFontSize( aSet, 20 );
					this.sectionPosition = this.textProperty.getDocument().getLength();
				} else if( type == 3 ) {
					StyleConstants.setFontSize( aSet, 17 );
					StyleConstants.setBold( aSet, false );
				}
				this.textProperty.getDocument().insertString( this.textProperty.getDocument().getLength(),
											contents + endline, aSet );
			}
		}
		} catch( Exception e ) {
		}
	}
	
	public void showWindow() throws BadLocationException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		MyLabel titleLabel = new MyLabel( );
		titleLabel.setBackground( new Color( 0.0f, 0.0f, 1.0f, 0.2f ) );
		titleLabel.setOpaque( true );
		MyLabel secLabel = new MyLabel();
		secLabel.setBackground( new Color( 0.4f, 0.8f, 0.2f, 0.2f ) );
		secLabel.setOpaque( true );

		JLayeredPane layeredPane = new JLayeredPane();

		layeredPane.add( titleLabel, 1 );
		layeredPane.add( secLabel, 0 );
		layeredPane.add( this.scrollPane, 2 );
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.yellow);
		JTextPane text = new JTextPane();
		text.setPreferredSize( new Dimension( screenSize.width / 6, 200 ));
		panel1.add(text);
		JPanel panel2 = new JPanel();
		panel2.setBackground( Color.green );

		this.setPreferredSize( new Dimension( screenSize.width, screenSize.height ) );
		this.getContentPane().setSize( new Dimension( screenSize.width, screenSize.height ));
		this.getContentPane().setLayout( new BorderLayout() );
		this.getContentPane().add( panel1, BorderLayout.WEST );
		this.getContentPane().add( panel2, BorderLayout.EAST );
		this.getContentPane().add( layeredPane, BorderLayout.CENTER );

		this.setFocusable( true );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setVisible( true );

		this.scrollPane.setBounds( 0, 0, screenSize.width * 4 / 6, screenSize.height );		
		
		JTextPane textPane = this.textProperty.getTextPane();
		JFrame frame = this;
		JScrollPane scrollPane = this.scrollPane;
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				Rectangle2D rect;

				try {
					rect = textPane.modelToView2D( titlePosition );
					titleLabel.setSize( new Dimension( 800, ( int )rect.getHeight() + 7 ) );
					titleLabel.setLocation( ( int )rect.getX(), ( int )rect.getY() - 20 );
						
					rect = textPane.modelToView2D(sectionPosition);
					secLabel.setSize( new Dimension( 400, ( int ) rect.getHeight() ) );
					secLabel.setLocation( ( int )rect.getX(), ( int ) rect.getY() -15 );
						
					scrollPane.getViewport().addChangeListener( new ScrollLabelListener( titleLabel, secLabel ) );
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
	MyLabel titleLabel;
	MyLabel secLabel;
	int titleX, titleY;
	int secX, secY;
	
	public ScrollLabelListener( MyLabel titleLabel, MyLabel secLabel ) {
		this.titleLabel = titleLabel;
		this.titleX = titleLabel.getLocation().x;
		this.titleY = titleLabel.getLocation().y + 19;
		this.secLabel = secLabel;
		this.secX = secLabel.getLocation().x;
		this.secY = secLabel.getLocation().y + 19;		
	}

	@Override
	public void stateChanged(ChangeEvent e) {

			// TODO Auto-generated method stub
			JViewport viewport = (JViewport) e.getSource();
        	Point viewPosition = viewport.getViewPosition();
        	// ここでJLayeredPane内のコンポーネントの位置を更新
        	titleLabel.setLocation( 0, 0);
        	secLabel.setLocation( 0, 0 );
        	titleLabel.repaint();
        	secLabel.repaint();
        	titleLabel.setX( titleX - viewPosition.x );
        	titleLabel.setY( titleY - viewPosition.y );
        	titleLabel.setLocation( titleLabel.getX(), titleLabel.getY() );
        	secLabel.setX( secX - viewPosition.x );
        	secLabel.setY( secY - viewPosition.y );
        	secLabel.setLocation(secLabel.getX(), secLabel.getY() );
        	titleLabel.repaint();
        	secLabel.repaint();
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