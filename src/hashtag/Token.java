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
public class Token {

    public final int type;
    public final int start;
    public final int length;

    public Token(int type, int start, int length) {
        this.type = type;
        this.start = start;
        this.length = length;
    }
}
