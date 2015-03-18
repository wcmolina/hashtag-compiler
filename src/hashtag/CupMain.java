/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtag;

/**
 *
 * @author Wilmer Carranza
 *
 * Clase solo de prueba, esto pasaria a la interfaz.
 */
public class CupMain {

    public static void main(String[] args) {
        String opts[] = new String[5];
        opts[0] = "-destdir";
        opts[1] = "src\\hashtag\\";
        opts[2] = "-parser";
        opts[3] = "Parser";
        opts[4] = "src\\hashtag\\Parser.cup";
        //opts[4] = "src\\hashtag\\Test.cup";
        //opts[4] = "src\\hashtag\\Errors.cup";

        try {
            java_cup.Main.main(opts);
        } catch (Exception e) {
        }
    }
}
