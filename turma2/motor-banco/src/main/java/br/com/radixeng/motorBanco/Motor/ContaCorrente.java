package br.com.radixeng.motorBanco.Motor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TipoConta.ContaCorrenteValorTipo)
public class ContaCorrente extends Conta 
{
   public ContaCorrente() {
      super();
      this.tipoConta = Integer.parseInt(TipoConta.ContaCorrenteValorTipo);
   }    
}