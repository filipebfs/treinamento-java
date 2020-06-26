package br.com.radixeng.motorBanco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.radixeng.motorBanco.Motor.Banco;
import br.com.radixeng.motorBanco.Motor.Cliente;
import br.com.radixeng.motorBanco.Motor.Conta;
import br.com.radixeng.motorBanco.Motor.IRepositorioCliente;
import br.com.radixeng.motorBanco.Motor.JpaRepositorioConta;
import br.com.radixeng.motorBanco.Motor.JpaRepositoriocliente;
import br.com.radixeng.motorBanco.Motor.Operacao;
import br.com.radixeng.motorBanco.Motor.exceptions.ContaInvalidaException;
import br.com.radixeng.motorBanco.Motor.exceptions.SaldoContaException;

@Path("/")
public class Servico 
{   
   public Servico() 
   {
      this.entityManager = Persistence.createEntityManagerFactory("motor-banco").createEntityManager();
      this.repositorioClientes = new JpaRepositoriocliente(this.entityManager);
      this.motorBanco = new Banco(new JpaRepositorioConta(this.entityManager));
   }

   private EntityManager entityManager;
   private Banco motorBanco;
   private IRepositorioCliente repositorioClientes;

   @Path("conta")
   @POST
   @Produces(MediaType.APPLICATION_JSON)
   public Response novaConta(NovaContaRequest request) 
   {
      Cliente cliente = repositorioClientes.get(request.nome);
      if (cliente == null) cliente = new Cliente(request.nome);
      
      this.entityManager.getTransaction().begin();

      repositorioClientes.put(cliente);

      try 
      {
         motorBanco.criarConta(cliente, request.tipoConta);
         this.entityManager.getTransaction().commit();
      } 
      catch (ContaInvalidaException e) 
      {
         e.printStackTrace();
         return Response.status(400).entity(e.getMessage()).build();
      }
      this.entityManager.close();

      return Response.accepted().build();
   }

   @POST
   @Path("operacao")
   @Produces(MediaType.APPLICATION_JSON)
   public Response operacao(OperacaoRequest request) 
   {
      try 
      {
         if(request.identificadorDestino == null && request.tipoContaDestino == null) 
         {
            Cliente cliente = repositorioClientes.get(request.identificadorOrigem);
            Operacao operacao;

            this.entityManager.getTransaction().begin();
         
            if(request.valor < 0) 
            {
               operacao = motorBanco.sacar(-request.valor, cliente, request.tipoContaOrigem);
            } 
            else 
            {
               operacao = motorBanco.depositar(request.valor, cliente, request.tipoContaOrigem);
            }

            this.entityManager.persist(operacao);               

            this.entityManager.getTransaction().commit();
         } 
         else if(request.valor >= 0 && request.identificadorOrigem != null && request.identificadorDestino != null && request.tipoContaOrigem != null && request.tipoContaDestino != null) 
         {
            this.entityManager.getTransaction().begin();
   
            List<Operacao> operacoes = motorBanco.transferir(request.valor, repositorioClientes.get(request.identificadorOrigem), request.tipoContaOrigem, repositorioClientes.get(request.identificadorDestino), request.tipoContaDestino);
            for (Operacao operacao : operacoes) 
            {
               this.entityManager.persist(operacao);               
            }

            this.entityManager.getTransaction().commit();
         } 
         else 
         {
            return Response.status(400).build();
         }
      } 
      catch (SaldoContaException se) 
      {
         return Response.status(Status.NOT_ACCEPTABLE).entity(se.getMessage()).build();
      }
      catch (ContaInvalidaException ce) 
      {
         return Response.status(Status.NOT_ACCEPTABLE).entity(ce.getMessage()).build();
      }

      this.entityManager.close();

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
   public Response obterConta(@QueryParam("identificador") String identificador, @QueryParam("tipoConta") String tipoConta, @QueryParam("intervalo") int intervalo) 
   {
      ContaResponse response = new ContaResponse();

      try 
      {
         List<Operacao> operacoesConta = motorBanco.consultaExtrato(repositorioClientes.get(identificador), tipoConta, intervalo);
      
         for (Operacao operacao : operacoesConta) {
            OperacaoResponse operacaoResponse = new OperacaoResponse();
            operacaoResponse.data = operacao.getData();
            operacaoResponse.valor = operacao.getValor();
            operacaoResponse.origem = operacao.getUsuarioOrigem() == null ? null : operacao.getUsuarioOrigem().getIdentificador();
            operacaoResponse.destino = operacao.getUsuarioDestino() == null ? null : operacao.getUsuarioDestino().getIdentificador();
            
            response.operacoes.add(operacaoResponse);
         }
      } 
      catch (ContaInvalidaException e) 
      {
         return Response.status(Status.NOT_FOUND).build();
      }

      return Response.ok(response).build();
   }

   @GET
   @Path("")
   @Produces(MediaType.TEXT_PLAIN)
   public String hello() 
   {
      return "Hello World";
   }
}

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
