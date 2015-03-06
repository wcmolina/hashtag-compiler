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
public class SyntaxErrorException extends Exception {

    public SyntaxErrorException() {
        super();
    }

    public SyntaxErrorException(String message) {
        super(message);
    }

    public SyntaxErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxErrorException(Throwable cause) {
        super(cause);
    }
}
