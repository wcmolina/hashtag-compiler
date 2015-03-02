/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtag;

/**
 *
 * @author Wilmer Carranza
 */
public class LexicalErrorException extends Exception {

    public LexicalErrorException() {
        super();
    }
    
    public LexicalErrorException(String message) {
        super(message);
    }
    
    public LexicalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LexicalErrorException(Throwable cause) {
        super(cause);
    }
}
