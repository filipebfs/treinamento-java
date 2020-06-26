package br.com.radixeng.motorBanco.Motor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class JpaRepositorioConta implements IRepositorioConta
{
   public JpaRepositorioConta(EntityManager entityManager) 
   {
      this.em = entityManager;
   }

   private EntityManager em;

   public void put(Conta conta) 
   {
      this.em.persist(conta);
   }

   public Conta get(String cliente, String tipoConta) 
   {
      Cliente c = new Cliente(cliente) { };
      Query q = this.em.createQuery("select distinct c from Conta c left join fetch c.operacoes o where c.cliente = :cliente and c.tipoConta = :tipo", Conta.class);
      q.setParameter("cliente", c);
      q.setParameter("tipo", tipoConta);
      Conta conta = null;
      try 
      {
         conta = (Conta)q.getSingleResult();          
      } 
      catch (NoResultException e) 
      {
      }

      return conta;
   }
   
}