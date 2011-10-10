import java.io.*;
import java.awt.*;
import java.net.URL;

public class Map {

public static final char WILDCARD_CHAR = '_';
private static final char INVALID_CHAR = '#';

private final int CHAR_PADDING = 4;
private final int MAP_PADDING = 20;

private char arr[][];
private int xSize, ySize;

/** 
create a map with the given dimensions, given the input map
description file. format of description file:
<xSize> <ySize>     <== x and y dimensions of the map
<y1> <startX1> <endX1> <== coordinates of map segments to be invalidated
<y2> <startX2> <endX2>
...  
(till eof)

WARNING: no error checking is done!! it is assumed that the input file is
well-formed.

@param filename name of a file to read the map description from
*/
public Map (String filename) throws IOException

{
    this.arr = new char[xSize][ySize];

    // read in the map description
    StreamTokenizer st = null;

    try 
    { 
        URL fileURL = Map.class.getResource(filename);
	st = new StreamTokenizer(fileURL.openStream());
        //st = new StreamTokenizer (new FileInputStream (filename));
    } catch (FileNotFoundException f)
    { 
        System.out.println ("Map definition file not found: " 
                                 + filename);
        return;
    }

    st.commentChar ('#');

    st.nextToken ();
    this.xSize = (int) st.nval;
    st.nextToken ();
    this.ySize = (int) st.nval;

    this.arr = new char[xSize][ySize];
 
    // mark all coordinates initially as valid
    for (int i = 0 ; i < ySize ; i++)
        for (int j = 0 ; j < xSize ; j++)
            this.arr[j][i] = WILDCARD_CHAR;
 
    // now mark off invalid coordinates by reading triples from the
    // file till we reach EOF

    while (st.nextToken() != StreamTokenizer.TT_EOF)
    {
        int y = (int) st.nval;

        st.nextToken ();
        int startX = (int) st.nval;
        st.nextToken ();
        int endX = (int) st.nval;
        // mark chars invalid
        for ( ; startX <= endX ; startX++)
            this.arr[startX][y] = INVALID_CHAR;
    }
}

/**
return char a given coordinate in the map. if the coordinate is
invalid, return INVALID_CHAR
@param x the x-coordinate
@param y the y-coordinate
@return the character at the given coordinate 
*/
private char getCharAt (int x, int y)

{
    if ((x >= xSize) || (x < 0) || (y >= ySize) || (y < 0))
        return Map.INVALID_CHAR;
    return arr[x][y];
}

/**
set a given coordinate in the map to a specific char
@param x the x-coordinate
@param y the y-coordinate
@param c the char the given coordinate is to be set to
*/
private void putCharAt (int x, int y, char c)

{
    arr[x][y] = c;
}

/**
get the string in the map, moving from (x, y) in the specified
direction
@param x x coordinate
@param y y coordinate
@param dir direction
@returns the string in the map starting at (x,y) in the specified direction
*/
public String getStringFromMap (int x, int y, int dir)

{
    StringBuffer sb = new StringBuffer ();

    int xinc = (dir == Direction.HORIZONTAL) ? 1 : 0;
    int yinc = (dir == Direction.HORIZONTAL) ? 0 : 1;
    
    char c;
    while ((c = getCharAt (x, y)) != Map.INVALID_CHAR)
    {
        sb.append (c);
        x += xinc;
        y += yinc;
    }

    return sb.toString ();
}

/*
set the string in the map moving from (x, y) in the specified
direction to the given string. no error checking on length of string,
valid x/y coordinates, etc.
@param x x coordinate
@param y y coordinate
@param dir direction
@param str the string the chars in the map are to be set to
*/
public void putStringIntoMap (int x, int y, int dir, String str)

{
    int xinc = (dir == Direction.HORIZONTAL) ? 1 : 0;
    int yinc = (dir == Direction.HORIZONTAL) ? 0 : 1;

    char c[] = str.toCharArray ();

    for (int i = 0 ; i < c.length ; i++)
    {
        putCharAt (x, y, c[i]);
        x += xinc;
        y += yinc;
    }
}

/**
get the beginning coordinate of the word which contains (x, y) in the
direction specified if there are any blank spaces remaining in the
word. if there are no blank spaces in that word return -1.
@param x x coordinate
@param y y coordinate
@param dir direction to move in (horiz => left, vert => up)
@returns the x or y coordinate at which the specified word begins (x
coordinate for horiz, y coordinate for vert)
*/
public int getNextMove (int x, int y, int dir)

{
    int xinc = (dir == Direction.HORIZONTAL) ? 1 : 0;
    int yinc = (dir == Direction.HORIZONTAL) ? 0 : 1;

    while (getCharAt (x - xinc, y - yinc) != Map.INVALID_CHAR)
    {
        x -= xinc; 
        y -= yinc; 
    }
 
    int startx = x;
    int starty = y;

    char c;
    while ((c = getCharAt (x, y)) != Map.INVALID_CHAR)
    {
     if (c == Map.WILDCARD_CHAR)
      return ((dir == Direction.HORIZONTAL) ? startx : starty);
     x += xinc;
     y += yinc;
    }

    return -1;
}

/** 
prints out the contents of the map 
*/
public String toString ()

{
    StringBuffer sb = new StringBuffer ();
    sb.append ("xSize = " + this.xSize + ", ySize = " + this.ySize + "\n");
    for (int i = 0 ; i < this.ySize ; i++)
    {
        for (int j = 0 ; j < this.xSize ; j++)
            sb.append (this.arr[j][i]);
        sb.append ("\n");
    }
    return sb.toString ();
}

/**
draw out the map 
*/
public void drawMap ()

{
    Frame f = new Map.MapFrame ();
    f.show ();
}

/* inner class */
class MapFrame extends CloseableFrame {

private FontMetrics fm;
private int width;
private int height;
private boolean sizeSet = false;  // is size set ?

private int i = 0;
/** setups the graphics context
 */
public void setup (Graphics g)

{
    Font defaultFont = g.getFont ();
    fm = g.getFontMetrics (defaultFont);
    width = fm.getMaxAdvance () + 2 * CHAR_PADDING;
    height = fm.getMaxAscent () + fm.getMaxDescent () + 2 * CHAR_PADDING;
        
    // set the size of the frame 
    sizeSet = true;
    setSize (getInsets ().left + xSize*width+MAP_PADDING*2,
             getInsets ().top + ySize*height+MAP_PADDING*2);
}

/** 
  well-known override
  repaints the xword
*/
public void paint (Graphics g)

{
    if (!sizeSet)
        setup (g);

    setTitle ("Crossword");

    // ?? why does this translate not work if put inside setup() ?
    g.translate (getInsets ().left, getInsets ().top);

    // draw outer rectangle 
    g.drawRect (MAP_PADDING, MAP_PADDING, 
                xSize * width, ySize * height);

    // draw inner lines - horiz and vertical
    for (int i = 1 ; i <= xSize ; i++)
    {
        g.drawLine (MAP_PADDING + i*width, MAP_PADDING, 
                    MAP_PADDING + i*width, MAP_PADDING + ySize * height);
    }
    for (int i = 1 ; i <= ySize ; i++)
    {
        g.drawLine (MAP_PADDING, MAP_PADDING + i*height,
                    MAP_PADDING + xSize * width, MAP_PADDING + i*height);
    }

    // write out the chars in the boxes
    for (int i = 0 ; i < xSize ; i++)
    {
        for (int j = 0 ; j < ySize ; j++)
	{
             if (arr[i][j] != Map.INVALID_CHAR)
	     {
                 g.drawChars (arr[i], j, 1,
                              MAP_PADDING + CHAR_PADDING + i*width,
                              MAP_PADDING + CHAR_PADDING + fm.getMaxAscent ()
                              + j * height);
	     }
             else
	     {
                 g.fillRect (MAP_PADDING+i*width, MAP_PADDING + j*height, 
                           width, height);
             }
	}
    }
}
}
}
