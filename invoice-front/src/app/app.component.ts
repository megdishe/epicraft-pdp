import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  private http = inject(HttpClient);
  private apiBase = 'http://localhost:8080';

  status = 'Ready';
  companies: any[] = [];
  customers: any[] = [];
  invoices: any[] = [];

  companyForm = { name: '', address: '', email: '', phone: '', taxId: '', bankName: '', iban: '', bic: '', accountHolder: '' };
  customerForm = { name: '', address: '', email: '', phone: '', taxId: '' };
  invoiceForm = { companyId: '', customerId: '', periodLabel: '', workedDays: 20, issueDate: '', paymentDelayDays: 30, templateName: 'invoice-template' };

  ngOnInit(): void { this.loadAll(); }

  loadAll(): void {
    this.status = 'Loading data...';
    Promise.all([
      this.http.get<any[]>(`${this.apiBase}/api/companies`).toPromise(),
      this.http.get<any[]>(`${this.apiBase}/api/customers`).toPromise(),
      this.http.get<any[]>(`${this.apiBase}/api/invoices`).toPromise()
    ]).then(([companies, customers, invoices]) => {
      this.companies = companies || [];
      this.customers = customers || [];
      this.invoices = invoices || [];
      this.status = 'Data loaded';
    }).catch((err) => this.status = `Error loading data: ${err?.message || err}`);
  }

  createCompany(): void {
    const body = {
      name: this.companyForm.name, address: this.companyForm.address, email: this.companyForm.email,
      phone: this.companyForm.phone, taxId: this.companyForm.taxId,
      bankDetails: { bankName: this.companyForm.bankName, iban: this.companyForm.iban, bic: this.companyForm.bic, accountHolder: this.companyForm.accountHolder }
    };
    this.http.post(`${this.apiBase}/api/companies`, body).subscribe({ next: () => this.loadAll(), error: (err) => this.status = `Company creation failed: ${err.message}` });
  }

  createCustomer(): void {
    this.http.post(`${this.apiBase}/api/customers`, this.customerForm).subscribe({ next: () => this.loadAll(), error: (err) => this.status = `Customer creation failed: ${err.message}` });
  }

  createInvoice(): void {
    this.http.post(`${this.apiBase}/api/invoices`, this.invoiceForm).subscribe({ next: () => this.loadAll(), error: (err) => this.status = `Invoice creation failed: ${err.message}` });
  }
}
