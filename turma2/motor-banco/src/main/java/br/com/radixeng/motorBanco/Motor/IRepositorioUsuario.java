package br.com.radixeng.motorBanco.Motor;

public interface IRepositorioUsuario 
{
   void put(Cliente cliente);
   Cliente get(String identificador);
}