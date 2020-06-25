package br.com.radixeng.motorBanco.Motor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
abstract public class Cliente 
{
   @Id
   protected String identificador;

   public Cliente(String identificador) {
      this.identificador = identificador;
   }

   public String getIdentificador() {
      return identificador;
   }
}