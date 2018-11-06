import java.awt.Graphics;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class Juanito extends Personaje {
	private JuanitoStateContext juanitoStateContext;
	private boolean right;
	private boolean left;
	private boolean up;
	private float dt;
	private int gravity;
	private Rectangle rectangulo;
	private int referencia;
	private ImageIcon jmoveizq;
	private ImageIcon jmoveder;
	private ImageIcon jstillizq;
	private ImageIcon jstillder;
	private ImageIcon jsaltader;
	private ImageIcon jsaltaizq;
	
	public Juanito(int x, int y, int w, int h, String path) {
		super(x,y,w,h, 5, 3, 0, path);
		right = false;
		left = false;
		dt = (float) 0.666;
		gravity = 2;	
		juanitoStateContext = new JuanitoStateContext();
		rectangulo = new Rectangle();
		System.out.println("Creaste un juanito e inicializaste su contexto");
		referencia = this.getY();
		
		jmoveder= new ImageIcon("img/jmoveder.gif");
		jmoveizq= new ImageIcon("img/jmoveizq.gif");
		jstillder= new ImageIcon("img/jstillder.png");
		jstillizq= new ImageIcon("img/jstillizq.png");
		jsaltader= new ImageIcon("img/jsaltader.png");
		jsaltaizq= new ImageIcon("img/jsaltaizq.png");
		
	}
	public JuanitoStateContext getContextoEstado() {return this.juanitoStateContext;}
	
	public void movingWithLandscape(Background fg, Background mg, Background bg, Obstaculos obs) {
		
		updateIcon(); // cambia de imagen dependiendo de la tecla presionada o no presionada
		
		if(right){
			if(x<480 || (fg.getX()<=-3240 && x+width<=1070)) {x += velx;}
			else {
				fg.setX(-3);
				if(fg.getX()!=-3240) {obs.avanzar(-3);}
				if(fg.getX()>-3240) {
					mg.setX(-2);
					bg.setX(-1);
				}
			}
		}
		
		if(left){  
		   if(x>480 || (fg.getX()>=0 && x>0)) {x -= velx;}
		   else {
			   fg.setX(3);
			   if(fg.getX()!=0) {obs.avanzar(3);}
			   if(fg.getX()>-3240) { 
				   mg.setX(2);
				   bg.setX(1);
			   }
		   }  
	   }
		
		if(up || this.y < referencia-10){ // -10 due to relative error
		   juanitoStateContext.setCurrent(juanitoStateContext.getMovingState());
		   juanitoStateContext.getCurrent().jump(this);
	   }
	}
	
	public void move(KeyEvent e) {
		int key = e.getKeyCode();	
		switch(key) {
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_UP:
			up = true;
			juanitoStateContext.getCurrent().jump(this);
			if(vely != -25 && this.y>=590) {vely = -25;} //Complemented with -&& 590-
			break;
		}
		if(right || left || up){juanitoStateContext.getCurrent().move();}
		else {juanitoStateContext.getCurrent().stop();}
	}
	
	public void stop(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		}if(!(right && left && up)) {
			juanitoStateContext.setCurrent(juanitoStateContext.getStaticState());
			juanitoStateContext.getCurrent().stop();
		}
	}
	
	public void updateIcon() {
		if(right) {
			this.setIcon(jmoveder);
		}
		
		if(left) {
			this.setIcon(jmoveizq);
		}
		
		if(right && up==true) {
			this.setIcon(jsaltader);
		}
		
		if(left && up==true) {
			this.setIcon(jsaltaizq);
		}
		
		if(right==false && left==false && up==false && (this.getIcon()==jmoveder || this.getIcon()==jsaltader)) {
			this.setIcon(jstillder);
		}
		
		if(right==false && left==false && up==false && (this.getIcon()==jmoveizq || this.getIcon()==jsaltaizq)) {
			this.setIcon(jstillizq);
		}
		
		if(up==true && left==false && right==false && this.getIcon()==jstillder) {
			this.setIcon(jsaltader);
		}
		
		if(up==true && left==false && right==false && this.getIcon()==jstillizq) {
			this.setIcon(jsaltaizq);
		}
		
		
	}
	
	public void hurt() {juanitoStateContext.getCurrent().hurt();}
	public void heal() {juanitoStateContext.getCurrent().heal();}
	public void die() {juanitoStateContext.getCurrent().die();}
	
	//getters
	public float getDt() {return this.dt;}
	public int getGravity() {return this.gravity;}
	
	@Override
	public void draw(Graphics g) {
		mygif.paintIcon(null,g, x, y);
		g.drawRect(x, y, this.width, this.height);
	}
	
	public static BufferedImage horizontalflip(BufferedImage img) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);  
        g.dispose();  
        return dimg;  
    }  
}