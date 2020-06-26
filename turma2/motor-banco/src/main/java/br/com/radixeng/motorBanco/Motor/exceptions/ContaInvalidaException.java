package br.com.radixeng.motorBanco.Motor.exceptions;

public class ContaInvalidaException extends Exception 
{
   public ContaInvalidaException() {
      super("Conta inválida");
   }

   public ContaInvalidaException(String message) {
      super(message);
   }
   
}