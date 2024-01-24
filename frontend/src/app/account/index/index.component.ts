import { Component } from '@angular/core';
import { LoginAttemptsWidgetComponent } from './widgets/login_attempt/login_attempt.component';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { Router } from '@angular/router';
import { AccountInfoComponent as AccountInfoWidgetComponent } from './widgets/account_info/account_info.component';

@Component({
    selector: 'account-index',
    templateUrl: './index.component.html',
    styleUrls: ['./index.component.scss'],
    standalone: true,
    imports: [
        LoginAttemptsWidgetComponent,
        AccountInfoWidgetComponent
    ],
})
export class AccountIndexComponent {
    constructor(
        protected readonly reqeng: RequestEngine,
        protected readonly router: Router,
    ) { }

    protected gotoMakeTransfer() {
        this.router.navigate(['/account/make_transfer']);
    }

    protected gotoTransfers() {
        this.router.navigate(['/account/transfers']);
    }

    protected gotoChangePassword() {
        this.router.navigate(['/account/change_password']);
    }

    protected gotoSensitiveData() {
        this.router.navigate(['/account/sensitive_data']);
    }

    protected logout() {
        window.location.href = new URL("/api/v1/auth/perform_logout", window.location.origin).toString();
    }
}
