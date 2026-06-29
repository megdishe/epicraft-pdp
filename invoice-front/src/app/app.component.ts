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
  private apiBase = '';

  status = 'Ready';
  companies: any[] = [];
  customers: any[] = [];
  invoices: any[] = [];

  companyForm = { name: '', address: '', email: '', phone: '', taxId: '', bankName: '', iban: '', bic: '', accountHolder: '' };
  customerForm = { name: '', address: '', email: '', phone: '', taxId: '' };
  invoiceForm = { companyId: '', customerId: '', periodLabel: '', workedDays: 20, issueDate: '', paymentDelayDays: 30, templateName: 'invoice-template', lines: [this.createInvoiceLine()] };
  legacyInvoiceForm = { month: new Date().getMonth() + 1, year: new Date().getFullYear(), numberOfDays: 20 };

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

  createInvoiceLine() {
    return { description: '', quantity: 1, unitPrice: 610 };
  }

  addInvoiceLine(): void {
    this.invoiceForm.lines.push(this.createInvoiceLine());
  }

  removeInvoiceLine(index: number): void {
    if (this.invoiceForm.lines.length > 1) {
      this.invoiceForm.lines.splice(index, 1);
    }
  }

  createInvoice(): void {
    const body = {
      ...this.invoiceForm,
      lines: this.invoiceForm.lines
        .filter((line) => line.description.trim())
        .map((line) => ({
          description: line.description.trim(),
          quantity: Number(line.quantity),
          unitPrice: Number(line.unitPrice)
        }))
    };
    this.http.post(`${this.apiBase}/api/invoices`, body).subscribe({ next: () => this.loadAll(), error: (err) => this.status = `Invoice creation failed: ${err.message}` });
  }

  createLegacyInvoice(): void {
    const { month, year, numberOfDays } = this.legacyInvoiceForm;
    this.http.post(`${this.apiBase}/api/invoices/temporary/legacy?month=${month}&year=${year}&numberOfDays=${numberOfDays}`, {}).subscribe({
      next: () => {
        this.status = 'Legacy invoice created';
        this.loadAll();
      },
      error: (err) => this.status = `Legacy invoice creation failed: ${err.message}`
    });
  }

  getInvoiceDownloadLink(invoice: any): string {
    const id = invoice?.id;
    return id ? `${this.apiBase}/api/invoices/${id}/pdf` : '';
  }
}
