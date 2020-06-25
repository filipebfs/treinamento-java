package br.com.radixeng.motorBanco.Motor;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.radixeng.motorBanco.Motor.exceptions.SaldoContaException;

@Entity
@DiscriminatorValue(TipoConta.ContaInvestimentoValorTipo)
public class ContaInvestimento extends Conta 
{
   public ContaInvestimento() {
      super();
      this.tipoConta = Integer.parseInt(TipoConta.ContaInvestimentoValorTipo);
   }

   @Override
   public void sacar(double valor, Cliente usuarioDestino) 
   throws SaldoContaException 
   {
      double saldoDisponivel = 0;
      
      Date dateAgora = DataBanco.agora();

      // Instant instant = dateAgora.toInstant();
      // Date date30DiasAtras = Date.from(instant.minus(30, ChronoUnit.DAYS));

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateAgora);
      calendar.add(Calendar.DAY_OF_MONTH, -30);
      Date date30DiasAtras = calendar.getTime();

      for (Operacao operacao : this.operacoes) {
         if (operacao.getData().compareTo(date30DiasAtras) <= 0) {
               saldoDisponivel += operacao.getValor();
         }
      }

      if (valor > saldoDisponivel) 
         throw new SaldoContaException("Conta investimento sem saldo dentro dos requisitos.");
         
      super.sacar(valor, usuarioDestino);
   }
}