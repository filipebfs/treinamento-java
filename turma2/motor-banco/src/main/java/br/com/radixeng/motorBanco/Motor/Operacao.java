package br.com.radixeng.motorBanco.Motor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Operacao {
    private double valor;
    private Date data;
    @ManyToMany
    private Cliente usuarioOrigem;
    @ManyToMany
    private Cliente usuarioDestino;

    public Date getData() {
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

    Operacao(double valor, Date data, Cliente usuarioOrigem, Cliente usuarioDestino) {
        this.valor = valor;
        this.data = data;
        this.usuarioDestino = usuarioDestino;
        this.usuarioOrigem = usuarioOrigem;
    }
}