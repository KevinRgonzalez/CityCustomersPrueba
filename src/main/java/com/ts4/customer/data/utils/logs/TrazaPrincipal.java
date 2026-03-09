package com.ts4.customer.data.utils.logs;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Component
public class TrazaPrincipal {
    public void trazaPrincipal(Map<String, Object> params, ResponseEntity<?> res) {
        // Dynatrace interceptará los headers automáticamente para el TraceID.
    }
}
