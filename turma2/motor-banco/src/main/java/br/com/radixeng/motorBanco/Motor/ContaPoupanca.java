package br.com.radixeng.motorBanco.Motor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TipoConta.ContaPoupancaValorTipo)
public class ContaPoupanca extends Conta 
{
   public ContaPoupanca() 
   {
      super();
      this.tipoConta = TipoConta.ContaPoupancaValorTipo;
   }    
}