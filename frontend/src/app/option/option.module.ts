import { NgModule } from '@angular/core';
import { Pipe, PipeTransform } from '@angular/core';
import { CommonModule } from '@angular/common';


//type declaration
export type Option<T> = T | undefined;


//explicit type generators
export function Some<T>(value: T): Option<T> {
    return value;
}

export const None: Option<undefined> = undefined;


//utility functions
export function isSome<T>(option: Option<T>): option is T {
    return option !== undefined;
}

export function isNone<T>(option: Option<T>): option is undefined {
    return option === undefined;
}

export function map<T, U>(option: Option<T>, mapper: (value: T) => U): Option<U> {
    return isSome(option) ? Some(mapper(option)) : None;
}

// export function unwrap<T>(option: Option<T>): T { //equivalent to '!'
//     if (isNone(option)) {
//         throw new TypeError('Illegal unwrapping of None');
//     }
//     return option;
// }

export function unwrapOr<T>(option: Option<T>, defaultValue: T): T {
    return isSome(option) ? option : defaultValue;
}

export function isSomeAnd<T>(option: Option<T>, predicate: (value: T) => boolean): boolean {
    return isSome(option) && predicate(option);
}

export function isSomeAndNull<T>(option: Option<T>): boolean {
    return isSome(option) && option === null;
}

export function isSomeAndNotNull<T>(option: Option<T>): boolean {
    return isSome(option) && option !== null;
}


//utility pipes
@Pipe({name: 'isSome'})
export class IsSomePipe implements PipeTransform {transform = isSome;}

@Pipe({name: 'isNone',})
export class IsNonePipe implements PipeTransform {transform = isNone;}

@Pipe({name: 'map',})
export class MapPipe implements PipeTransform {transform = map;}

// @Pipe({name: 'unwrap',}) //equivalent to '!'
// export class UnwrapPipe implements PipeTransform {transform = unwrap;}

@Pipe({name: 'unwrapOr',})
export class UnwrapOrPipe implements PipeTransform {transform = unwrapOr;}

@Pipe({name: 'isSomeAnd',})
export class IsSomeAndPipe implements PipeTransform {transform = isSomeAnd;}

@Pipe({name: 'isSomeAndNull',})
export class IsSomeAndNullPipe implements PipeTransform {transform = isSomeAndNull;}

@Pipe({name: 'isSomeAndNotNull',})
export class IsSomeAndNotNullPipe implements PipeTransform {transform = isSomeAndNotNull;}


//module export
@NgModule({
    declarations: [IsSomePipe, IsNonePipe, MapPipe, UnwrapOrPipe, IsSomeAndPipe, IsSomeAndNullPipe, IsSomeAndNotNullPipe],
    imports: [CommonModule],
    exports: [IsSomePipe, IsNonePipe, MapPipe, UnwrapOrPipe, IsSomeAndPipe, IsSomeAndNullPipe, IsSomeAndNotNullPipe]
})
export class OptionModule {}
