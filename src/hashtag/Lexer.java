/* The following code was generated by JFlex 1.6.0 */

package hashtag;
import java_cup.runtime.Symbol;

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.0
 * from the specification file <tt>src/hashtag/hashtag.flex</tt>
 */
class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int COMMENT = 2;
  public static final int SSTRING = 4;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2, 2
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\4\1\7\1\10\1\10\1\7\22\0\1\4\1\27\1\13"+
    "\1\14\1\0\1\32\1\0\1\6\1\30\1\31\1\20\1\15\1\33"+
    "\1\16\1\5\1\17\12\1\1\35\1\34\1\22\1\23\1\21\2\0"+
    "\32\2\4\0\1\3\1\0\1\46\1\41\1\44\1\37\1\43\1\52"+
    "\1\51\1\45\1\36\1\2\1\55\1\42\1\54\1\24\1\25\1\56"+
    "\1\2\1\47\1\50\1\26\1\40\1\57\1\53\3\2\1\11\1\0"+
    "\1\12\7\0\1\10\u1fa2\0\1\10\1\10\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\udfe6\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\0\1\2\1\3\1\4\1\5\1\2"+
    "\1\6\1\7\1\5\1\10\1\11\1\12\1\13\1\14"+
    "\1\15\1\16\3\4\1\2\1\17\1\20\1\21\1\22"+
    "\1\23\1\24\15\4\1\1\1\25\1\26\1\27\2\0"+
    "\1\30\1\31\1\32\2\4\1\33\1\4\1\34\1\4"+
    "\1\35\1\36\22\4\1\37\1\40\1\41\2\4\1\42"+
    "\4\4\1\43\3\4\1\44\4\4\1\45\7\4\1\46"+
    "\4\4\1\47\1\50\1\51\11\4\1\52\1\53\2\4"+
    "\1\54\1\55\10\4\1\56\1\57\1\4\1\60\1\61"+
    "\1\4\1\62\4\4\1\63\1\64\2\4\1\65\1\66"+
    "\6\4\1\67\1\4\1\70\3\4\1\71\1\72\1\73";

  private static int [] zzUnpackAction() {
    int [] result = new int[166];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\60\0\140\0\220\0\300\0\360\0\220\0\u0120"+
    "\0\220\0\220\0\u0150\0\220\0\220\0\220\0\220\0\u0180"+
    "\0\u01b0\0\u01e0\0\u0210\0\u0240\0\u0270\0\u02a0\0\220\0\220"+
    "\0\220\0\220\0\220\0\220\0\u02d0\0\u0300\0\u0330\0\u0360"+
    "\0\u0390\0\u03c0\0\u03f0\0\u0420\0\u0450\0\u0480\0\u04b0\0\u04e0"+
    "\0\u0510\0\u0540\0\220\0\220\0\220\0\u0570\0\u05a0\0\220"+
    "\0\220\0\220\0\u05d0\0\u0600\0\360\0\u0630\0\220\0\u0660"+
    "\0\360\0\u0690\0\u06c0\0\u06f0\0\u0720\0\u0750\0\u0780\0\u07b0"+
    "\0\u07e0\0\u0810\0\u0840\0\u0870\0\u08a0\0\u08d0\0\u0900\0\u0930"+
    "\0\u0960\0\u0990\0\u09c0\0\u09f0\0\u0570\0\220\0\360\0\u0a20"+
    "\0\u0a50\0\360\0\u0a80\0\u0ab0\0\u0ae0\0\u0b10\0\360\0\u0b40"+
    "\0\u0b70\0\u0ba0\0\360\0\u0bd0\0\u0c00\0\u0c30\0\u0c60\0\360"+
    "\0\u0c90\0\u0cc0\0\u0cf0\0\u0d20\0\u0d50\0\u0d80\0\u0db0\0\360"+
    "\0\u0de0\0\u0e10\0\u0e40\0\u0e70\0\360\0\360\0\360\0\u0ea0"+
    "\0\u0ed0\0\u0f00\0\u0f30\0\u0f60\0\u0f90\0\u0fc0\0\u0ff0\0\u1020"+
    "\0\360\0\360\0\u1050\0\u1080\0\360\0\360\0\u10b0\0\u10e0"+
    "\0\u1110\0\u1140\0\u1170\0\u11a0\0\u11d0\0\u1200\0\360\0\360"+
    "\0\u1230\0\360\0\360\0\u1260\0\360\0\u1290\0\u12c0\0\u12f0"+
    "\0\u1320\0\360\0\360\0\u1350\0\u1380\0\360\0\360\0\u13b0"+
    "\0\u13e0\0\u1410\0\u1440\0\u1470\0\u14a0\0\360\0\u14d0\0\360"+
    "\0\u1500\0\u1530\0\u1560\0\360\0\360\0\360";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[166];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\4\1\5\1\6\1\4\1\7\1\4\1\10\1\7"+
    "\1\4\1\11\1\4\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36"+
    "\1\6\1\37\1\6\1\40\1\41\1\6\1\42\1\43"+
    "\1\44\1\6\1\45\1\46\1\47\1\6\1\50\1\51"+
    "\12\52\1\53\45\52\7\54\2\0\2\54\1\55\44\54"+
    "\61\0\1\5\3\0\1\56\53\0\3\6\20\0\3\6"+
    "\7\0\22\6\7\57\2\0\47\57\7\13\2\0\47\13"+
    "\23\0\1\60\57\0\1\61\57\0\1\62\35\0\3\6"+
    "\20\0\1\6\1\63\1\6\7\0\22\6\1\0\3\6"+
    "\20\0\2\6\1\64\7\0\11\6\1\65\10\6\1\0"+
    "\3\6\20\0\3\6\7\0\11\6\1\66\10\6\23\0"+
    "\1\67\35\0\3\6\20\0\1\70\2\6\7\0\14\6"+
    "\1\71\5\6\1\0\3\6\20\0\1\6\1\72\1\6"+
    "\7\0\22\6\1\0\3\6\20\0\1\6\1\73\1\6"+
    "\7\0\5\6\1\74\3\6\1\75\10\6\1\0\3\6"+
    "\20\0\1\76\2\6\7\0\4\6\1\77\15\6\1\0"+
    "\3\6\20\0\3\6\7\0\7\6\1\100\1\101\11\6"+
    "\1\0\3\6\20\0\1\102\2\6\7\0\22\6\1\0"+
    "\3\6\20\0\3\6\7\0\5\6\1\103\14\6\1\0"+
    "\3\6\20\0\2\6\1\104\7\0\15\6\1\105\4\6"+
    "\1\0\3\6\20\0\1\6\1\106\1\6\7\0\2\6"+
    "\1\107\5\6\1\110\11\6\1\0\3\6\20\0\3\6"+
    "\7\0\7\6\1\111\12\6\1\0\3\6\20\0\3\6"+
    "\7\0\10\6\1\112\11\6\1\0\3\6\20\0\3\6"+
    "\7\0\11\6\1\113\10\6\1\0\3\6\20\0\1\6"+
    "\1\114\1\6\7\0\22\6\12\52\1\0\45\52\1\0"+
    "\1\115\64\0\1\116\52\0\3\6\20\0\2\6\1\117"+
    "\7\0\22\6\1\0\3\6\20\0\3\6\7\0\7\6"+
    "\1\120\12\6\1\0\3\6\20\0\3\6\7\0\2\6"+
    "\1\121\17\6\1\0\3\6\20\0\2\6\1\122\7\0"+
    "\22\6\1\0\3\6\20\0\3\6\7\0\2\6\1\123"+
    "\17\6\1\0\3\6\20\0\1\6\1\124\1\6\7\0"+
    "\22\6\1\0\3\6\20\0\3\6\7\0\13\6\1\125"+
    "\6\6\1\0\3\6\20\0\3\6\7\0\5\6\1\126"+
    "\14\6\1\0\3\6\20\0\3\6\7\0\1\6\1\127"+
    "\20\6\1\0\3\6\20\0\3\6\7\0\12\6\1\130"+
    "\7\6\1\0\3\6\20\0\3\6\7\0\10\6\1\131"+
    "\11\6\1\0\3\6\20\0\3\6\7\0\12\6\1\132"+
    "\7\6\1\0\3\6\20\0\3\6\7\0\1\6\1\133"+
    "\20\6\1\0\3\6\20\0\2\6\1\134\7\0\10\6"+
    "\1\135\11\6\1\0\3\6\20\0\3\6\7\0\11\6"+
    "\1\136\10\6\1\0\3\6\20\0\3\6\7\0\1\137"+
    "\21\6\1\0\3\6\20\0\3\6\7\0\11\6\1\140"+
    "\10\6\1\0\3\6\20\0\1\141\2\6\7\0\22\6"+
    "\1\0\3\6\20\0\3\6\7\0\4\6\1\142\15\6"+
    "\1\0\3\6\20\0\3\6\7\0\1\143\21\6\1\0"+
    "\3\6\20\0\3\6\7\0\1\144\21\6\1\0\3\6"+
    "\20\0\3\6\7\0\1\145\21\6\1\0\3\6\20\0"+
    "\3\6\7\0\1\146\21\6\1\0\3\6\20\0\3\6"+
    "\7\0\5\6\1\147\14\6\1\0\3\6\20\0\3\6"+
    "\7\0\5\6\1\150\14\6\1\0\3\6\20\0\3\6"+
    "\7\0\3\6\1\151\16\6\1\0\3\6\20\0\3\6"+
    "\7\0\4\6\1\152\15\6\1\0\3\6\20\0\3\6"+
    "\7\0\1\153\21\6\1\0\3\6\20\0\3\6\7\0"+
    "\10\6\1\154\11\6\1\0\3\6\20\0\3\6\7\0"+
    "\5\6\1\155\14\6\1\0\3\6\20\0\3\6\7\0"+
    "\11\6\1\156\10\6\1\0\3\6\20\0\3\6\7\0"+
    "\5\6\1\157\14\6\1\0\3\6\20\0\3\6\7\0"+
    "\2\6\1\160\17\6\1\0\3\6\20\0\3\6\7\0"+
    "\1\6\1\161\20\6\1\0\3\6\20\0\3\6\7\0"+
    "\1\162\21\6\1\0\3\6\20\0\2\6\1\163\7\0"+
    "\22\6\1\0\3\6\20\0\3\6\7\0\6\6\1\164"+
    "\13\6\1\0\3\6\20\0\3\6\7\0\12\6\1\165"+
    "\7\6\1\0\3\6\20\0\3\6\7\0\4\6\1\166"+
    "\15\6\1\0\3\6\20\0\1\167\2\6\7\0\22\6"+
    "\1\0\3\6\20\0\1\170\2\6\7\0\22\6\1\0"+
    "\3\6\20\0\3\6\7\0\1\6\1\171\20\6\1\0"+
    "\3\6\20\0\3\6\7\0\11\6\1\172\10\6\1\0"+
    "\3\6\20\0\3\6\7\0\4\6\1\173\15\6\1\0"+
    "\3\6\20\0\3\6\7\0\5\6\1\174\14\6\1\0"+
    "\3\6\20\0\1\175\2\6\7\0\22\6\1\0\3\6"+
    "\20\0\3\6\7\0\17\6\1\176\2\6\1\0\3\6"+
    "\20\0\3\6\7\0\11\6\1\177\10\6\1\0\3\6"+
    "\20\0\3\6\7\0\1\200\1\201\4\6\1\202\3\6"+
    "\1\203\7\6\1\0\3\6\20\0\1\204\2\6\7\0"+
    "\22\6\1\0\3\6\20\0\3\6\7\0\6\6\1\205"+
    "\13\6\1\0\3\6\20\0\2\6\1\206\7\0\22\6"+
    "\1\0\3\6\20\0\3\6\7\0\5\6\1\207\14\6"+
    "\1\0\3\6\20\0\3\6\7\0\5\6\1\210\14\6"+
    "\1\0\3\6\20\0\3\6\7\0\3\6\1\211\16\6"+
    "\1\0\3\6\20\0\2\6\1\212\7\0\22\6\1\0"+
    "\3\6\20\0\3\6\7\0\5\6\1\213\14\6\1\0"+
    "\3\6\20\0\3\6\7\0\10\6\1\214\11\6\1\0"+
    "\3\6\20\0\1\215\2\6\7\0\22\6\1\0\3\6"+
    "\20\0\1\216\2\6\7\0\22\6\1\0\3\6\20\0"+
    "\1\6\1\217\1\6\7\0\22\6\1\0\3\6\20\0"+
    "\3\6\7\0\7\6\1\220\12\6\1\0\3\6\20\0"+
    "\2\6\1\221\7\0\22\6\1\0\3\6\20\0\3\6"+
    "\7\0\13\6\1\222\6\6\1\0\3\6\20\0\3\6"+
    "\7\0\7\6\1\223\12\6\1\0\3\6\20\0\3\6"+
    "\7\0\1\224\21\6\1\0\3\6\20\0\3\6\7\0"+
    "\5\6\1\225\14\6\1\0\3\6\20\0\1\226\2\6"+
    "\7\0\22\6\1\0\3\6\20\0\2\6\1\227\7\0"+
    "\22\6\1\0\3\6\20\0\3\6\7\0\2\6\1\230"+
    "\17\6\1\0\3\6\20\0\3\6\7\0\10\6\1\231"+
    "\11\6\1\0\3\6\20\0\3\6\7\0\11\6\1\232"+
    "\10\6\1\0\3\6\20\0\1\6\1\233\1\6\7\0"+
    "\22\6\1\0\3\6\20\0\3\6\7\0\13\6\1\234"+
    "\6\6\1\0\3\6\20\0\3\6\7\0\3\6\1\235"+
    "\16\6\1\0\3\6\20\0\3\6\7\0\11\6\1\236"+
    "\10\6\1\0\3\6\20\0\3\6\7\0\1\237\21\6"+
    "\1\0\3\6\20\0\1\240\2\6\7\0\22\6\1\0"+
    "\3\6\20\0\3\6\7\0\1\241\21\6\1\0\3\6"+
    "\20\0\3\6\7\0\4\6\1\242\15\6\1\0\3\6"+
    "\20\0\1\243\2\6\7\0\22\6\1\0\3\6\20\0"+
    "\1\244\2\6\7\0\22\6\1\0\3\6\20\0\3\6"+
    "\7\0\5\6\1\245\14\6\1\0\3\6\20\0\3\6"+
    "\7\0\13\6\1\246\6\6";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5520];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\1\1\0\1\11\2\1\1\11\1\1\2\11"+
    "\1\1\4\11\7\1\6\11\16\1\3\11\2\0\3\11"+
    "\4\1\1\11\26\1\1\11\130\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[166];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
    /*para los simbolos generales*/
    private Symbol symbol(int type){
      return new JavaSymbol(type,yyline+1,yycolumn+1,yytext());
    }

    /*para el tipo de token con su valor*/
    private Symbol symbol (int type, Object value){
      return new JavaSymbol(type,yyline+1,yycolumn+1,yytext(),value);
    }

    StringBuilder string = new StringBuilder();
    StringBuilder comment = new StringBuilder();


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 166) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;           
    int totalRead = 0;
    while (totalRead < requested) {
      int numRead = zzReader.read(zzBuffer, zzEndRead + totalRead, requested - totalRead);
      if (numRead == -1) {
        break;
      }
      totalRead += numRead;
    }

    if (totalRead > 0) {
      zzEndRead += totalRead;
      if (totalRead == requested) { /* possibly more input available */
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      return false;
    }

    // totalRead = 0: End of stream
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 1: 
          { comment.append(yytext());
          }
        case 60: break;
        case 2: 
          { int l = yyline+1; int c = yycolumn+1;
                        Editor.console.setText(Editor.console.getText()+"Error: (line: " + l + ", column: " + c + "). Unrecognized token " + yytext() + " : Lexical error\n");
          }
        case 61: break;
        case 3: 
          { return symbol(sym.NUMERO, new Integer(yytext()));
          }
        case 62: break;
        case 4: 
          { return symbol(sym.IDENTIFICADOR, yytext());
          }
        case 63: break;
        case 5: 
          { /*Ignore*/
          }
        case 64: break;
        case 6: 
          { comment.setLength(0); yybegin(COMMENT);
          }
        case 65: break;
        case 7: 
          { string.setLength(0); yybegin(SSTRING);
          }
        case 66: break;
        case 8: 
          { return symbol(sym.SUMA);
          }
        case 67: break;
        case 9: 
          { return symbol(sym.MENOS);
          }
        case 68: break;
        case 10: 
          { return symbol(sym.DIV);
          }
        case 69: break;
        case 11: 
          { return symbol(sym.MULT);
          }
        case 70: break;
        case 12: 
          { return symbol(sym.MAYOR);
          }
        case 71: break;
        case 13: 
          { return symbol(sym.MENOR);
          }
        case 72: break;
        case 14: 
          { return symbol(sym.ASIGNACION);
          }
        case 73: break;
        case 15: 
          { return symbol(sym.PARIZQ);
          }
        case 74: break;
        case 16: 
          { return symbol(sym.PARDER);
          }
        case 75: break;
        case 17: 
          { return symbol(sym.MOD);
          }
        case 76: break;
        case 18: 
          { return symbol(sym.COMA);
          }
        case 77: break;
        case 19: 
          { return symbol(sym.PUNTOCOMA);
          }
        case 78: break;
        case 20: 
          { return symbol(sym.DOSPUNTOS);
          }
        case 79: break;
        case 21: 
          { yybegin (YYINITIAL);
          }
        case 80: break;
        case 22: 
          { string.append(yytext());
          }
        case 81: break;
        case 23: 
          { yybegin (YYINITIAL); return symbol(sym.CADENA, string.toString());
          }
        case 82: break;
        case 24: 
          { return symbol(sym.MAYORIGUAL);
          }
        case 83: break;
        case 25: 
          { return symbol(sym.MENORIGUAL);
          }
        case 84: break;
        case 26: 
          { return symbol(sym.IGUAL);
          }
        case 85: break;
        case 27: 
          { return symbol(sym.OR);
          }
        case 86: break;
        case 28: 
          { return symbol(sym.DIFERENTE);
          }
        case 87: break;
        case 29: 
          { return symbol(sym.IF);
          }
        case 88: break;
        case 30: 
          { return symbol(sym.DO);
          }
        case 89: break;
        case 31: 
          { return symbol(sym.REAL, new Double(yytext()));
          }
        case 90: break;
        case 32: 
          { return symbol(sym.CARACTER, new Character(yytext().charAt(1)));
          }
        case 91: break;
        case 33: 
          { return symbol(sym.NOT);
          }
        case 92: break;
        case 34: 
          { return symbol(sym.INT);
          }
        case 93: break;
        case 35: 
          { return symbol(sym.END);
          }
        case 94: break;
        case 36: 
          { return symbol(sym.AND);
          }
        case 95: break;
        case 37: 
          { return symbol(sym.FOR);
          }
        case 96: break;
        case 38: 
          { return symbol(sym.TRUE);
          }
        case 97: break;
        case 39: 
          { return symbol(sym.ELSE);
          }
        case 98: break;
        case 40: 
          { return symbol (sym.CHAR);
          }
        case 99: break;
        case 41: 
          { return symbol(sym.CASE);
          }
        case 100: break;
        case 42: 
          { return symbol(sym.VOID);
          }
        case 101: break;
        case 43: 
          { return symbol(sym.OTHER);
          }
        case 102: break;
        case 44: 
          { return symbol(sym.BEGIN);
          }
        case 103: break;
        case 45: 
          { return symbol(sym.BREAK);
          }
        case 104: break;
        case 46: 
          { return symbol(sym.FALSE);
          }
        case 105: break;
        case 47: 
          { return symbol(sym.WHILE);
          }
        case 106: break;
        case 48: 
          { return symbol(sym.PRINT);
          }
        case 107: break;
        case 49: 
          { return symbol(sym.DOUBLE);
          }
        case 108: break;
        case 50: 
          { return symbol(sym.RETURN);
          }
        case 109: break;
        case 51: 
          { return symbol(sym.STRING);
          }
        case 110: break;
        case 52: 
          { return symbol(sym.SWITCH);
          }
        case 111: break;
        case 53: 
          { return symbol(sym.BOOLEAN);
          }
        case 112: break;
        case 54: 
          { return symbol(sym.READINT);
          }
        case 113: break;
        case 55: 
          { return symbol(sym.READCHAR);
          }
        case 114: break;
        case 56: 
          { return symbol(sym.FUNCTION);
          }
        case 115: break;
        case 57: 
          { return symbol(sym.MAINBEGIN);
          }
        case 116: break;
        case 58: 
          { return symbol(sym.READDOUBLE);
          }
        case 117: break;
        case 59: 
          { return symbol(sym.READSTRING);
          }
        case 118: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {   return symbol(sym.EOF);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
