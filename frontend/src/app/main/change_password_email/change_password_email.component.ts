import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { None, Option, Some } from 'src/app/option/option.module';

@Component({
    selector: 'change-password-email',
    templateUrl: './change_password_email.component.html',
    styleUrls: ['./change_password_email.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
    ],
})
export class ChangePasswordEmailComponent {

    protected passwordChangeForm: FormGroup;
    protected passwordChangeSuccess: boolean = false;
    protected errorMessage: Option<string>;
    protected otherMessage: Option<string>;
    protected token: Option<string>;

    constructor(
        private readonly reqeng: RequestEngine,
        private readonly router: Router,
        private readonly route: ActivatedRoute,
        private readonly fb: FormBuilder,
    ) {
        this.passwordChangeForm = this.fb.group({
            newPassword: ['', [Validators.required]],
            confirmNewPassword: ['', [Validators.required]],
        });
    }

    ngOnInit() {
        this.route.queryParamMap.subscribe(params => {
            this.token = Some(params.get('token') || "")
        });
    }

    protected changePassword() {
        if (this.passwordChangeForm.valid) {
            const newPassword = this.passwordChangeForm.value.newPassword;
            const confirmNewPassword = this.passwordChangeForm.value.confirmNewPassword;
            if (newPassword != confirmNewPassword) {
                this.errorMessage = Some("Niepoprawnie powtórzyliście hasło");
                return;
            }
            this.reqeng.changePasswordEmail(this.token!, newPassword).subscribe(
                changed => { this.passwordChangeSuccess = true; },
                (error: ErrorDTO) => {
                    this.errorMessage = Some(error.message);
                }
            )
        }
    }

    protected redirectToLogin() {
        this.router.navigate(['/login']);
    }

}

