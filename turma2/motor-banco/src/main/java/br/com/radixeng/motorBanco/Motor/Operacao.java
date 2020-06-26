package br.com.radixeng.motorBanco.Motor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Operacao {

   @Id
   @GeneratedValue
   private Long id;
   private double valor;
   private Date data;
   @ManyToOne
   private Cliente usuarioOrigem;
   @ManyToOne
   private Cliente usuarioDestino;

   @Deprecated
   public Operacao() 
   {
   }

   Operacao(double valor, Date data, Cliente usuarioOrigem, Cliente usuarioDestino) {
   this.valor = valor;
   this.data = data;
   this.usuarioDestino = usuarioDestino;
   this.usuarioOrigem = usuarioOrigem;
  }

  public Date getData() 
  {
      return data;
   }

   public Cliente getUsuarioDestino() {
      return usuarioDestino;
   }

   public void setUsuarioDestino(Cliente usuarioDestino) {
      this.usuarioDestino = usuarioDestino;
   }

   public Cliente getUsuarioOrigem() {
      return usuarioOrigem;
   }

   public void setUsuarioOrigem(Cliente usuarioOrigem) {
      this.usuarioOrigem = usuarioOrigem;
   }

   public double getValor() {
      return valor;
   }
}