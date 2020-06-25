package br.com.radixeng.motorBanco.Motor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.radixeng.motorBanco.Motor.exceptions.ContaInvalidaException;
import br.com.radixeng.motorBanco.Motor.exceptions.SaldoContaException;

public class Banco 
{
   private IRepositorioConta repositorioContas;

   private static Banco instancia;

   private Banco() {
   }

   public static Banco getInstancia(IRepositorioConta repositorioConta) 
   {
      if (instancia == null) {
         instancia = new Banco();
      }

      instancia.repositorioContas = repositorioConta;

      return instancia;
   }

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

   public void sacar(double valor, Cliente usuario, String tipoConta) 
   throws SaldoContaException 
   {
      Conta conta = obterConta(usuario, tipoConta);
      conta.sacar(valor, null);
   }

   public void depositar(double valor, Cliente usuario, String tipoConta) 
   throws SaldoContaException 
   {
      Conta conta = obterConta(usuario, tipoConta);
      conta.depositar(valor, null);
   }

   private Conta obterConta(Cliente usuario, String tipoConta) 
   {
      Conta conta = repositorioContas.get(usuario.getIdentificador(), tipoConta);
      return conta;
   }

   public void transferir(double valor, Cliente usuarioOrigem, String tipoContaOrigem, Cliente usuarioDestino, String tipoContaDestino)
   throws SaldoContaException 
   {
      Conta contaOrigem = obterConta(usuarioOrigem, tipoContaOrigem);
      Conta contaDestino = obterConta(usuarioDestino, tipoContaDestino);
      contaOrigem.sacar(valor, usuarioDestino);
      contaDestino.depositar(valor, usuarioOrigem);
   }

   public double saldo(Cliente usuario, String tipoConta)
   {
      return repositorioContas.get(usuario.getIdentificador(), tipoConta).getSaldo();
   }

   public List<Operacao> consultaExtrato(Cliente usuario, String tipoConta, int intervalo) 
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