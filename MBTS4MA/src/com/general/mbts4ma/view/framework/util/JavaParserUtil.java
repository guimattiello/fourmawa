package com.general.mbts4ma.view.framework.util;

import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import com.general.mbts4ma.view.MainView;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class JavaParserUtil {

	//public static String pageObjectAtual;
	
	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this 
             CompilationUnit, including inner class methods */          
        	//MainView.metodos.add(n.getName().toString());
        	String modifier[] = n.getDeclarationAsString(true, false).split(" ");
        	if (modifier[0].equals("public")){
        		MainView.metodos.add(n.getDeclarationAsString(false, false).toString());
        	}
        	//System.out.println(n.getDeclarationAsString(false, false));
        	NodeList<Parameter> no = n.getParameters();
        	/*if (no.size() > 0){
        		System.out.println("Parametro: " + no.get(0).getName().toString());
        		System.out.println("Tipo: " + no.get(0).getType().toString());
        	}*/
        	//MainView.metodosWeb.add(n.getName().toString());
            super.visit(n, arg);
        }
    }
	
	public static void getMethodNames(String pageObjectPath) {
		
		MainView.metodos = new ArrayList<String>();
		
		/*FileInputStream in = null;
		try {
			in = new FileInputStream(pageObjectPath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}*/

        // parse it
        CompilationUnit cu = JavaParser.parse(pageObjectPath);
       
        // visit and print the methods names
        new MethodVisitor().visit(cu, null);
      
	}
	
}
