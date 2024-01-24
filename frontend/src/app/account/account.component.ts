import { Component } from '@angular/core';
import { RequestEngine } from '../mainsv/requestengine';
import { Router } from '@angular/router';
import { LoginAttemptDTO_Type } from '../mainsv/data/login_attempt_dto';

@Component({
    selector: 'account-root',
    templateUrl: './account.component.html',
    styleUrls: ['./account.component.scss'],
})
export class AccountComponent {

    constructor(
        protected readonly reqeng: RequestEngine,
        protected readonly router: Router,
    ) { }

    protected gotoAccount() {
        this.router.navigate(['/account']);
    }

}
