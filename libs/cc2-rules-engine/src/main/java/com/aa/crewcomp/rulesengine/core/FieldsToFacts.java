package com.aa.crewcomp.rulesengine.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.jeasy.rules.api.Facts;

public class FieldsToFacts<EntityClassType> {
    public void fieldsToFacts(Facts facts, EntityClassType record) {
        try {
            for (Method method : record.getClass().getMethods())
                if (method.getName().startsWith("get") && Objects.nonNull(method.invoke(record)))
                    facts.put(method.getName().toLowerCase().charAt(3) + method.getName().substring(4), 
                        method.invoke(record));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}