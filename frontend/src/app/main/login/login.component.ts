import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { Option, Some, isSomeAnd } from 'src/app/option/option.module';

@Component({
    selector: 'login',
    standalone: true,
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    imports: [
        CommonModule,
        FormsModule,
    ]
})

export class LoginComponent {

    protected username: string = '';
    protected password: string = '';
    protected errorMessage: Option<string>;
    protected otherMessage: Option<string>;
    protected passwordRequired: boolean = false;

    constructor(
        protected readonly reqeng: RequestEngine,
        protected readonly route: ActivatedRoute,
        protected readonly router: Router,
    ) { }

    ngOnInit() {
        this.route.queryParamMap.subscribe(params => {
            let message = params.get("message");
            if(message) {
                this.errorMessage = Some(message);
            }
        });
    }

    protected checkUsername() {
        this.reqeng.getPasswordVariantPattern(this.username).subscribe(variant =>{
            this.passwordRequired = true;
            this.otherMessage = variant.message;
        }, (error: ErrorDTO) => {
            this.errorMessage = Some("Niepoprawny login");
        });
    }

    protected login() {
        this.reqeng.performLogin(this.username, this.password).subscribe(result => {
            if (isSomeAnd(result.isSuccessful, x => x === true)) {
                this.router.navigate(['/account']);
            } else {
                this.errorMessage = Some(result.message);
            }
        });
    }

    protected changePassword() {
        this.reqeng.changePasswordEmailGenerate(this.username).subscribe(x=>{});
        this.router.navigate(['/']);
    }

}