/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Brandon
 */
public class Exprv {
    static double a;
    static int i, j;
    public static void main(String[] args)
    {
        BufferedReader file;
        if(args.length != 4)
        {
            throw new IllegalArgumentException("There must be 3 arguments!");
        }
        try{
            i = Integer.parseInt(args[1]);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        try{
             a = Double.parseDouble(args[2]);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        try{
            j = Integer.parseInt(args[3]);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        Stack<VarNode> valueStack = new Stack<VarNode>();
        try
        {
            file = new BufferedReader(new FileReader(args[0]));
            String line;
            while((line = file.readLine()) != null)
            {
                if(line.compareTo("\ti2d") == 0)
                {
                    VarNode v = valueStack.pop();
                    valueStack.push(new DoubleVarNode(v.value()));
                }
                else if(line.compareTo("\tdmul") == 0)
                {
                    VarNode vLeft = valueStack.pop();
                    VarNode vRight = valueStack.pop();
                    valueStack.push(new DoubleVarNode(vLeft.value() * vRight.value()));
                }
                else if(line.compareTo("\tdadd") == 0)
                {
                    VarNode vLeft = valueStack.pop();
                    VarNode vRight = valueStack.pop();
                    valueStack.push(new DoubleVarNode(vLeft.value() + vRight.value()));
                }
                
                else 
                {
                    String ss = line.substring(0, 7);
                    if(ss.compareTo("\tiload_") == 0)
                    {
                        valueStack.push(new IntVarNode(line.charAt(7) == '0'?i:j));
                    }
                    else if(ss.compareTo("\tdload_") == 0)
                    {
                        valueStack.push(new DoubleVarNode(line.charAt(7) == '1'?a:0));
                    }
                }
            }
            System.out.println(valueStack.pop().value());
        }
        catch(IOException f)
        {
            System.out.println(f.getMessage());
        }

    }
}

 abstract class VarNode {
    public abstract double value();
    public abstract String type();
 }

    
class IntVarNode extends VarNode {
    protected int _value;
    public IntVarNode(int v) {
               _value = v;
    }
    public double value() {
        return _value;
    }
    public String type() {
         return "int";
    }
}

class DoubleVarNode extends VarNode {
    protected double _value;
    public DoubleVarNode(double v) {
               _value = v;
    }
    public double value() {
        return _value;
    }
    public String type() {
         return "double";
    }
}

