import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from './account.component';
import { MakeTransferComponent } from './make_transfer/make_transfer.component';
import { AccountIndexComponent } from './index/index.component';
import { TransfersComponent } from './transfers/transfers.component';
import { ChangePasswordComponent } from './change_password/change_password.component';
import { SensitiveDataComponent } from './sensitive_data/sensitive_data.component';

const routes: Routes = [{
    path: '',
    component: AccountComponent,
    children: [
        { path: '', component: AccountIndexComponent },
        { path: 'make_transfer', component: MakeTransferComponent },
        { path: 'transfers', component: TransfersComponent },
        { path: 'change_password', component: ChangePasswordComponent },
        { path: 'sensitive_data', component: SensitiveDataComponent },
    ],
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class AccountRoutingModule { }