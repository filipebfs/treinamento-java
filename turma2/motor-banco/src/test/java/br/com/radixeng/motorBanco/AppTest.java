package br.com.radixeng.motorBanco;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import br.com.radixeng.motorBanco.Motor.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
   public AppTest() 
   {      
      this.b = new Banco(
         new IRepositorioConta(){
            private HashMap<String, Conta> rep = new HashMap<>();
         
            @Override
            public void put(Conta conta) 
            {
               rep.put(conta.getCliente().getIdentificador()+conta.getTipoconta(), conta);
            }
         
            @Override
            public Conta get(String cliente, String tipoConta) 
            {
               return rep.get(cliente + tipoConta);
            }
         }
      );
   }

   private Banco b;

   /**
    * Ao criar uma conta o saldo deve iniciar com o valor 0
    */
   @Test
   public void saldoDeContasDeveIniciarComZero() throws Exception 
   {
      Cliente u = new Cliente("Julio Cesar") { };
      Cliente u2 = new Usuario("Teste");

      b.criarConta(u, TipoConta.ContaInvestimentoValorTipo);
      b.criarConta(u, TipoConta.ContaCorrenteValorTipo);
      b.criarConta(u, TipoConta.ContaPoupancaValorTipo);
      
      b.criarConta(u2, TipoConta.ContaPoupancaValorTipo);

      assertEquals(0.0, b.saldo(u, TipoConta.ContaInvestimentoValorTipo), 0.0); 
      assertEquals(0.0, b.saldo(u, TipoConta.ContaCorrenteValorTipo), 0.0); 
      assertEquals(0.0, b.saldo(u, TipoConta.ContaPoupancaValorTipo), 0.0); 
      assertEquals(0.0, b.saldo(u2, TipoConta.ContaPoupancaValorTipo), 0.0); 
   }

   // Exercício 18/06/2020
   // Dar Continuidade aos testes do motor
}
