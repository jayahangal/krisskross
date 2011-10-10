import java.awt.*;
import java.io.*;

public class Picture{

		private Grid grid;
		private Line linePattern[];
               

        public void Picture(Grid grid, Line linePattern[]){

			   this.grid = grid;
			   for(int i = 0; i < linePattern.length; i++)	
			       this.linePattern[i] = linePattern[i];
			   	
		}


        public void flip(){
               this.grid.translate( ( -1 * this.grid.getw()), 0); /* flip(-x, y) */
        } 
        
	public void drawp ()
	{
        	Frame f = new Picture.pFrame ();
 	        f.Show ();
	}

		
	class pFrame extends CloseableFrame {

		public void paint(Graphics g){
    			 g.translate (getInsets ().left, getInsets ().top);
			 this.grid.paint(g);
			 for( int i = 0; i < this.linePattern.length; i++){
				this.linePattern[i].translate( this.grid.getw(), this.grid.geth());
				this.linePattern[i].paint(g);
				/* translate back */	
				this.linePattern[i].translate( -1 * (this.grid.getw()), -1 * (this.grid.geth()));
			 }
			
		}		
       }
				
}		
		
		 	
