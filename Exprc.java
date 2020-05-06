/**
 * (1) reads in a squence of expressions (1 expression per line) from a file called expr.dat
 * (2) determines for each legal expression its equivalent bytecodes
 * (3) outputs this compiled form, for each expression on line "lineno" of the file expr.dat, in the output file called 
 *     "lineno.jbc" in one bytecode instruction per line format
 */


/**
 *
 * @author Brandon Prenger
 * 
 * Input: Single arithmetic expression from file "exprEg.dat"
 */


// Context:  static double f(int i) {...}
// Output : Equivalent Java bytecodes in file "exprEg.bc"

//ASSOCIATIVITY IS RIGHT TO LEFT WITH * GIVEN PRECEDENCE OVER +
 
import java.io.*;

public class Exprc {
    static BufferedReader in;
    static StreamTokenizer inTok;

 public static void main(String[] args) throws IOException {
      in = new BufferedReader (new FileReader("expr.dat"));
      inTok = new StreamTokenizer(in);
      int lineno = 1;
      inTok.eolIsSignificant(true);
      inTok.nextToken();  // lookahead token
      // processes a sequence of expressions holding onto the translation of the last expression
      while (inTok.ttype != inTok.TT_EOF) {
         if (inTok.ttype != inTok.TT_EOL) {
               PrintWriter out = new PrintWriter (
                  new FileWriter("" + lineno + ".jbc" ), true );
             try {
               Node ae = expr();
               ae.code(out);
               out.flush();
             } catch (Exception e) {System.out.println(" \t\t Error =>  " + e);}
             finally 
             {
                 while (inTok.ttype != inTok.TT_EOL && inTok.ttype != inTok.TT_EOF)
                     inTok.nextToken();
             }
         }
         lineno++;
         inTok.nextToken();
      }
  }
 
  public static Node expr() throws Exception {
       // PRE: Expects lookahead token.
       // POST: Lookahead token available.
       Node temp = term();
       Node temp1;
       while (inTok.ttype == '+')
       {
            inTok.nextToken();
            temp1 = term();
            temp = new OpNode(temp, '+', temp1);
       }
       return temp;
  }
  
  public static Node term() throws Exception {
       // PRE: Expects lookahead token.
       // POST: Lookahead token available.
       Node temp = factor();
       Node temp1;
       while (inTok.ttype == '*')
       {
            inTok.nextToken();
            temp1 = factor();
            temp = new OpNode(temp, '*', temp1);
       }
       return temp;
  }
 
  public static Node factor() throws Exception {
       // PRE: Expects lookahead token.
       // POST: Reads in a lookahead token.
       Node temp;
       if ( inTok.ttype == inTok.TT_WORD) {
              char ch = inTok.sval.charAt(0);
              if ( (inTok.sval.length() == 1))
              {
                  switch(ch)
                  {
                      case 'i': temp = new VarIntNode(ch);
                          break;
                      case 'a': temp = new VarDoubleNode(ch);
                          break;
                      case 'j': temp = new VarIntNode(ch);
                          break;
                      default: throw new Exception("Illegal Variable Name");
                      
                  } 
                  inTok.nextToken();
              } else  throw new Exception("Illegal Variable Name");
       } else
       {
           if(inTok.ttype == '(')
           {
               inTok.nextToken();
               temp = expr();   //the case of having a new expression
               inTok.nextToken();
           }
           else
           {
               throw new Exception("Expression does not begin with parenthesis");
           }
           
       }
       return temp;
  }
 }


 interface Node {
    double value();
    String type();
    void code(PrintWriter out);
 }


// only allows an integer and does not have the correct value
 class VarNode implements Node {
    protected char id;
    public VarNode(char _id) {
	       id = _id;
    }
    public double value() {
		// TO BE CHANGED
        return 2;
    };
    public String type() {
		// TO BE CHANGED
         return "int";
    };
    public void code(PrintWriter out) {
		// TO BE CHANGED
         out.print("\tiload_0\n");
    };
 }

 class VarIntNode extends VarNode 
 {
    public VarIntNode(char _id) 
    {
	 super(_id);
    }
    public double value() {
		// TO BE CHANGED
        return 2;
    };
    public String type() {
         return "int";
    };
    public void code(PrintWriter out) {
         if(id == 'i')
         {
             out.print("\tiload_0\n");
         }
         else
         {
             out.print("\tiload_3\n");
         }
         out.print("\ti2d\n");
    };
 }

 class VarDoubleNode extends VarNode 
 {
    public VarDoubleNode(char _id) 
    {
	       super(_id);
    }
    public double value() 
    {
		// TO BE CHANGED
        return 2;
    }
    public String type() 
    {
         return "double";
    }
    public void code(PrintWriter out) 
    {
         out.print("\tdload_1\n");
    }
 }

// only performs integer addition
 class OpNode implements Node {
    protected Node LNode, RNode;
    protected char op;

    protected OpNode(Node _LNode, char _op, Node _RNode) {
	      LNode = _LNode;
              op = _op;
	      RNode = _RNode;
    }
    
    // TO BE CHANGED - IMPLEMENT INTERFACE
    public double value() 
    {
            if(op == '+')
            {
                  return LNode.value() + RNode.value();            
            }
            else
            {
                  return LNode.value() * RNode.value();            

            }
    }
	public String type() {
    // TO BE CHANGED
	     return "double";
	};
	public void code(PrintWriter out) {
    // TO BE CHANGED
         LNode.code(out);
	     RNode.code(out);
             if(op == '+')
             {
                 out.print("\tdadd\n");
             }
             else
             {
                 out.print("\tdmul\n");
             }
    };
 }
 class OpAddNode extends OpNode {

    public OpAddNode(Node _LNode, Node _RNode) {
	      super(_LNode, '+', _RNode);
    }
    
    // TO BE CHANGED - IMPLEMENT INTERFACE
    public double value() {
	// TO BE CHANGED (promotion?)
	      return LNode.value() + RNode.value();
	};
	public String type() {
	     return "double";
	};
	public void code(PrintWriter out) {
         LNode.code(out);
	     RNode.code(out);
	     out.print("\tdadd\n");
    };
 }

 class OpMulNode extends OpNode {

    public OpMulNode(Node _LNode, Node _RNode) {
	      super(_LNode, '*', _RNode);
    }
    
    // TO BE CHANGED - IMPLEMENT INTERFACE
    public double value() {
	// TO BE CHANGED (promotion?)
	      return LNode.value() * RNode.value();
	};
	public String type() {
	     return "double";
	};
	public void code(PrintWriter out) {
         LNode.code(out);
	     RNode.code(out);
	     out.print("\tdmul\n");
    };
 }
