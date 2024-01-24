import { Injectable, Signal, WritableSignal, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable, map, timeout, take, catchError, throwError, tap, of, ObservableInput, ObservedValueOf, OperatorFunction } from 'rxjs';
import { LoginResponse } from './data/login_response';
import { None, Option, Some, isNone } from '../option/option.module';
import { Page } from './data/page';
import { LoginAttemptDTO, LoginAttemptDTO_Type } from './data/login_attempt_dto';
import { AccountDTO } from './data/account_dto';
import { Router } from '@angular/router';
import { ErrorDTO } from './data/error_dto';
import { MakeTransferForm } from './data/make_transfer_form';
import { DirectedTransferDTO } from './data/directed_transfer_dto';
import { PasswordVariantPatternDTO } from './data/password_variant_pattern_dto';
import { ChangePasswordForm } from './data/change_password_form';
import { ChangePasswordEmailForm } from './data/change_password_email_form';
import { StringValueDTO } from './data/string_value';

@Injectable({
    providedIn: 'root'
})
export class RequestEngine {

    private readonly STATIC_TEST = '/api/v1/debug/test';
    private readonly PERFORM_LOGIN = '/api/v1/auth/perform_login';

    private readonly CHANGE_PASSWORD = '/api/v1/password/change_password';
    private readonly CHANGE_PASSWORD_EMAIL = '/api/v1/password/change_password_email';
    private readonly CHANGE_PASSWORD_EMAIL_GENERATE = '/api/v1/password/change_password_email_generate';

    private readonly LOGIN_GET_PATTERN = '/api/v1/auth/get_pattern';
    private readonly LOGIN_GET_PATTERN_SELF = '/api/v1/auth/get_pattern_self';
    private readonly LOGIN_ERROR = '/api/v1/auth/login_error';
    private readonly LOGIN_SUCCESS = '/api/v1/auth/login_success';

    private readonly SENSITIVE_GET_ID_NUMBER = '/api/v1/sensitive_data/get_id_number';
    private readonly SENSITIVE_GET_CREDIT_CARD_NUMBER = '/api/v1/sensitive_data/get_credit_card_number';

    private readonly LOGIN_ATTEMPT_PAGE = '/api/v1/login_attempt/page';

    private readonly ACCOUNT_MAKE_TRANSFER = '/api/v1/account/make_transfer';
    private readonly ACCOUNT_INFO = '/api/v1/account/info';
    private readonly TRANSFERS_PAGE = '/api/v1/account/transfers_page';

    private readonly PROT = '/api/v1/prot';

    constructor(
        private readonly http: HttpClient,
        private readonly router: Router,
    ) { }

