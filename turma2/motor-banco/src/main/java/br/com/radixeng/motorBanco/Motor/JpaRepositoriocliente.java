package br.com.radixeng.motorBanco.Motor;

import javax.persistence.EntityManager;

public class JpaRepositoriocliente implements IRepositorioCliente
{
   public JpaRepositoriocliente(EntityManager entityManager) 
   {
      this.em = entityManager;
   }

   private EntityManager em;

   public void put(Cliente cliente) 
   {
      this.em.persist(cliente);
   }

   public Cliente get(String identificador) 
   {
      return this.em.find(Cliente.class, identificador);
   }
      
}