public class StackNode {

private int count, x, y, direction;
private String str;

/** public constructor
*/
public StackNode (int x, int y, int count, int direction, String str)

{
    this.count = count;
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.str = str;
}

/** 
accessor for x
@returns value of x for this object
*/
public int getX ()

{
    return this.x;
}

/** 
accessor for y
@returns value of y for this object
*/
public int getY ()

{
    return this.y;
}

/** 
accessor for count
@returns value of count for this object
*/
public int getCount ()

{
    return this.count;
}

/** 
accessor for direction
@returns value of direction for this object
*/
public int getDirection ()

{
    return this.direction;
}

/** 
accessor for str
@returns value of y for this object
*/
public String getStr ()

{
    return this.str;
}

/** 
 well-known override 
*/
public String toString () 

{
    StringBuffer sb = new StringBuffer ();

    sb.append ("str = " + this.str);
    sb.append (", x = " + this.x);
    sb.append (", y = " + this.y);
    sb.append (", count = " + this.count);
    sb.append (", direction = " + this.direction);

    return sb.toString ();
}
}
