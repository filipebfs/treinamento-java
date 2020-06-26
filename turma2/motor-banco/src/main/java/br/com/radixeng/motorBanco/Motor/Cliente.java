package br.com.radixeng.motorBanco.Motor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cliente 
{
   @Id
   protected String identificador;

   @Deprecated
   public Cliente() 
   {
   }

   public Cliente(String identificador) {
      this.identificador = identificador;
   }

   public String getIdentificador() {
      return identificador;
   }
}