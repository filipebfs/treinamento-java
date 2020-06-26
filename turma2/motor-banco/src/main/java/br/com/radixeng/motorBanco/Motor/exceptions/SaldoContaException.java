package br.com.radixeng.motorBanco.Motor.exceptions;

public class SaldoContaException extends Exception
{
   public SaldoContaException() {
      super("Saldo insuficiente");
   }

   public SaldoContaException(String message) {
      super(message);
   }
   
}