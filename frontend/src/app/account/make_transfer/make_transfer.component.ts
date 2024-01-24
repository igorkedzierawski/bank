import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { Option, Some } from 'src/app/option/option.module';

@Component({
    selector: 'app-make-transfer',
    templateUrl: './make_transfer.component.html',
    styleUrls: ['./make_transfer.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
    ],
})
export class MakeTransferComponent {
    protected transferForm: FormGroup;
    protected errorMessage: Option<string>;
    protected transferSuccess: boolean = false;

    constructor(
        private readonly reqeng: RequestEngine,
        private readonly router: Router,
        private readonly fb: FormBuilder,
    ) {
        this.transferForm = this.fb.group({
            senderAccountNumber: ['', [Validators.required]],
            receiverAccountNumber: ['', [Validators.required]],
            transferAmount: ['', [Validators.required, Validators.pattern(/^\d+\.\d{2}$/)]],
            title: ['', [Validators.required]],
        });
    }

    protected makeTransfer() {
        if (this.transferForm.valid) {
            const senderAccountNumber = this.transferForm.value.senderAccountNumber;
            const receiverAccountNumber = this.transferForm.value.receiverAccountNumber;
            const transferAmount = parseInt(this.transferForm.value.transferAmount.replace(/\./g, ''));
            const title = this.transferForm.value.title;

            this.reqeng
                .makeTransfer(senderAccountNumber, receiverAccountNumber, transferAmount, title)
                .subscribe(
                    () => {
                        console.log('Transfer successful');
                        this.transferSuccess = true;
                        // Additional logic or navigation after successful transfer
                    },
                    (error: ErrorDTO) => {
                        this.errorMessage = Some(error.message);
                    }
                );
        }
    }

    protected redirectToAccount() {
        this.router.navigate(['/account']);
    }

}