import { Pipe, PipeTransform } from "@angular/core";

@Pipe({name: 'simplifyDate'})
export class SimplifyDatePipe implements PipeTransform {
    transform(dateIso8601: string): string {
        if (!dateIso8601) {
            return '';
        }
      
        const date = new Date(dateIso8601);
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
        const hours = ('0' + date.getHours()).slice(-2);
        const minutes = ('0' + date.getMinutes()).slice(-2);

        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
}