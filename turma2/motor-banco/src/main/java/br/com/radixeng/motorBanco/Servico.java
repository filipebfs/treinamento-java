package br.com.radixeng.motorBanco;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.radixeng.motorBanco.Motor.Banco;
import br.com.radixeng.motorBanco.Motor.Cliente;
import br.com.radixeng.motorBanco.Motor.Conta;
import br.com.radixeng.motorBanco.Motor.IRepositorioUsuario;
import br.com.radixeng.motorBanco.Motor.Operacao;
import br.com.radixeng.motorBanco.Motor.exceptions.ContaInvalidaException;
import br.com.radixeng.motorBanco.Motor.exceptions.SaldoContaException;

class NovaContaRequest {
   public String nome;
   public String tipoConta;
}

class OperacaoRequest {
   public double valor;
   public String identificadorDestino;
   public String identificadorOrigem;
   public String tipoContaOrigem;
   public String tipoContaDestino;
}

class TipoContaResponse {
   public List<String> tipos = new ArrayList<>();
}

class ContaResponse {
   public List<OperacaoResponse> operacoes = new ArrayList<>();
}

class OperacaoResponse {
   public double valor;
   public Date data;
   public String origem;
   public String destino;
}

@Path("/")
public class Servico 
{
   public Servico() 
   {
      this.motorBanco = Banco.getInstancia();  //falta resolver injecao de dependencia
   }

   private Banco motorBanco;

   private IRepositorioUsuario repositorioUsuarios; //falta resolver injecao de dependencia

   @Path("conta")
   @POST
   @Produces(MediaType.APPLICATION_JSON)
   public Response novaConta(NovaContaRequest request) 
   {
      Cliente cliente = new Cliente(request.nome) {
      };

      repositorioUsuarios.put(cliente);

      try 
      {
         motorBanco.criarConta(cliente, request.tipoConta);
      } 
      catch (ContaInvalidaException e) 
      {
         e.printStackTrace();
         return Response.status(400).entity(e.getMessage()).build();
      }

      return Response.accepted().build();
   }

   @POST
   @Path("operacao")
   @Produces(MediaType.APPLICATION_JSON)
   public Response operacao(OperacaoRequest request) throws SaldoContaException 
   {
      
      if(request.identificadorDestino == null && request.tipoContaDestino == null) 
      {         
         if(request.valor < 0) {
               motorBanco.sacar(-request.valor, repositorioUsuarios.get(request.identificadorOrigem), request.tipoContaOrigem);
         } else {
               motorBanco.depositar(request.valor, repositorioUsuarios.get(request.identificadorOrigem), request.tipoContaOrigem);
         }
      } 
      else if(request.valor >= 0 && request.identificadorOrigem != null && request.identificadorDestino != null && request.tipoContaOrigem != null && request.tipoContaDestino != null) 
      {
         motorBanco.transferir(request.valor, repositorioUsuarios.get(request.identificadorOrigem), request.tipoContaOrigem, repositorioUsuarios.get(request.identificadorDestino), request.tipoContaDestino);
      } 
      else 
      {
         return Response.status(400).build();
      }

      return Response.accepted().build();
   }

   @GET
   @Path("/tipo-conta")
   @Produces(MediaType.APPLICATION_JSON)
   public TipoContaResponse tiposDeConta() 
   {
      TipoContaResponse response = new TipoContaResponse();
      response.tipos = Conta.tiposDeConta();

      return response;
   }

   @GET
   @Path("/conta")
   @Produces(MediaType.APPLICATION_JSON)
   public ContaResponse obterConta(@QueryParam("identificador") String identificador, @QueryParam("tipoConta") String tipoConta, @QueryParam("intervalo") int intervalo) 
   {
      ContaResponse response = new ContaResponse();

      List<Operacao> operacoesConta = motorBanco.consultaExtrato(repositorioUsuarios.get(identificador), tipoConta, intervalo);
      
      for (Operacao operacao : operacoesConta) {
         OperacaoResponse operacaoResponse = new OperacaoResponse();
         operacaoResponse.data = operacao.getData();
         operacaoResponse.valor = operacao.getValor();
         operacaoResponse.origem = operacao.getUsuarioOrigem().getIdentificador();
         operacaoResponse.destino = operacao.getUsuarioDestino().getIdentificador();
         
         response.operacoes.add(operacaoResponse);
      }

      return response;
   }

   @GET
   @Path("")
   @Produces(MediaType.TEXT_PLAIN)
   public String hello() 
   {
      return "Hello World";
   }
}