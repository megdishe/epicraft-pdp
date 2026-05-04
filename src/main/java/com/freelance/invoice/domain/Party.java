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
    public static Party defaultCompany() {
        return new Party(
                "EPICRAFT FRANCE",
                "55 RUE DE LA FRETTE",
                "78500",
                "SARTROUVILLE",
                "France",
                "98862388000016",
                "FR40988623880",
                ""
        );
    }

    public static Party defaultClient() {
        return new Party(
                "OCSI",
                "4 RUE DU COLONEL DRIANT",
                "75001",
                "PARIS",
                "France",
                "38115857500070",
                "FR64381158575",
                ""
        );
    }

    public static Party withDefaults(Party provided, Party defaults) {
        if (provided == null) {
            return defaults;
        }

        return new Party(
                firstNonBlank(provided.name(), defaults.name()),
                firstNonBlank(provided.address(), defaults.address()),
                firstNonBlank(provided.postalCode(), defaults.postalCode()),
                firstNonBlank(provided.city(), defaults.city()),
                firstNonBlank(provided.country(), defaults.country()),
                firstNonBlank(provided.siret(), defaults.siret()),
                firstNonBlank(provided.vatNumber(), defaults.vatNumber()),
                firstNonBlank(provided.email(), defaults.email())
        );
    }

    private static String firstNonBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
