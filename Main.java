import java.io.*; 
import java.util.*; 
 
public class Main { 
 
public static void main (String argv[]) throws IOException 
 
{ 
    Map map = new Map (argv[0]); 
    System.out.println (map); 
    Words words = new Words (argv[1]); 
    words.printAllWords (); 
// System.exit (1); 
    solve (map, words); 
    System.out.println (map);
    map.drawMap ();
} 
 
private static void solve (Map map, Words words) 
 
{ 
    boolean lastStateBacktracked = false; 
    boolean foundMove = true; 
    int x = 0, y = 0, count = 0, direction;
    Stack stack1 = new Stack (); 
    Stack stack2 = new Stack (); 
    int nBacktracks = 0, stackOpNo = 0, mapTraceNo = 0;

    int m = map.getNextMove (x, y, Direction.HORIZONTAL); 

    if(m != -1){ 
      x = m; 
      direction  = Direction.HORIZONTAL; 
    } 
    else { 
       m  =  map.getNextMove (x, y, Direction.VERTICAL); 
        y = m; 
        direction = Direction.VERTICAL; 
    } 
    debugPrint ("direction ="+ direction + "  x = " + x + " y  = " + y); 
    String mapStr = null, matchStr; 
    do 
    { 
        if (! lastStateBacktracked) 
        { 
            mapStr = map.getStringFromMap (x, y, direction); 
            debugPrint ("mapstr    =" + mapStr); 
            count = words.nMatchingStrings (mapStr); 
            debugPrint ("count = " + count); 
        } 
   
        if (count != 0) 
        { 
            matchStr = words.getMatchingString (mapStr, count); 
            debugPrint ("matchstr    =" + matchStr); 
            map.putStringIntoMap (x, y, direction, matchStr); 
            debugPrint (map.toString ());
            words.setUsedBit (matchStr, true); 
            count--; 
            StackNode snode = new StackNode (x, y, count, direction, mapStr); 
            stack1.push (snode); 
            lastStateBacktracked = false; 
        } 
        else
        { 
            lastStateBacktracked = true; 
            nBacktracks++;
            if (!stack1.empty ()) 
            { 
               StackNode snode = (StackNode) stack1.pop (); 
               x = snode.getX (); 
               y = snode.getY (); 
               mapStr = snode.getStr (); 
               direction = snode.getDirection (); 
               count = snode.getCount (); 
               String curMapStr = map.getStringFromMap (x, y, direction); 
               map.putStringIntoMap (x, y, direction, mapStr); 
               words.setUsedBit (curMapStr, false); 
               debugPrint  (snode.toString ()); 
           }
        } 
 
        if (!lastStateBacktracked) 
        { 
            do 
            { 
                StackNode sn = (StackNode) stack1.pop (); 
                stack2.push (sn); 
                debugPrint (sn.toString ()); 
                foundMove = false; 
                stackOpNo++; 
 
                for (int i = 0 ; i < (sn.getStr ()).length () ; i++) 
		{     
		    int moved;  
                    mapTraceNo++;   
		    if (sn.getDirection () == Direction.HORIZONTAL){ 
			moved = map.getNextMove (sn.getX ()+i, 
					         sn.getY (), 
					         Direction.VERTICAL); 
                        debugPrint ("  moved = " + moved); 
                    } 
		    else 
		        moved = map.getNextMove(sn.getX (), 
					        sn.getY ()+i, 
   					        Direction.HORIZONTAL); 
		    if (moved != -1) 
		    { 
			foundMove = true; 
			if (sn.getDirection () == Direction.HORIZONTAL) 
			{ 
			    y = moved; 
			    x = sn.getX () + i; 
                            direction = Direction.VERTICAL; 
			}  
			else 
			{ 
			    x = moved; 
			    y = sn.getY () + i; 
                            direction = Direction.HORIZONTAL;    
			} 
			break; 
		    } 
		} 
               
            debugPrint ("  x = " + x + " y  = " + y); 

            //System.exit (1);
            } while (!foundMove && !stack1.empty ()); 
             
            while (!stack2.empty ()) 
	    { 
                stack1.push (stack2.pop ()); 
	    } 
        } 
    } while (foundMove);
    System.out.println ("stackOpNo = " + stackOpNo); 
    System.out.println ("mapTraceNo = " + mapTraceNo);
    System.out.println ("backtracks = " + nBacktracks); 
} 

static void debugPrint (String s)

{
//    System.out.println (s);
}
 
} 
