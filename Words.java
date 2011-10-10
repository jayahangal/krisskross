import java.util.*;
import java.io.*;
import java.net.URL;

public class Words {

// words database
private String wordsDB[][];
private boolean wordsDBUsed[][];

/** 
public constructor
@param filename the name of the file to read the words from. words are
placed one on a line in the input file.
*/
public Words (String filename) throws IOException

{
    // read in the map description
    StreamTokenizer st = null;

    try 
    { 
	URL fileURL = Words.class.getResource(filename);
        st = new StreamTokenizer(fileURL.openStream());
        //st = new StreamTokenizer (new FileInputStream (filename));
    } catch (FileNotFoundException f)
    { 
        System.out.println ("Words definition file not found: " 
                                 + filename);
        return;
    }

    st.commentChar ('#');

    int maxWordLen = 0;
    Vector svect = new Vector ();
    int lenFreqs[] = new int[100]; //max word len = 100

    // store all words in a vector first
    while (st.nextToken () != StreamTokenizer.TT_EOF)
    {
        String s = (String) st.sval;
        svect.addElement (s);
        int len = s.length ();
        lenFreqs[len]++;
        
        if (len > maxWordLen)
            maxWordLen = len;
    }

    // create wordsDB and wordsDBUsed
    wordsDB = new String[maxWordLen+1][];
    wordsDBUsed = new boolean[maxWordLen+1][];

    // create wordsDB first dimension arrays of size = frequency of
    // words of corr. length

    for (int i = 0 ; i <= maxWordLen ; i++)
    {
         wordsDB[i] = new String[lenFreqs[i]];
         wordsDBUsed[i] = new boolean[lenFreqs[i]];
    }
    // transfer words from vector into wordsDB    
    Enumeration e = svect.elements ();
    while (e.hasMoreElements ())
    {
        String str = (String) e.nextElement ();
        int len = str.length ();
        wordsDB[len][lenFreqs[len]-1] = str;
        lenFreqs[len]--;
    }
}

/**
return true if the given string matches the wildcard
lengths are assumed to be the same
@param wildcardString
*/
private boolean wildcardMatch (String wildcardString, String matchString)

{
    char c1[] = wildcardString.toCharArray ();
    char c2[] = matchString.toCharArray ();
    for (int i = 0 ; i < c1.length ; i++)
        if ((c1[i] != Map.WILDCARD_CHAR) && (c1[i] != c2[i]))
            return false;

    return true;
}

/**
return the number of strings which match the given string (which may
have wildcards, specified with Map.INVALID_CHAR
@param str the string to be matched
@returns the count of strings in the database which matched the input string
*/
int nMatchingStrings (String str)

{
    int len = str.length ();
    int matchCount = 0;

    for (int i = 0 ; i < wordsDB[len].length ; i++)
    {
        if (!wordsDBUsed[len][i] &&
            wildcardMatch (str, wordsDB[len][i]))
        {
            matchCount ++;
        }
    }
 
    return matchCount;
}

/**
get a specific occurrence of a wildcard matching string from the words
database. 
@param String the match string
@param count the count'th string matching the wildcard is to be
returned. for the first occurrence, count is 1.
@returns a matching string in the words database, null if the occurrence 
         is not found
*/
public String getMatchingString (String str, int count)

{
    int len = str.length ();

    for (int i = 0 ; i < wordsDB[len].length ; i++)
        if (!wordsDBUsed[len][i] &&
            wildcardMatch (str, wordsDB[len][i]))
        {
            count--;
            if (count == 0)
                return wordsDB[len][i];
        }

    return null;
}

/**
set the used bit on a particular word in the words DB.
@param str the word
@param used the new value of the used bit
*/
public void setUsedBit (String str, boolean used)

{
    int len = str.length ();
    for (int i = 0 ; i < wordsDB[len].length ; i++)
        if (str.equals (wordsDB[len][i]))
        {
            wordsDBUsed[len][i] = used;
            return;
        }

    System.out.println ("Error: no match for word " + str);
}

/**
finds out if all words in this database are marked used
@returns true if all words in this database are marked used, false otherwise
*/
public boolean allWordsUsed ()

{
    for (int i = 0 ; i < wordsDB.length ; i++)
        for (int j = 0 ; j < wordsDB[i].length ; j++)
            if (!wordsDBUsed[i][j])
                return false;
    return true;
}

/** 
prints out all words in this database of words
*/
public void printAllWords ()

{
    System.out.println ("Words database:");
    for (int i = 0 ; i < wordsDB.length ; i++)
        for (int j = 0 ; j < wordsDB[i].length ; j++)
            System.out.println (wordsDB[i][j]);
}

public boolean MatchString(String str)

{
    int len = str.length();
    for(int i = 0; i < wordsDB[len].length; i++)
        if(str.equals (wordsDB[len][i]))
          return (true); 
    return (false);
}

/**
get the used bit on a particular word in the words DB.
@param str the word
*/
public boolean getUsedBit (String str)

{
    int len = str.length ();
    for (int i = 0 ; i < wordsDB[len].length ; i++)
        if (str.equals (wordsDB[len][i]))
        {
            return (wordsDBUsed[len][i]);
        }

    System.out.println ("Error: no match for word " + str);
    return (false);
}

}
