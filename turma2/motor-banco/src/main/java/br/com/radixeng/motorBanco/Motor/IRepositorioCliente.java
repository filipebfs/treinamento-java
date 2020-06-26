package br.com.radixeng.motorBanco.Motor;

public interface IRepositorioCliente 
{
   void put(Cliente cliente);
   Cliente get(String identificador);
}