import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RequestEngine } from 'src/app/mainsv/requestengine';
import { LoginAttemptDTO, LoginAttemptDTO_Type } from 'src/app/mainsv/data/login_attempt_dto';
import { FormsModule } from '@angular/forms';
import { None, Option, Some } from 'src/app/option/option.module';
import { ErrorDTO } from 'src/app/mainsv/data/error_dto';

@Component({
    selector: 'login-attempts-widget',
    templateUrl: './login_attempt.component.html',
    styleUrls: ['./login_attempt.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        FormsModule
    ]
})
export class LoginAttemptsWidgetComponent implements OnInit {

    protected loginAttempts: LoginAttemptDTO[] = [];
    protected selectedStatus: LoginAttemptDTO_Type | null = null;
    protected currentPage = 1;
    protected totalPages = 1;
    protected errorMessage: Option<string>;

    readonly SUCCESS_OPTION = LoginAttemptDTO_Type.SUCCESS;
    readonly FAILURE_OPTION = LoginAttemptDTO_Type.FAILURE;
    readonly ALL_OPTION = null;

    constructor(
        protected readonly reqeng: RequestEngine,
        protected readonly router: Router,
    ) { }

    ngOnInit(): void {
        this.loadLoginAttempts();
    }

    loadLoginAttempts() {
        this.reqeng.getLogginAttempts(this.currentPage - 1, 8, this.selectedStatus).subscribe(loginAttemptPage => {
            if (loginAttemptPage) {
                this.loginAttempts = loginAttemptPage.content;
                this.totalPages = loginAttemptPage.totalPages;
            }
            this.errorMessage = None;
        }, (error: ErrorDTO) => {
            this.loginAttempts = [];
            this.currentPage = 1;
            this.totalPages = 1;
            this.errorMessage = Some(error.message);
        });
    }

    onRefreshClick() {
        this.loadLoginAttempts();
    }

    onPageChange(newPage: number): void {
        this.currentPage = newPage;
        this.loadLoginAttempts();
    }

    onRadioChange() {
        this.currentPage = 1;
        this.loadLoginAttempts();
    }
}
