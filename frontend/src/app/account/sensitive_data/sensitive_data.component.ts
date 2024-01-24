import { Component, OnInit } from '@angular/core';
import { StringValueDTO } from 'src/app/mainsv/data/string_value';import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RequestEngine } from 'src/app/mainsv/requestengine';

@Component({
    selector: 'sensitive-data',
    templateUrl: './sensitive_data.component.html',
    styleUrls: ['./sensitive_data.component.scss'],
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
    ],
})
export class SensitiveDataComponent implements OnInit {

    dataVisible: boolean = false;
    idNumber: string = "";
    creditCardNumber: string = "";

    constructor(private readonly reqeng: RequestEngine) {}

    ngOnInit() {
        this.fetchSensitiveData();
    }

    private fetchSensitiveData() {
        this.reqeng.getSensitiveData_IdNumber().subscribe(
            (data: StringValueDTO) => this.idNumber = data.value,
            error => console.error('Error fetching Id Number:', error)
        );

        this.reqeng.getSensitiveData_CreditCardNumber().subscribe(
            (data: StringValueDTO) => this.creditCardNumber = data.value,
            error => console.error('Error fetching Credit Card Number:', error)
        );
    }

    showData() {
        this.dataVisible = true;
    }

    hideData() {
        this.dataVisible = false;
    }
}