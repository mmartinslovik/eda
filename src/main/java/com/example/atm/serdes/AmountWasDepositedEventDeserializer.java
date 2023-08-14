package com.example.atm.serdes;

import com.example.atm.event.AmountWasDepositedEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class AmountWasDepositedEventDeserializer implements Deserializer<AmountWasDepositedEvent> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public AmountWasDepositedEvent deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        Long bankAccountId = buffer.getLong();
        Long amount = buffer.getLong();

        AmountWasDepositedEvent event = new AmountWasDepositedEvent();
        event.setBankAccountId(bankAccountId);
        event.setAmount(amount);

        return event;
    }

    @Override
    public void close() {
    }
}