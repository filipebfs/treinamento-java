package br.com.radixeng.motorBanco.Motor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.DiscriminatorType;

import br.com.radixeng.motorBanco.Motor.exceptions.ContaInvalidaException;
import br.com.radixeng.motorBanco.Motor.exceptions.SaldoContaException;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipoConta",  discriminatorType = DiscriminatorType.INTEGER)
abstract public class Conta 
{
   @Id   
   @GeneratedValue
   protected Long id;
   @ManyToOne
   protected Cliente cliente;
   @Column(insertable = false, updatable = false)
   protected String tipoConta;
   @OneToMany
   protected List<Operacao> operacoes = new ArrayList<>();

   public Cliente getCliente()
   {
      return this.cliente;
   }

   public String getTipoconta()
   {
      return this.tipoConta;
   }

   public double getSaldo() {
      double saldo = 0.0;

      for (Operacao transacao : operacoes) {
         saldo += transacao.getValor();
      }

      return saldo;
   }

   public List<Operacao> getTransacoes() {
      return operacoes;
   }

   private Operacao operacao(double valorOperacao, Cliente usuarioOrigem, Cliente usuarioDestino) 
   throws SaldoContaException 
   {
      if (valorOperacao == 0) return null;

      if (valorOperacao < 0)
      {
         double saldo = this.getSaldo();

         if ((saldo - valorOperacao) < 0) throw new SaldoContaException();
      }

      Operacao novaOperacao = new Operacao(valorOperacao, DataBanco.agora(), usuarioOrigem, usuarioDestino);
      this.operacoes.add(novaOperacao);
      return novaOperacao;
   }

   public Operacao sacar(double valor, Cliente usuarioDestino) 
   throws SaldoContaException 
   {
      return this.operacao(-valor, this.getCliente(), usuarioDestino);
   }

   public Operacao depositar(double valor, Cliente usuarioOrigem) 
   throws SaldoContaException 
   {
      return this.operacao(valor, usuarioOrigem, this.getCliente());
   }

   public static Conta criarConta(String tipoConta, Cliente cliente) throws ContaInvalidaException 
   {
      Conta conta;
      if (tipoConta.equals(TipoConta.ContaCorrenteValorTipo)) {
         conta = new ContaCorrente();
      } else if (tipoConta.equals(TipoConta.ContaPoupancaValorTipo)) {
         conta = new ContaPoupanca();
      } else if (tipoConta.equals(TipoConta.ContaInvestimentoValorTipo)) {
         conta = new ContaInvestimento();
      } else {
         throw new ContaInvalidaException("Tipo de conta não permitido pelo sistema.");
      }

      conta.cliente = cliente;
      return conta;
   }

   public static List<String> tiposDeConta() {
      List<String> tiposDeConta = new ArrayList<>();
      tiposDeConta.add(TipoConta.ContaCorrenteValorTipo);
      tiposDeConta.add(TipoConta.ContaPoupancaValorTipo);
      tiposDeConta.add(TipoConta.ContaInvestimentoValorTipo);

      return tiposDeConta;
   }
}

