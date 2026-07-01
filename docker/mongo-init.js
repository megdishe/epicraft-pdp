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

db.companies.updateOne(
  { _id: '1' },
  {
    $set: {
      name: 'EPICRAFT FRANCE',
      address: '55 RUE DE LA FRETTE\n78500 SARTROUVILLE',
      email: '',
      phone: '',
      siret: '98862388000016',
      taxId: 'FR40988623880',
      bankDetails: {
        bankName: '',
        iban: 'FR7640618805000004040056158',
        bic: 'BOUSFRPPXXX',
        accountHolder: ''
      }
    }
  },
  { upsert: true }
);

db.customers.insertOne({
  name: 'Banque de France',
  address: '31 Rue Croix des Petits Champs, 75001 Paris',
  email: 'finance@banque-france.fr',
  phone: '+33 1 42 92 42 92',
  taxId: '57210489100013'
});

db.customers.updateOne(
  { _id: '3' },
  {
    $set: {
      name: 'OCSI',
      address: '4 RUE DU COLONEL DRIANT\n75001 PARIS',
      email: '',
      phone: '',
      taxId: 'FR64381158575',
      siret: '38115857500070'
    }
  },
  { upsert: true }
);
