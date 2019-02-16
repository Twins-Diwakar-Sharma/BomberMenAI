package base;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import object.FullMenu;

public class MouseInput implements MouseListener{

	FullMenu fullMenu;
	
	public MouseInput(FullMenu menu) {
		this.fullMenu = menu;
	}
	


@Override
public void mouseClicked(MouseEvent arg0) {
	// TODO Auto-generated method stub
	fullMenu.mouseClicked(arg0);
}

@Override
public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub

}

@Override
public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub

}

@Override
public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}


}
