package br.com.radixeng.motorBanco.Motor;

import java.util.Calendar;
import java.util.Date;

public class ContaInvestimento extends Conta {
    //Adicionar regra para que saques na conta de investimento só possam ser realizados 30 dias após o depósito (saque aniversário)
    @Override
    public void sacar(double valor) {
        double saldoDisponivel = 0;
        
        Date dateAgora = DataBanco.agora();

        // Instant instant = dateAgora.toInstant();
        // Date date30DiasAtras = Date.from(instant.minus(30, ChronoUnit.DAYS));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateAgora);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date date30DiasAtras = calendar.getTime();

        for (Transacao transacao : this.transacoes) {
            if (transacao.getData().compareTo(date30DiasAtras) <= 0) {
                saldoDisponivel += transacao.getValor();
            }
        }

        if (valor <= saldoDisponivel) {
            super.sacar(valor);
        }
    }
}