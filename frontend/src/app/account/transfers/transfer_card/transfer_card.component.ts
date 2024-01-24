import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DirectedTransferDTO } from 'src/app/mainsv/data/directed_transfer_dto';
import { Option, Some, None } from 'src/app/option/option.module';

@Component({
    selector: 'transfer-card',
    templateUrl: './transfer_card.component.html',
    styleUrls: ['./transfer_card.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
    ]
})
export class TransferCardComponent {
    protected readonly SUFFIX_LENGTH = 3;
    @Input() transfer: DirectedTransferDTO | undefined;

    public senderShown = false;
    public receiverShown = false;

    public toggleSenderAccountNumberVisibility(): void {
        this.senderShown = !this.senderShown;
    }

    public toggleReceiverAccountNumberVisibility(): void {
        this.receiverShown = !this.receiverShown;
    }

    updateDisplayAccountNumber(accountNumber: string, show: boolean): string {
        return show
            ? accountNumber
            : '*'.repeat(accountNumber.length - this.SUFFIX_LENGTH) + accountNumber.slice(-this.SUFFIX_LENGTH);
    }

}