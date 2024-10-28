package procedure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

import window.View;

public class WndProc {
	private View wnd;
	public WndProc( ) {
		this.wnd = new View( this );
	}

	public void drawGraph( Graphics g ) {
        JLabel label = new JLabel( "Java�A�v���P�[�V�����̂����");
		Font font = new Font( "Meiryo", Font.BOLD, 20 );
		label.setFont( font );

		g.setFont( font );
		g.drawString( "Java�A�v���P�[�V�����̂����", 200, 50 );
	}
	public View getView() {
		return wnd;
	}
}
