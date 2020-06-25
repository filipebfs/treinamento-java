package br.com.radixeng.motorBanco.Motor;

public interface IRepositorioConta 
{   
   void put(Conta conta);
   Conta get(String cliente, String tipoConta);
}