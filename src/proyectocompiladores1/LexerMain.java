/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectocompiladores1;

import java.io.File;

/**
 *
 * @author stephanie
 */
public class LexerMain {

    /**
     * @param args the command line arguments
     * @return 
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
       jflex.Main.generate(new File("src\\ProyectoCompiladores1\\Lexer.flex"));
    }
    
}
