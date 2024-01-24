import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { Option, Some } from 'src/app/option/option.module';

@Component({
    selector: 'change-password',
    templateUrl: './change_password.component.html',
    styleUrls: ['./change_password.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
    ],
})
export class ChangePasswordComponent {

    protected passwordChangeForm: FormGroup;
    protected passwordChangeSuccess: boolean = false;
    protected errorMessage: Option<string>;
    protected otherMessage: Option<string>;

    constructor(
        private readonly reqeng: RequestEngine,
        private readonly router: Router,
        private readonly fb: FormBuilder,
    ) {
        this.passwordChangeForm = this.fb.group({
            currentPassword: ['', [Validators.required]],
            newPassword: ['', [Validators.required]],
            confirmNewPassword: ['', [Validators.required]],
        });
    }

    ngOnInit() {
        this.setPasswordVariant();
    }

    protected setPasswordVariant() {
        this.reqeng.getPasswordVariantPatternSelf().subscribe(variant => {
            this.otherMessage = variant.message;
        }, (error: ErrorDTO) => {
            this.errorMessage = Some("Niepoprawny login");
        });
    }

    protected changePassword() {
        if (this.passwordChangeForm.valid) {
            const currentPassword = this.passwordChangeForm.value.currentPassword;
            const newPassword = this.passwordChangeForm.value.newPassword;
            const confirmNewPassword = this.passwordChangeForm.value.confirmNewPassword;
            if (newPassword != confirmNewPassword) {
                this.errorMessage = Some("Niepoprawnie powtórzyliście hasło");
                return;
            }
            this.reqeng.changePassword(currentPassword, newPassword).subscribe(
                changed => { this.passwordChangeSuccess = true; },
                (error: ErrorDTO) => {
                    this.errorMessage = Some(error.message);
                }
            )
        }
    }

    protected redirectToAccount() {
        this.router.navigate(['/account']);
    }

}

