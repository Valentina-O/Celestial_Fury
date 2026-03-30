package org.valeneisa.exceptions;

public class NetworkException extends Exception{
    public NetworkException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
