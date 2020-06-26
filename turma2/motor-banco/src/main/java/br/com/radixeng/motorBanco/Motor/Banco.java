package br.com.radixeng.motorBanco.Motor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.radixeng.motorBanco.Motor.exceptions.ContaInvalidaException;
import br.com.radixeng.motorBanco.Motor.exceptions.SaldoContaException;

public class Banco 
{
   public Banco(IRepositorioConta repositorioConta) 
   {
      this.repositorioContas = repositorioConta;
   }

   private IRepositorioConta repositorioContas;

   public void criarConta(Cliente usuario, String tipoConta) 
   throws ContaInvalidaException 
   {
      Conta conta = repositorioContas.get(usuario.getIdentificador(), tipoConta);
      if (conta != null) 
      {
         throw new ContaInvalidaException("Conta já existente para o cliente "+ usuario.getIdentificador());
      } 
      else 
      {
         Conta novaConta = Conta.criarConta(tipoConta, usuario);
         repositorioContas.put(novaConta);
      }
   }

   public Operacao sacar(double valor, Cliente usuario, String tipoConta) 
   throws SaldoContaException, ContaInvalidaException 
   {
      Conta conta = obterConta(usuario, tipoConta);
      return conta.sacar(valor, null);
   }

   public Operacao depositar(double valor, Cliente usuario, String tipoConta) 
   throws SaldoContaException, ContaInvalidaException 
   {
      Conta conta = obterConta(usuario, tipoConta);
      return conta.depositar(valor, null);
   }

   private Conta obterConta(Cliente usuario, String tipoConta) throws ContaInvalidaException 
   {
      Conta conta = repositorioContas.get(usuario.getIdentificador(), tipoConta);
      if (conta == null) throw new ContaInvalidaException();
      return conta;
   }

   public List<Operacao> transferir(double valor, Cliente usuarioOrigem, String tipoContaOrigem, Cliente usuarioDestino, String tipoContaDestino)
   throws SaldoContaException, ContaInvalidaException 
   {
      Conta contaOrigem = obterConta(usuarioOrigem, tipoContaOrigem);
      Conta contaDestino = obterConta(usuarioDestino, tipoContaDestino);
      List<Operacao> operacoes = new ArrayList<>();
      operacoes.add( contaOrigem.sacar(valor, usuarioDestino) );
      operacoes.add( contaDestino.depositar(valor, usuarioOrigem) );
      return operacoes;
   }

   public double saldo(Cliente usuario, String tipoConta)
   {
      return repositorioContas.get(usuario.getIdentificador(), tipoConta).getSaldo();
   }

   public List<Operacao> consultaExtrato(Cliente usuario, String tipoConta, int intervalo)
         throws ContaInvalidaException 
   {
      List<Operacao> extrato = new ArrayList<>();

      Conta conta = obterConta(usuario, tipoConta);
      
      Date dateAgora = DataBanco.agora();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateAgora);
      calendar.add(Calendar.DAY_OF_MONTH, -intervalo);
      Date dateFiltro = calendar.getTime();
      
      for (Operacao operacao : conta.getTransacoes()) {
         if(operacao.getData().compareTo(dateFiltro) >= 0) {
               extrato.add(operacao);
         }
      }

      return extrato;
   }
}