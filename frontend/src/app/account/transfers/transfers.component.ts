import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { DirectedTransferDTO } from 'src/app/mainsv/data/directed_transfer_dto';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';
import { Option, Some, None } from 'src/app/option/option.module';
import { TransferCardComponent } from './transfer_card/transfer_card.component';

@Component({
    selector: 'transfers',
    templateUrl: './transfers.component.html',
    styleUrls: ['./transfers.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        TransferCardComponent,
    ]
})
export class TransfersComponent implements OnInit {

    protected transfers: DirectedTransferDTO[] = [];
    protected currentPage = 1;
    protected totalPages = 1;
    protected errorMessage: Option<string>;

    protected readonly SUFFIX_LENGTH = 3;

    protected showAllCharacters: { [key: string]: { sender: boolean; receiver: boolean } } = {};

    constructor(
        protected readonly reqeng: RequestEngine,
    ) { }

    ngOnInit(): void {
        this.loadTransfers();
    }

    loadTransfers() {
        this.reqeng.getTransfers(this.currentPage - 1, 8).subscribe(transferPage => {
            if (transferPage) {
                this.transfers = transferPage.content;
                this.totalPages = transferPage.totalPages;
            }
            this.errorMessage = None;
        }, (error: ErrorDTO) => {
            this.transfers = [];
            this.currentPage = 1;
            this.totalPages = 1;
            this.errorMessage = Some(error.message);
        });
    }

    onRefreshClick() {
        this.loadTransfers();
    }

    onPageChange(newPage: number): void {
        this.currentPage = newPage;
        this.loadTransfers();
    }

    toggleAccountNumberVisibility(party: string, transferId: number) {
        if (!this.showAllCharacters[transferId]) {
            this.showAllCharacters[transferId] = { sender: true, receiver: true };
        }
        this.showAllCharacters[transferId][party === "receiver" ? "receiver" : "sender"] = !this.showAllCharacters[transferId][party === "receiver" ? "receiver" : "sender"];
    }

    updateDisplayAccountNumber(party: string, accountNumber: string, transferId: number): string {
        const isPartyVisible = this.showAllCharacters[transferId]?.[party === "receiver" ? "receiver" : "sender"];
        return isPartyVisible ? accountNumber : '*'.repeat(accountNumber.length - this.SUFFIX_LENGTH) + accountNumber.slice(-this.SUFFIX_LENGTH);
    }

}