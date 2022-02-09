package ie.nuig.i3market.semantic.engine.exceptions;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class BindingException extends Exception{
    public BindingException(String exception){
        super(exception);
    }

    public BindingException(String message, String s) {
        super(message +" "+ s );
    }
}
