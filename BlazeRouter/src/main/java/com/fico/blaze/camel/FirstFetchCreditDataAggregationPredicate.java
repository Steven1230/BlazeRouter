package com.fico.blaze.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class FirstFetchCreditDataAggregationPredicate implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        return false;
    }
}
