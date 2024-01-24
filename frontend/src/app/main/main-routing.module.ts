import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';
import { ChangePasswordEmailComponent } from './change_password_email/change_password_email.component';

const routes: Routes = [{
    path: '',
    component: MainComponent,
    children: [
        { path: '', component: IndexComponent },
        { path: 'login', component: LoginComponent },
        { path: 'change_password', component: ChangePasswordEmailComponent },
    ],
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class MainRoutingModule { }