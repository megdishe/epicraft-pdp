package com.freelance.invoice.domain;

public record Party(
        String name,
        String address,
        String postalCode,
        String city,
        String country,
        String siret,
        String vatNumber,
        String email
) {
}
