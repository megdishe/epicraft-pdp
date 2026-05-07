db = db.getSiblingDB('invoice_api');

db.companies.insertOne({
  name: 'Freelance IT Consulting',
  address: '10 rue de Paris, 75001 Paris',
  email: 'contact@freelance-it.fr',
  phone: '+33 6 00 00 00 00',
  taxId: '12345678900011',
  bankDetails: {
    bankName: 'Boursorama',
    iban: 'FR7640618805000004040056158',
    bic: 'BOUSFRPPXXX',
    accountHolder: 'Freelance IT Consulting'
  }
});

db.customers.insertOne({
  name: 'Banque de France',
  address: '31 Rue Croix des Petits Champs, 75001 Paris',
  email: 'finance@banque-france.fr',
  phone: '+33 1 42 92 42 92',
  taxId: '57210489100013'
});
