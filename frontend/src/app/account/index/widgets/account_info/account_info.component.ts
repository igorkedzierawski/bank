import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { AccountDTO } from 'src/app/mainsv/data/account_dto';
import { None, Option, Some } from 'src/app/option/option.module';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';

@Component({
    selector: 'account-info-widget',
    templateUrl: './account_info.component.html',
    styleUrls: ['./account_info.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
    ]
})
export class AccountInfoComponent implements OnInit {

    protected accountInfo: AccountDTO | null = null;
    protected displayAccountNumber: string | null = null;
    protected showAllCharacters = false;
    protected errorMessage: Option<string>;

    protected readonly SUFFIX_LENGTH = 3;

    constructor(private readonly reqeng: RequestEngine) { }

    ngOnInit(): void {
        this.getAccountInfo();
    }

    getAccountInfo() {
        this.reqeng.getAccountInfo().subscribe(info => {
            this.accountInfo = info;
            this.updateDisplayAccountNumber();
            this.errorMessage = None;
        }, (error: ErrorDTO) => {
            this.accountInfo = null;
            this.displayAccountNumber = null;
            this.errorMessage = Some(error.message);
        });
    }

    toggleAccountNumberVisibility() {
        this.showAllCharacters = !this.showAllCharacters;
        this.updateDisplayAccountNumber();
    }

    updateDisplayAccountNumber() {
        if (this.accountInfo) {
            const accountNumber = this.accountInfo.accountNumber;
            this.displayAccountNumber = this.showAllCharacters
                ? accountNumber
                : '*'.repeat(accountNumber.length - this.SUFFIX_LENGTH) + accountNumber.slice(-this.SUFFIX_LENGTH);
        }
    }
}
