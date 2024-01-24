import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    { path: '', loadChildren: () => import('./main/main-routing.module').then((m) => m.MainRoutingModule) },
    { path: 'account', loadChildren: () => import('./account/account-routing.module').then((m) => m.AccountRoutingModule) },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule { }
