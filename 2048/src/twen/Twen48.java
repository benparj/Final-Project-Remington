package twen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Twen48 extends JPanel 
{
  private static final int Square_SIZE = 50;

  private Square[] mySquares;
  boolean Win = false;
  boolean Lose = false;

  public Twen48() 
  {
    setFocusable(true);
    addKeyListener(
    		
    		new KeyAdapter() 
    		{
    
    	 public void left() 
    	 {
 		    boolean needT = false;
 		    for (int i = 0; i < 4; i++) 
 		    {
 		      
 		    	Square[] row = getrow(i);
 		      
 		      Square[] combined = combinerow(moverow(row));
 		      setrow(i, combined);
 		      if (!needT && !compare(row, combined)) 
 		      {
 		        needT = true;
 		      }
 		    }

 		    if (needT) {
 		      newSquare();
 		}
  }
    	 
    	 
      public void keyPressed(KeyEvent e) 
      {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) 
        {
          restart();
        }
        if (!canMove()) {
          Lose = true;
        }

        if (!Win && !Lose) {
          switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
              left();
              break;
              
            case KeyEvent.VK_RIGHT:
            	 mySquares = rotate(180);
            	    left();
            	    mySquares = rotate(180);
            	    break;
              
            case KeyEvent.VK_UP:
            	 mySquares = rotate(270);
            	    left();
            	 mySquares = rotate(90);
              break;
              
            case KeyEvent.VK_DOWN:
           	 mySquares = rotate(90);
           	    left();
           	    mySquares = rotate(270);
             break;
          }
        
        }
        
        if (!canMove() && !Win) {
          Lose = true;
        }

        repaint();
      }
    }
    );
    
    restart();
  }

  public void restart() {
	Lose = false;
	Win = false;
    mySquares = new Square[4 * 4];
    for (int i = 0; i < mySquares.length; i++) {
      mySquares[i] = new Square();
    }
    newSquare();
    newSquare();
  }
  
  
  
  
  private List<Square> openSq() 
  {
	    final List<Square> list = new ArrayList<Square>(16);
	    for (Square t : mySquares) 
	    {
	      if (t.isEmpty()) 
	      {
	        list.add(t);
	      }
	    }
	    return list;
  }

  
  
  private void newSquare() 
  {
    List<Square> list = openSq();
    if (!openSq().isEmpty()) 
    {
      int index = (int) (Math.random() * list.size());
      Square et = list.get(index);
      et.value = Math.random() < 0.1 ? 4 : 2;
    }
  }

  private boolean Dead() {
    return openSq().size() == 0;
  }

  
  private Square SquareAt(int x, int y) {
	    return mySquares[x + y * 4];
	  }
  
  
  boolean canMove() 
  {
    if (!Dead()) 
    {
      return true;
    }
    
    for (int x = 0; x < 4; x++) 
    {
      for (int y = 0; y < 4; y++) 
      {
        Square t = SquareAt(x, y);
        if ((x < 3 && t.value == SquareAt(x + 1, y).value)
          || ((y < 3) && t.value == SquareAt(x, y + 1).value)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean compare(Square[] row1, Square[] row2) 
  {
    if (row1 == row2) 
    {
      return true;
    }
    else 
    
    	return false;
  }

 
  private Square[] rotate(int angle) {
    Square[] newSquares = new Square[4 * 4];
    int OX = 3, OY = 3;
    
    if (angle == 270) 
    {
      OX = 0;
    }
    
    if (angle == 90) 
    {
      OY = 0;
    } 
    
    double rad = Math.toRadians(angle);
   
    int cos = (int) Math.cos(rad);
    int sin = (int) Math.sin(rad);
    for (int x = 0; x < 4; x++) 
    {
      for (int y = 0; y < 4; y++) 
      {
        int newX = (x * cos) - (y * sin) + OX;
        int newY = (x * sin) + (y * cos) + OY;
        newSquares[(newX) + (newY) * 4] = SquareAt(x, y);
      }
    }
    return newSquares;
  }

  
  
  private Square[] moverow(Square[] old) 
  {
    LinkedList<Square> l = new LinkedList<Square>();
    for (int i = 0; i < 4; i++) 
    {
      if (!old[i].isEmpty())
        l.addLast(old[i]);
    }
    if (l.size() == 0) 
    {
      return old;
    } 
    
    else 
    {
      Square[] newrow = new Square[4];
      enSize(l, 4);
      for (int i = 0; i < 4; i++) 
      {
        newrow[i] = l.removeFirst();
      }
      
      return newrow;
    }
  }

  private Square[] combinerow(Square[] old) 
  {
    List<Square> list = new ArrayList<Square>();
    for (int i = 0; i < 4 && !old[i].isEmpty(); i++) 
    {
      int num = old[i].value;
      if (i < 3 && old[i].value == old[i + 1].value) 
      {
        num *= 2;
        int goal = 2048;
        if (num == goal) 
        {
          Win = true;
        }
        i++;
      }
      list.add(new Square(num));
    }
    if (list.size() == 0) 
    {
      return old;
    } 
    else 
    {
      enSize(list, 4);
      return list.toArray(new Square[4]);
    }
  }

  private static void enSize(List<Square> l, int s) 
  {
    while (l.size() != s) {
      l.add(new Square());
    }
  }

  private Square[] getrow(int index) {
    Square[] result = new Square[4];
    for (int i = 0; i < 4; i++) {
      result[i] = SquareAt(i, index);
    }
    return result;
  }

  private void setrow(int index, Square[] r) {
    System.arraycopy(r, 0, mySquares, index * 4, 4);
  }

  public void paint(Graphics g) {
    super.paint(g);
    g.drawString("2048", 0, 270);
    g.drawString("by: Bennett Juwvipart", 0, 300); 
    g.drawString("Instructions: Slide Squares with", 0, 330);
    g.drawString("with arrow keys to reach 2048", 0, 345);


    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        drawSquare(g, mySquares[x + y * 4], x, y);
      }
    }
  }

  private void drawSquare(Graphics h, Square Square, int x, int y) {
	  
	Graphics2D g = ((Graphics2D) h);
    int value = Square.value;
    int xO = OCoors(x);
    int yO = OCoors(y);
    g.setColor(Square.getBackground());
    g.fillRoundRect(xO, yO, Square_SIZE, Square_SIZE, 14, 14);
    g.setColor(Square.getFont());

    String s = String.valueOf(value);


    if (value != 0)
      g.drawString(s, xO + (Square_SIZE) / 2, yO + Square_SIZE - (Square_SIZE) / 2 - 2);

    if (Win || Lose) {
      g.setColor(new Color(255, 255, 255, 30));
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(new Color(78, 139, 202));
      if (Win) {
        g.drawString("You win!", 68, 150);
      }
      if (Lose) {
        g.drawString("Game over", 50, 130);
        g.drawString("You lose", 64, 200);
      }
      if (Win || Lose) {
        g.setColor(new Color(128, 128, 128, 128));
        g.drawString("Press ENT to try again", 80, 460);
      }
    }
  }

  private static int OCoors(int a) {
	    return a * (Square_SIZE);
	  }

	  static class Square {
	    int value;

	    public Square() {
	    }

	    public Square(int num) {
	      value = num;
	    }

	    public boolean isEmpty() {
	      return value == 0;
	    }

    public Color getFont() {
      return new Color(0x776e65); 
    }

    public Color getBackground() {
      switch (value) {
        case 2:    return new Color(0xFAFAD2);
        case 4:    return new Color(0xF0E68C);
        case 8:    return new Color(0xFAA460);
        case 16:   return new Color(0xA0522D);
        case 32:   return new Color(0xBC8F8F);
        case 64:   return new Color(0x87CEEB);
        case 128:  return new Color(0xEE82EE);
        case 256:  return new Color(0x778899);
        case 512:  return new Color(0xFF0000);
        case 1024: return new Color(0xC0C0C0);
        case 2048: return new Color(0x808000);
      }
      return new Color(0xFFFFFF);
    }
  }

  public static void main(String[] args) {
    JFrame game = new JFrame();
    
    game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    game.setTitle("2048");
    game.setSize(500, 500);

    game.add(new Twen48());

    game.setVisible(true);
  }
}