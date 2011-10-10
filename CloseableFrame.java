/**
 * A frame that terminates the program when the user closes it
 * @version 1.10 06 May 1997
 * @author Cay Horstmann
 */

import java.awt.*;
import java.awt.event.*;

public class CloseableFrame extends Frame 
{  public CloseableFrame()
   {  addWindowListener(new WindowAdapter() { public void
         windowClosing(WindowEvent e) { System.exit(0); } } );
      setSize(300, 200);
      setTitle(getClass().getName());
   }
}

