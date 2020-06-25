package br.com.radixeng.motorBanco.Motor.exceptions;

public class ContaInvalidaException extends Exception 
{
   public ContaInvalidaException() {
      super();
   }

   public ContaInvalidaException(String message) {
      super(message);
   }
   
}