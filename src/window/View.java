package window;
import procedure.WndProc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class View extends JFrame {
	  public View( WndProc proc ){
	        this.setSize(800, 500);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        MyPanel panel = new MyPanel( this, proc );
			JLabel label = new JLabel( "Hello, Java" );
			Font font = new Font( "Meiryo", Font.BOLD, 50 );
			label.setFont(font);
			label.setSize( 200, 200 );
			label.setLocation(200, 200);
			this.getContentPane().add( label );
	        this.add( panel );
	        panel.setOpaque( true );
	        this.setVisible(true);
	    }

	public static void main(String[] args) {
		WndProc proc = new WndProc( );
	}
}

@SuppressWarnings("serial")
class MyPanel extends JPanel {
	View view;
	private WndProc proc;
	
	public MyPanel( View view, WndProc proc ) {
		this.view = view;
		this.proc = proc;
	}

	public void paint( Graphics g ) {
        	g.setColor(Color.RED);
        	g.fillOval(0, 0, 100, 100);
        	proc.drawGraph( g );       
	}
    public void paintComponent(Graphics g){
    }
}