    public test(): Observable<string> {
        return this.http.get<string>(this.getOrigin() + this.STATIC_TEST, {})
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public prot(): Observable<string> {
        return this.http.get<string>(this.getOrigin() + this.PROT, {})
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public performLogin(username: string, password: string): Observable<LoginResponse> {
        const formData = new FormData();
        formData.append('username', username);
        formData.append('password', password);
        return this.http.post<any>(this.getOrigin() + this.PERFORM_LOGIN, formData, { observe: 'response', withCredentials: true })
            .pipe(
                this.handleAndRemapErrors(), catchError(() => { return of(null); }), take(1),
                map(response => {
                    const pathname = new URL(response.url || "").pathname;
                    let isSuccessful: Option<boolean> = None;
                    if (pathname === this.LOGIN_SUCCESS) {
                        isSuccessful = Some(true);
                    } else if (pathname === this.LOGIN_ERROR) {
                        isSuccessful = Some(false);
                    }
                    return {
                        isSuccessful: isSuccessful,
                        message: response.body,
                    };
                })
            );
    }

    public changePassword(currentPasswordVariant: string, newPasswordFull: string): Observable<any> {
        const form: ChangePasswordForm = {
            currentPasswordVariant: currentPasswordVariant,
            newPasswordFull: newPasswordFull,
        }
        return this.http.post<any>(this.getOrigin() + this.CHANGE_PASSWORD, form, { observe: 'response', withCredentials: true })
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public changePasswordEmail(token: string, newPasswordFull: string): Observable<any> {
        const form: ChangePasswordEmailForm = {
            token: token,
            newPasswordFull: newPasswordFull,
        }
        return this.http.post<any>(this.getOrigin() + this.CHANGE_PASSWORD_EMAIL, form, { observe: 'response', withCredentials: true })
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public changePasswordEmailGenerate(login: string): Observable<any> {
        return this.http.get<any>(this.getOrigin() + this.CHANGE_PASSWORD_EMAIL_GENERATE, { params: { login: login } })
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public getLogginAttempts(page: number, size: number, type: LoginAttemptDTO_Type | null): Observable<Page<LoginAttemptDTO>> {
        let params: any = {
            page: page,
            size: size
        };
        if (type !== null) {
            params.type = type;
        }
        return this.http.get<Page<LoginAttemptDTO>>(this.getOrigin() + this.LOGIN_ATTEMPT_PAGE, {
            params: params
        }).pipe(this.handleAndRemapErrors(), take(1));
    }

    public getAccountInfo(): Observable<AccountDTO> {
        return this.http.get<AccountDTO>(this.getOrigin() + this.ACCOUNT_INFO, {})
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public getPasswordVariantPattern(login: string): Observable<PasswordVariantPatternDTO> {
        return this.http.get<PasswordVariantPatternDTO>(this.getOrigin() + this.LOGIN_GET_PATTERN, { params: { login: login } })
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public getPasswordVariantPatternSelf(): Observable<PasswordVariantPatternDTO> {
        return this.http.get<PasswordVariantPatternDTO>(this.getOrigin() + this.LOGIN_GET_PATTERN_SELF, {})
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public getSensitiveData_IdNumber(): Observable<StringValueDTO> {
        return this.http.get<StringValueDTO>(this.getOrigin() + this.SENSITIVE_GET_ID_NUMBER, {})
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public getSensitiveData_CreditCardNumber(): Observable<StringValueDTO> {
        return this.http.get<StringValueDTO>(this.getOrigin() + this.SENSITIVE_GET_CREDIT_CARD_NUMBER, {})
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public makeTransfer(senderAccountNumber: string, receiverAccountNumber: string, transferAmount: number, title: string): Observable<any> {
        const form: MakeTransferForm = {
            senderAccountNumber: senderAccountNumber,
            receiverAccountNumber: receiverAccountNumber,
            transferAmount: transferAmount,
            title: title,
        }
        return this.http.post<any>(this.getOrigin() + this.ACCOUNT_MAKE_TRANSFER, form, { observe: 'response', withCredentials: true })
            .pipe(this.handleAndRemapErrors(), take(1));
    }

    public getTransfers(page: number, size: number): Observable<Page<DirectedTransferDTO>> {
        return this.http.get<Page<DirectedTransferDTO>>(this.getOrigin() + this.TRANSFERS_PAGE, {
            params: {
                page: page,
                size: size
            }
        }).pipe(this.handleAndRemapErrors(), take(1));
    }

    private getOrigin(): string {
        return window.location.origin;
    }

    private handleAndRemapErrors(): (source: Observable<any>) => Observable<any> {
        return catchError((error) => {
            if (error.status === 401) {
                const authErrorDTO: ErrorDTO = {
                    message: "Sesja wygasła, zaloguj się ponownie!",
                    type: "AuthError",
                };
                this.router.navigate(['/login'], {
                    queryParams: {
                        message: authErrorDTO.message,
                    }
                });
                return throwError(() => authErrorDTO);
            } else if (error.status === 400) {
                const errorDTO: ErrorDTO = error.error || {};
                errorDTO.message = errorDTO.message || "unknown";
                errorDTO.type = errorDTO.type || "unknown";
                return throwError(() => errorDTO);
            } else {
                const errorDTO: ErrorDTO = {
                    message: `${error.status} ${error["statusText"]}`,
                    type: `HttpCode${error.status}`,
                }
                return throwError(() => errorDTO);
            }
        });
    }

}
